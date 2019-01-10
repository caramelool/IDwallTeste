package br.com.caramelo.idwallteste.ui.feed.category

import android.arch.lifecycle.*
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.repository.CategoryRepository
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import kotlinx.coroutines.launch
import javax.inject.Inject

open class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : BaseViewModel() {

    private val categoryLiveData = MutableLiveData<CategoryViewModelState.CategoryList>()

    override val mediator: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(categoryLiveData)
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun requestCategories() {
        if (categoryLiveData.value != null) return
        uiScope.launch {
            val list = repository.categories()
            val state = CategoryViewModelState.CategoryList(list)
            categoryLiveData.postValue(state)
        }
    }

    class Factory @Inject constructor(
        private val repository: CategoryRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CategoryViewModel(repository) as T
        }
    }
}

sealed class CategoryViewModelState {
    data class CategoryList(val list: List<DogCategory>) : CategoryViewModelState()
}