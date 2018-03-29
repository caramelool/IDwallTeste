package br.com.caramelo.idwallteste.ui.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Patterns
import br.com.caramelo.idwallteste.data.repository.AuthRepository
import br.com.caramelo.idwallteste.kodein
import com.github.salomonbrys.kodein.instance

open class AuthViewModel(
        private val repository: AuthRepository = kodein.instance()
) : ViewModel() {

    open val stepLiveData = MutableLiveData<AuthStep>()

    open fun auth(email: String) {
        if (!isValidEmail(email)) {
            stepLiveData.postValue(AuthStep.INVALID_EMAIL_ADDRESS)
            return
        }
        stepLiveData.postValue(AuthStep.IN_PROGRESS)
        repository.auth(email)
                .observer {
                    if (it == true) {
                        stepLiveData.postValue(AuthStep.SUCCESS)
                    } else {
                        stepLiveData.postValue(AuthStep.FAIL)
                    }
                }
    }

    open fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}