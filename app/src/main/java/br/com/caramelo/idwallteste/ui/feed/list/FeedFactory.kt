package br.com.caramelo.idwallteste.ui.feed.list

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import br.com.caramelo.idwallteste.data.model.entity.DogCategory

class FeedFactory(
        bundle: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeedViewModel(category) as T
    }

    private val category = when(bundle?.getString(PARAM_FEED_CATEGORY)) {
        DogCategory.HOUND.name -> DogCategory.HOUND
        DogCategory.PUG.name -> DogCategory.PUG
        DogCategory.LABRADOR.name -> DogCategory.LABRADOR
        DogCategory.HUSKY.name -> DogCategory.HUSKY
        else -> DogCategory.HUSKY
    }
}