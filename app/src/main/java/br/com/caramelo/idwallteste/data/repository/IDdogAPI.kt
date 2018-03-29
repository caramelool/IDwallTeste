package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.data.model.request.AuthRequest
import br.com.caramelo.idwallteste.data.model.response.AuthResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*

interface IDdogAPI {

    @Headers("Content-Type: application/json")
    @POST("signup")
    fun signup(@Body body: AuthRequest): Deferred<AuthResponse>

    @Headers("Content-Type: application/json")
    @GET("feed")
    fun feed(@Query("category") category: String = DogCategory.HUSKY.name): Deferred<Feed>
}