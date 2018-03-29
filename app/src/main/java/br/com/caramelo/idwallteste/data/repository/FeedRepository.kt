package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import kotlinx.coroutines.experimental.async

open class FeedRepository(
        private val api: IDdogAPI
) {
    open fun feed(category: DogCategory): RepositoryLiveData<Feed> {
        val liveData = RepositoryLiveData<Feed>()
        async {
            try {
                val name = category.name.toLowerCase()
                val response = api.feed(name).await()
                liveData.postValue(response)
            } catch (t: Throwable) {
                liveData.postThowable(t)
            }
        }
        return liveData
    }
}