package br.com.caramelo.idwallteste.data.di

import br.com.caramelo.idwallteste.IDwallApplication
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

val picassoModule = Kodein.Module {
    bind<OkHttp3Downloader>() with singleton {
        OkHttp3Downloader(instance<IDwallApplication>(), Integer.MAX_VALUE.toLong()) }

    bind<Picasso>() with singleton {
        Picasso.Builder(instance<IDwallApplication>())
                .downloader(instance<OkHttp3Downloader>()).build()
    }
}