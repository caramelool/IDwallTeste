package br.com.caramelo.idwallteste.ui.feed.detail

import android.os.Bundle

import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.ext.load
import br.com.caramelo.idwallteste.ui.base.BaseActivity
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import kotlinx.android.synthetic.main.dialog_fragment_feed.*

const val EXTRA_URL = "extra_url"

class FeedDetailActivity : BaseActivity() {

    override val viewModels: Array<BaseViewModel>
        get() = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fragment_feed)

        closeButton.setOnClickListener {
            supportFinishAfterTransition()
        }

        intent?.getStringExtra(EXTRA_URL)?.let { url ->
            dogImage.load(url)
        }

    }

    override fun render(state: State) {
        //Do nothing
    }

}