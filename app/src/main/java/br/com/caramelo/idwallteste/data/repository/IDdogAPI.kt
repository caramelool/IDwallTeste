package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.data.model.request.AuthRequest
import br.com.caramelo.idwallteste.data.model.response.AuthResponse
import retrofit2.Call
import retrofit2.http.*

interface IDdogAPI {

    @Headers("Content-Type: application/json")
    @POST("signup")
    fun signup(@Body body: AuthRequest): Call<AuthResponse>

    @Headers("Content-Type: application/json")
    @GET("feed")
    fun feed(@Query("category") category: String = DogCategory.HUSKY.name): Call<Feed>
}