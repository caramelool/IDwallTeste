package br.com.caramelo.idwallteste.data.di.module

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class PicassoModule {

    @Provides
    fun providePicasso(context: Context): Picasso {
        val loader = OkHttp3Downloader(context, Integer.MAX_VALUE.toLong())
        return Picasso.Builder(context)
            .downloader(loader)
            .build().apply {
                setIndicatorsEnabled(false)
                isLoggingEnabled = false
            }
    }

}