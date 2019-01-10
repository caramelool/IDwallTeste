package br.com.caramelo.idwallteste.data.di.module

import br.com.caramelo.idwallteste.ui.auth.AuthActivity
import br.com.caramelo.idwallteste.ui.feed.category.FeedCategoryActivity
import br.com.caramelo.idwallteste.ui.feed.category.FeedCategoryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ContributesModule {

    @ContributesAndroidInjector
    fun contributesAuthActivity(): AuthActivity

    @ContributesAndroidInjector(modules = [FeedCategoryModule::class])
    fun contributesFeedActivity(): FeedCategoryActivity

}