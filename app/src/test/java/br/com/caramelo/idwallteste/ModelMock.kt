package br.com.caramelo.idwallteste

import br.com.caramelo.idwallteste.data.model.entity.Feed
import com.google.gson.Gson

object ModelMock {

    private inline fun <reified T> mockJson(file: String): T {
        val content = javaClass.classLoader?.getResourceAsStream(file)
                ?.reader()?.readText()
        return Gson().fromJson(content, T::class.java)
    }

    val AUTH by lazy { mockJson<Feed>("mock_feed.json") }

    val FEED by lazy { mockJson<Feed>("mock_auth.json") }
}