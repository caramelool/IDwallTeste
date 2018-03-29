package br.com.caramelo.idwallteste.data.di

import br.com.caramelo.idwallteste.BuildConfig
import br.com.caramelo.idwallteste.data.model.entity.Session
import com.github.salomonbrys.kodein.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = Kodein.Module {

    constant("baseUrl") with "https://iddog-api.now.sh"

    bind<OkHttpClient>() with singleton { bindOkHTTPClient() }

    bind<Retrofit>() with singleton { bindRetrofit(instance("baseUrl"), instance()) }
}

private fun bindRetrofit(urlBase: String,
                         httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(httpClient)
            .build()
}

private fun bindOkHTTPClient(): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

    clientBuilder.addInterceptor(interceptorToken)

    val level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
    else HttpLoggingInterceptor.Level.NONE
    val logging = HttpLoggingInterceptor()
    logging.level = level
    clientBuilder.addInterceptor(logging)

    return clientBuilder.build()
}

private val interceptorToken: (Interceptor.Chain) -> Response = {
    val original = it.request()
    val requestBuilder = original.newBuilder()
    Session.user?.token?.let {  token ->
        requestBuilder.addHeader("Authorization", token)
    }
    val request = requestBuilder.build()
    it.proceed(request)
}