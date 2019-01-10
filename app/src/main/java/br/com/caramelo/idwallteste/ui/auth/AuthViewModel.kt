package br.com.caramelo.idwallteste.ui.auth

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Patterns
import br.com.caramelo.idwallteste.data.repository.AuthRepository
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel(
    private val repository: AuthRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(dispatcher) {

    companion object {
        private const val EMAIL_REGEX = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")
    }

    private val stateLiveData = MutableLiveData<AuthViewModelState>()
    private val buttonVisibilityLiveData = MutableLiveData<AuthViewModelState.ButtonVisibility>()
    private val emailLiveData = MutableLiveData<AuthViewModelState.Email>()

    override val mediator: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(stateLiveData)
            addSource(buttonVisibilityLiveData)
        }

    fun setEmail(email: String) {
        emailLiveData.value = AuthViewModelState.Email(email)
        buttonVisibilityLiveData.postValue(AuthViewModelState.ButtonVisibility(email.isNotBlank()))
    }

    fun auth() {
        uiScope.launch {
            val email = emailLiveData.value?.email ?: ""
            if (isValidEmail(email).not()) {
                stateLiveData.postValue(AuthViewModelState.InvalidEmailAddress)
                return@launch
            }
            stateLiveData.postValue(AuthViewModelState.InProgressAddress)
            val isSuccess = repository.auth(email)
            when (isSuccess) {
                true -> stateLiveData.postValue(AuthViewModelState.Success)
                else -> stateLiveData.postValue(AuthViewModelState.Fail)
            }
        }

    }

    private fun isValidEmail(email: String) = EMAIL_REGEX.toRegex().matches(email)

    /**
     * ViewModel Factory
     */

    class Factory @Inject constructor(
        private val repository: AuthRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthViewModel(repository) as T
        }
    }
}

sealed class AuthViewModelState {
    data class Email(val email: String) : AuthViewModelState()
    data class ButtonVisibility(val visible: Boolean) : AuthViewModelState()
    object InvalidEmailAddress : AuthViewModelState()
    object InProgressAddress : AuthViewModelState()
    object Fail : AuthViewModelState()
    object Success : AuthViewModelState()
}