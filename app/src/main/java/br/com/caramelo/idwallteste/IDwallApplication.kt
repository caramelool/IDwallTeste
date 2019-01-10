package br.com.caramelo.idwallteste

import android.app.Activity
import android.app.Application
import br.com.caramelo.idwallteste.data.di.AppComponent
import br.com.caramelo.idwallteste.data.di.DaggerAppComponent
import br.com.caramelo.idwallteste.data.di.module.AppModule
import com.squareup.picasso.Picasso
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class IDwallApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var picasso: Picasso

    private val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this@IDwallApplication))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        Picasso.setSingletonInstance(picasso)
    }

    override fun activityInjector() = activityDispatchingAndroidInjector
}
