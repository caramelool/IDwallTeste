package br.com.caramelo.idwallteste.ui.feed.list

import android.arch.lifecycle.*
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.data.repository.FeedRepository
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val category: DogCategory,
    private val repository: FeedRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(dispatcher) {

    private val tryAgainLiveData = MutableLiveData<FeedViewModelState.TryAgain>()
    private val loadingLiveData = MutableLiveData<FeedViewModelState.Loading>()
    private val feedLiveData = MutableLiveData<FeedViewModelState.FeedList>()

    override val mediator: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(tryAgainLiveData)
            addSource(loadingLiveData)
            addSource(feedLiveData)
        }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        if (isInitialized()) return
        requestFeed()
    }

    fun requestFeed() {
        uiScope.launch {
            hideTryAgain()
            showLoading()
            val feed = repository.feed(category)
            when (feed) {
                null -> {
                    showTryAgain()
                    hideLoading()
                }
                else -> {
                    feedLiveData.postValue(FeedViewModelState.FeedList(feed))
                    hideLoading()
                }
            }
        }
    }

    private fun showLoading() {
        loadingLiveData.postValue(FeedViewModelState.Loading(true))
    }

    private fun hideLoading() {
        loadingLiveData.postValue(FeedViewModelState.Loading(false))
    }

    private fun showTryAgain() {
        tryAgainLiveData.postValue(FeedViewModelState.TryAgain(true))
    }

    private fun hideTryAgain() {
        tryAgainLiveData.postValue(FeedViewModelState.TryAgain(false))
    }

    class Factory @Inject constructor(
        private val category: DogCategory,
        private val repository: FeedRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FeedViewModel(category, repository) as T
        }

    }
}

sealed class FeedViewModelState {
    data class TryAgain(val visible: Boolean) : FeedViewModelState()
    data class Loading(val visible: Boolean) : FeedViewModelState()
    data class FeedList(val feed: Feed?) : FeedViewModelState()
}