package br.com.caramelo.idwallteste

import android.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

@InternalCoroutinesApi
val testDispatcher = TestDispatcher()

@InternalCoroutinesApi
class TestDispatcher : CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        block.run()
    }

    override fun scheduleResumeAfterDelay(
        timeMillis: Long,
        continuation: CancellableContinuation<Unit>
    ) {
        continuation.resume(Unit)
    }
}
