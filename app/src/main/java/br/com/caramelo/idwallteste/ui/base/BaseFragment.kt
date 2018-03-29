package br.com.caramelo.idwallteste.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.github.salomonbrys.kodein.android.KodeinSupportFragment
import kotlin.reflect.KClass

abstract class BaseFragment : KodeinSupportFragment() {
    fun <T: ViewModel> providesOf(modelClass: KClass<T>, factory: ViewModelProvider.Factory? = null): T {
        return ViewModelProviders.of(this, factory)
                .get(modelClass.java)
    }
}