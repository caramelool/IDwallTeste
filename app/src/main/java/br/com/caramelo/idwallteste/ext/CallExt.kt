package br.com.caramelo.idwallteste.ext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RequestException : IllegalStateException {
    constructor(s: String?) : super(s)
    constructor(cause: Throwable?) : super(cause)
}

suspend fun <T> Call<T>.awaitNoNull(): T {
    return await() ?: throw RequestException(NullPointerException())
}

suspend fun <T> Call<T>.await(): T? {
    return suspendCoroutine {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                with(response) {
                    if (isSuccessful) {
                        it.resume(body())
                    } else {
                        it.resumeWithException(RequestException(message()))
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resumeWithException(RequestException(t))
            }
        })
    }
}