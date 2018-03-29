package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.Session
import br.com.caramelo.idwallteste.data.model.request.AuthRequest
import kotlinx.coroutines.experimental.async

open class AuthRepository(
        private val api: IDdogAPI
) {
    open fun auth(email: String): RepositoryLiveData<Boolean> {
        val liveData = RepositoryLiveData<Boolean>()
        val body = AuthRequest(email)
        async {
            try {
                val response = api.signup(body).await()
                Session.user = response.user
                liveData.postValue(true)
            } catch (t: Throwable) {
//                liveData.postThowable(t)
                liveData.postValue(false)
            }
        }
        return liveData
    }
}