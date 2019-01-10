package br.com.caramelo.idwallteste.data.di.module

import br.com.caramelo.idwallteste.data.repository.AuthRepository
import br.com.caramelo.idwallteste.data.repository.FeedRepository
import br.com.caramelo.idwallteste.data.repository.IDdogAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.reflect.KClass

private fun <T: Any> Retrofit.create(
    kClass: KClass<T>
) = create(kClass.java)

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesIDdogApi(retrofit: Retrofit): IDdogAPI {
        return retrofit.create(IDdogAPI::class)
    }

    @Singleton
    @Provides
    fun providesAuthRepository(api: IDdogAPI): AuthRepository {
        return AuthRepository(api)
    }

    @Singleton
    @Provides
    fun providesFeedRepository(api: IDdogAPI): FeedRepository {
        return FeedRepository(api)
    }

}