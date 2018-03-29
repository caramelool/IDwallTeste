package br.com.caramelo.idwallteste.ui.feed.list

import android.arch.lifecycle.Observer
import br.com.caramelo.idwallteste.BaseTest
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.data.repository.FeedRepository
import com.github.salomonbrys.kodein.instance
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*

class FeedViewModelTest: BaseTest() {

    private lateinit var repository: FeedRepository
    private lateinit var viewModel: FeedViewModel
    private var category = DogCategory.HUSKY

    @Before
    override fun `before each test`() {
        super.`before each test`()
        repository = spy(kodein.instance<FeedRepository>())
        viewModel = spy(FeedViewModel(category, repository))
    }

    @Test
    fun `should request all dogs images correspond the category`() {
        val loadingObserver: Observer<Boolean> = mock()
        val feedObserver: Observer<Feed> = mock()

        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(FEED_RESPONSE_200))

        viewModel.loadingLiveData.observeForever(loadingObserver)
        viewModel.feedLiveData?.observeForever(feedObserver)

        verify(viewModel).requestFeed()
        verify(loadingObserver).onChanged(true)
        verify(repository).feed(category)

        await()

        verify(feedObserver).onChanged(any())
        verify(loadingObserver).onChanged(false)
    }

    private val FEED_RESPONSE_200 = "{\n" +
            "    \"category\": \"${category.name}\",\n" +
            "    \"list\": [\n" +
            "        \"https://dog.ceo/api/img/${category.name}/n02110958_10.jpg\",\n" +
            "        \"https://dog.ceo/api/img/${category.name}/n02110958_10186.jpg\",\n" +
            "        \"https://dog.ceo/api/img/${category.name}/n02110958_10193.jpg\",\n" +
            "        \"https://dog.ceo/api/img/${category.name}/n02110958_10378.jpg\",\n" +
            "        \"https://dog.ceo/api/img/${category.name}/n02110958_10842.jpg\"\n" +
            "    ]\n" +
            "}"
}