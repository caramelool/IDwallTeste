package br.com.caramelo.idwallteste.ui.base

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main

abstract class BaseViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel(), LifecycleObserver {

    abstract val mediator: MediatorLiveData<State>

    private val job = Job()

    private var initialized: Boolean = false

    protected val uiScope by lazy { CoroutineScope(dispatcher + job) }

    protected fun <T>  MediatorLiveData<State>.addSource(source: LiveData<T>) {
        addSource(source) {
            initialized = true
            value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        initialized = false
    }

    protected fun CoroutineScope.cancel() {
        run {
            job.cancel()
        }
    }

    protected fun isInitialized() = initialized
}