package br.com.caramelo.idwallteste.data.di.module

import br.com.caramelo.idwallteste.BuildConfig
import br.com.caramelo.idwallteste.data.model.entity.Session
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private typealias BaseUrl = String

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun providesBaseUrl(): BaseUrl {
        return "https://iddog-api.now.sh"
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        urlBase: BaseUrl,
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesOkHTTPClient(): OkHttpClient {
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
        Session.user?.token?.let { token ->
            requestBuilder.addHeader("Authorization", token)
        }
        val request = requestBuilder.build()
        it.proceed(request)
    }

}