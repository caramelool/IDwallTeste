package br.com.caramelo.idwallteste

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.caramelo.idwallteste.data.di.repositoryModule
import br.com.caramelo.idwallteste.data.di.retrofitModule
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.lazy
import com.github.salomonbrys.kodein.with
import com.google.gson.Gson
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

/**
 * Created by lucascaramelo on 16/03/2018.
 */
@RunWith(JUnit4::class)
open class BaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    val server: MockWebServer by lazy {
        MockWebServer()
    }

    val kodein by Kodein.lazy {
        import(retrofitModule)
        import(repositoryModule)
        constant(tag = "baseUrl", overrides = true) with server.url("/").toString()
    }

    @Before
    open fun `before each test`() {
        server.start()
    }

    @After
    open fun `after each test`() {
        server.shutdown()
    }

    inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

    inline fun <reified T : Any> mockFromJson(json: String): T
            = Gson().fromJson(json, T::class.java)

    fun await(millis: Long = 200) = Thread.sleep(millis)
}