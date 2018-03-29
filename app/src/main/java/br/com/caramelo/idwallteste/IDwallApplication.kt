package br.com.caramelo.idwallteste

import android.app.Application
import br.com.caramelo.idwallteste.data.di.picassoModule
import br.com.caramelo.idwallteste.data.di.repositoryModule
import br.com.caramelo.idwallteste.data.di.retrofitModule
import com.github.salomonbrys.kodein.*
import com.squareup.picasso.Picasso

class IDwallApplication : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        bind<IDwallApplication>() with singleton { instance }
        import(retrofitModule)
        import(repositoryModule)
        import(picassoModule)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        configPicasso()
    }

    private fun configPicasso() {
        val picasso: Picasso = instance()
        picasso.setIndicatorsEnabled(false)
        picasso.isLoggingEnabled = false
        Picasso.setSingletonInstance(picasso)
    }
}

private lateinit var instance: IDwallApplication
val kodein by lazy { instance.kodein }
