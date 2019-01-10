package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.Session
import br.com.caramelo.idwallteste.data.model.request.AuthRequest
import br.com.caramelo.idwallteste.ext.RequestException
import br.com.caramelo.idwallteste.ext.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository(
    private val api: IDdogAPI
) {
    suspend fun auth(email: String): Boolean {
//mock for tests
//        return suspendCoroutine {
//            CoroutineScope(it.context).launch {
//                delay(1000)
//                it.resume(true)
//            }
//        }
        return try {
            val body = AuthRequest(email)
            val response = api.signup(body).await()
            Session.user = response?.user
            response?.user != null
        } catch (e: RequestException) {
            false
        }
    }
}