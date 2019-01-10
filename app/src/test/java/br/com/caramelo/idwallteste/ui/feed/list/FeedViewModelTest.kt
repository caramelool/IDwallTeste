package br.com.caramelo.idwallteste.ui.feed.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import br.com.caramelo.idwallteste.ModelMock
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.data.repository.FeedRepository
import br.com.caramelo.idwallteste.testDispatcher
import br.com.caramelo.idwallteste.ui.auth.AuthViewModelState
import br.com.caramelo.idwallteste.ui.base.State
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FeedViewModelTest: LifecycleOwner {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: FeedRepository

    @Mock
    lateinit var observerState: Observer<State>

    private lateinit var viewModel: FeedViewModel
    private var category = DogCategory.HUSKY
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    @Before
    fun before() {
        viewModel = spy(FeedViewModel(category, repository, testDispatcher))
        viewModel.mediator.observeForever(observerState)
        lifecycle.addObserver(viewModel)
    }

    @Test
    fun `should request all dogs images correspond the category`() = runBlocking {

        val mock = ModelMock.FEED

        doReturn(mock)
            .whenever(repository)
            .feed(category)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        val argumentCaptor = ArgumentCaptor.forClass(Any::class.java)

        argumentCaptor.run {
            verify(observerState, times(4)).onChanged(capture())

            val (hideTryAgain, showLoading, feed, hideLoading) = allValues

            Assert.assertEquals(FeedViewModelState.TryAgain(false), hideTryAgain)
            Assert.assertEquals(FeedViewModelState.Loading(true), showLoading)
            Assert.assertEquals(FeedViewModelState.FeedList(mock), feed)
            Assert.assertEquals(FeedViewModelState.Loading(false), hideLoading)
        }
    }

    @Test
    fun `should try again when received a null feed`() = runBlocking {

        doReturn(null)
            .whenever(repository)
            .feed(category)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        val argumentCaptor = ArgumentCaptor.forClass(Any::class.java)

        argumentCaptor.run {
            verify(observerState, times(4)).onChanged(capture())

            val (hideTryAgain, showLoading, showTryAgain, hideLoading) = allValues

            Assert.assertEquals(FeedViewModelState.TryAgain(false), hideTryAgain)
            Assert.assertEquals(FeedViewModelState.Loading(true), showLoading)
            Assert.assertEquals(FeedViewModelState.TryAgain(true), showTryAgain)
            Assert.assertEquals(FeedViewModelState.Loading(false), hideLoading)
        }
    }
}