package br.com.caramelo.idwallteste.ui.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract val viewModels: Array<BaseViewModel>

    inline fun <reified T : ViewModel> viewModel(factory: ViewModelProvider.Factory? = null): Lazy<T> {
        return lazy {
            ViewModelProviders.of(this, factory)
                .get(T::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModels.forEach {
            it.mediator.observe(this, Observer(::render))
            lifecycle.addObserver(it)
        }
    }

    abstract fun render(state: State)
}