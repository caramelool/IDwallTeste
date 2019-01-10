package br.com.caramelo.idwallteste.ui.feed.list

import android.support.v4.app.Fragment
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import dagger.Module
import dagger.Provides

@Module
class FeedModule {

    @Provides
    fun providesActivity(fragment: FeedFragment) : Fragment {
        return fragment
    }

    @Provides
    fun providesCategory(fragment: FeedFragment) : DogCategory {
        return when (fragment.arguments?.getString(PARAM_FEED_CATEGORY)) {
            DogCategory.HOUND.name -> DogCategory.HOUND
            DogCategory.PUG.name -> DogCategory.PUG
            DogCategory.LABRADOR.name -> DogCategory.LABRADOR
            DogCategory.HUSKY.name -> DogCategory.HUSKY
            else -> DogCategory.HUSKY
        }
    }

}