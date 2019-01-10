package br.com.caramelo.idwallteste.ui.feed.category

import br.com.caramelo.idwallteste.ui.feed.list.FeedFragment
import br.com.caramelo.idwallteste.ui.feed.list.FeedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FeedCategoryModule {

    @ContributesAndroidInjector(modules = [FeedModule::class])
    abstract fun contributesFeedFragment(): FeedFragment

}