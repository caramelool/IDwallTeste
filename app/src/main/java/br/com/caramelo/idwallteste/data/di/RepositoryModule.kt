package br.com.caramelo.idwallteste.data.di

import br.com.caramelo.idwallteste.data.repository.AuthRepository
import br.com.caramelo.idwallteste.data.repository.FeedRepository
import br.com.caramelo.idwallteste.data.repository.IDdogAPI
import com.github.salomonbrys.kodein.*
import retrofit2.Retrofit

val repositoryModule = Kodein.Module {

    bind<IDdogAPI>() with provider { instance<Retrofit>().create(IDdogAPI::class.java) }

    bind<AuthRepository>() with singleton { AuthRepository(instance()) }

    bind<FeedRepository>() with singleton { FeedRepository(instance()) }
}