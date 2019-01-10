package br.com.caramelo.idwallteste.ui.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

abstract class BaseFragment : Fragment() {
    abstract val viewModels: Array<BaseViewModel>

    inline fun <reified T : ViewModel> viewModel(factory: ViewModelProvider.Factory? = null): Lazy<T> {
        return lazy {
            ViewModelProviders.of(this, factory)
                .get(T::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModels.forEach {
            it.mediator.observe(this, Observer(::render))
            lifecycle.addObserver(it)
        }
    }

    abstract fun render(state: State)
}