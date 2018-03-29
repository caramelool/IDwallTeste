package br.com.caramelo.idwallteste.ui.auth

import android.arch.lifecycle.Observer
import br.com.caramelo.idwallteste.BaseTest
import br.com.caramelo.idwallteste.data.model.entity.Session
import br.com.caramelo.idwallteste.data.repository.AuthRepository
import com.github.salomonbrys.kodein.instance
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.*
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito.*

class AuthViewModelTest: BaseTest() {

    private lateinit var repository: AuthRepository
    private lateinit var viewModel: AuthViewModel
    private val email = "lucascaramelo@gmail.com"

    @Before
    override fun `before each test`() {
        super.`before each test`()
        repository = spy(kodein.instance<AuthRepository>())
        viewModel = spy(AuthViewModel(repository))
        Session.user = null
    }

    @Test
    fun `shouldn't authentication the user when received a incorrect email `() {
        val stepObserver: Observer<AuthStep> = mock()

        val email = "blablablabla"

        viewModel.stepLiveData.observeForever(stepObserver)

        doReturn(false).`when`(viewModel).isValidEmail(email)

        viewModel.auth(email)

        verify(viewModel).isValidEmail(email)
        verify(stepObserver).onChanged(AuthStep.INVALID_EMAIL_ADDRESS)
        verify(repository, never()).auth(email)
    }

    @Test
    fun `should authentication the user when received a correct email `() {
        val stepObserver: Observer<AuthStep> = mock()

        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(AUTH_RESPONSE_200))

        doReturn(true).`when`(viewModel).isValidEmail(email)

        viewModel.stepLiveData.observeForever(stepObserver)

        viewModel.auth(email)

        verify(viewModel).isValidEmail(email)
        verify(stepObserver).onChanged(AuthStep.IN_PROGRESS)
        verify(repository).auth(email)

        await()

        verify(stepObserver).onChanged(AuthStep.SUCCESS)
        assertNotNull(Session.user?.token)
    }

    @Test
    fun `should show fail when authentication return a bad request `() {
        val stepObserver: Observer<AuthStep> = mock()

        server.enqueue(MockResponse()
                .setResponseCode(400))

        doReturn(true).`when`(viewModel).isValidEmail(email)

        viewModel.stepLiveData.observeForever(stepObserver)

        viewModel.auth(email)

        verify(viewModel).isValidEmail(email)
        verify(stepObserver).onChanged(AuthStep.IN_PROGRESS)
        verify(repository).auth(email)

        await()

        verify(stepObserver).onChanged(AuthStep.FAIL)
        assertNull(Session.user?.token)
    }

    private val AUTH_RESPONSE_200 = "{\n" +
            "    \"user\": {\n" +
            "        \"_id\": \"5abbc826cb31ee004edc2769\",\n" +
            "        \"email\": \"$email\",\n" +
            "        \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJpZGRvZyIsInN1YiI6IjVhYmJjODI2Y2IzMWVlMDA0ZWRjMjc2OSIsImlhdCI6MTUyMjI1NTkxMCwiZXhwIjoxNTIzNTUxOTEwfQ.NXcsIZF0s4nPX1HScrOcym8eit9d6R9HwDG4xwbENNg\",\n" +
            "        \"createdAt\": \"2018-03-28T16:51:50.379Z\",\n" +
            "        \"updatedAt\": \"2018-03-28T16:51:50.379Z\",\n" +
            "        \"__v\": 0\n" +
            "    }\n" +
            "}"
}