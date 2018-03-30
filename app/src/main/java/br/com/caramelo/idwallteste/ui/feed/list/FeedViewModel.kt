package br.com.caramelo.idwallteste.ui.feed.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.data.repository.FeedRepository
import br.com.caramelo.idwallteste.kodein
import com.github.salomonbrys.kodein.instance

open class FeedViewModel(
        private val category: DogCategory,
        private val repository: FeedRepository = kodein.instance()
) : ViewModel() {

    var tryAgainLiveData = MutableLiveData<Boolean>()
    var loadingLiveData = MutableLiveData<Boolean>()
    var feedLiveData: MutableLiveData<Feed>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                requestFeed()
            }
            return field
        }

    open fun requestFeed() {
        tryAgainLiveData.postValue(false)
        loadingLiveData.postValue(true)
        repository.feed(category)
                .observer { feed ->
                    feedLiveData?.postValue(feed)
                    loadingLiveData.postValue(false)
                }
                .observerThrowable {
                    tryAgainLiveData.postValue(true)
                    loadingLiveData.postValue(false)
                }
    }
}