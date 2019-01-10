package br.com.caramelo.idwallteste.ext

import android.widget.ImageView
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.ui.custom.RoundedCornersTransformation
import com.squareup.picasso.Picasso

fun ImageView.load(url: String) {
    Picasso.get().load(url)
            .transform(RoundedCornersTransformation(15, 5))
            .placeholder(R.drawable.feed_placeholder)
            .error(R.drawable.feed_placeholder)
            .into(this)
}