package br.com.caramelo.idwallteste.data.di

import br.com.caramelo.idwallteste.IDwallApplication
import br.com.caramelo.idwallteste.data.di.module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RetrofitModule::class,
        RepositoryModule::class,
        PicassoModule::class,
        AndroidInjectionModule::class,
        ContributesModule::class
    ]
)
interface AppComponent : AndroidInjector<IDwallApplication>