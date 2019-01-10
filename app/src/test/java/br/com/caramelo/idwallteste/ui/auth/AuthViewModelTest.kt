package br.com.caramelo.idwallteste.ui.auth

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import br.com.caramelo.idwallteste.data.repository.AuthRepository
import br.com.caramelo.idwallteste.testDispatcher
import br.com.caramelo.idwallteste.ui.base.State
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: AuthRepository

    @Mock
    lateinit var observerState: Observer<State>

    private lateinit var viewModel: AuthViewModel

    private val email = "lucascaramelo@gmail.com"

    @Before
    fun before()  {
        viewModel = spy(AuthViewModel(repository, testDispatcher))
        viewModel.mediator.observeForever(observerState)
    }

    @Test
    fun `shouldn't authentication the user when received a incorrect email `() = runBlocking {
        viewModel.setEmail("blablabla")
        viewModel.auth()

        val argumentCaptor = ArgumentCaptor.forClass(Any::class.java)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())

            val (buttonVisibility, invalidEmail) = allValues

            assertEquals(AuthViewModelState.ButtonVisibility(true), buttonVisibility)
            assertEquals(true, invalidEmail is AuthViewModelState.InvalidEmailAddress)
        }
    }

    @Test
    fun `should authentication the user when received a correct email `() = runBlocking {

        doReturn(true)
            .whenever(repository).auth(email)

        viewModel.setEmail(email)
        viewModel.auth()

        val argumentCaptor = ArgumentCaptor.forClass(Any::class.java)
        val buttonVisibilityExpected = AuthViewModelState.ButtonVisibility(true)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())

            val (buttonVisibility, inProgress, response) = allValues

            assertEquals(AuthViewModelState.ButtonVisibility(true), buttonVisibility)

            assertEquals(buttonVisibilityExpected, buttonVisibility)
            assertEquals(true, inProgress is AuthViewModelState.InProgressAddress)
            assertEquals(true, response is AuthViewModelState.Success)
        }
    }

    @Test
    fun `should show fail when authentication return a bad request `() = runBlocking {

        doReturn(false)
            .whenever(repository).auth(email)

        viewModel.setEmail(email)
        viewModel.auth()

        val argumentCaptor = ArgumentCaptor.forClass(Any::class.java)
        val buttonVisibilityExpected = AuthViewModelState.ButtonVisibility(true)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())

            val (buttonVisibility, inProgress, response) = allValues

            assertEquals(AuthViewModelState.ButtonVisibility(true), buttonVisibility)

            assertEquals(buttonVisibilityExpected, buttonVisibility)
            assertEquals(true, inProgress is AuthViewModelState.InProgressAddress)
            assertEquals(true, response is AuthViewModelState.Fail)
        }
    }
}