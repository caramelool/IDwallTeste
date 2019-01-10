package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.ext.RequestException
import br.com.caramelo.idwallteste.ext.awaitNoNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FeedRepository(
    private val api: IDdogAPI
) {
    suspend fun feed(category: DogCategory): Feed? {
//mock for tests
//        return suspendCoroutine {
//            CoroutineScope(it.context).launch {
//                delay(1000)
//                val response = Feed(
//                    category = category.name,
//                    list = (0..50).map {
//                        listOf(
//                            "https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg",
//                            "https://ae01.alicdn.com/kf/HTB1ezzzOFXXXXaNXXXXq6xXFXXXN/2017-Venda-Hot-Sale-Ret-ngulo-Pintura-Pintura-Pintura-em-Spray-Sem-Precipita-o-Rosto-Triste.jpg",
//                            "https://img.huffingtonpost.com/asset/5b7fdeab1900001d035028dc.jpeg?cache=sixpwrbb1s&ops=1910_1000"
//                        ).random()
//                    }
//                )
//                it.resume(response)
//            }
//        }
        return try {
            val name = category.name.toLowerCase()
            api.feed(name).awaitNoNull()
        } catch (t: RequestException) {
            null
        }
    }
}