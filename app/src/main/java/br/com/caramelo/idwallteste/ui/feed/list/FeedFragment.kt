package br.com.caramelo.idwallteste.ui.feed.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.ui.base.BaseFragment
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import br.com.caramelo.idwallteste.ui.feed.detail.EXTRA_URL
import br.com.caramelo.idwallteste.ui.feed.detail.FeedDetailActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_feed.*
import javax.inject.Inject

const val PARAM_FEED_CATEGORY = "param_category"

class FeedFragment : BaseFragment() {

    companion object {
        fun newInstance(category: DogCategory): FeedFragment {
            return FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_FEED_CATEGORY, category.name)
                }
            }
        }
    }

    /**
     * ViewModel initialize
     */

    @Inject
    lateinit var factory: FeedViewModel.Factory

    private val viewModel by lazy { viewModel<FeedViewModel>(factory).value }

    override val viewModels: Array<BaseViewModel>
        get() = arrayOf(viewModel)

    private val feedAdapter = FeedAdapter { view, url ->

        val intent = Intent(context, FeedDetailActivity::class.java)
        intent.putExtra(EXTRA_URL, url)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity as Activity,
                            Pair.create(view, view.transitionName))
            context?.startActivity(intent, options.toBundle())
        } else {
            context?.startActivity(intent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)

        val spanCount = resources.getInteger(R.integer.span_count)
        val gridLayoutManager = StaggeredGridLayoutManager(spanCount, OrientationHelper.VERTICAL)
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        recyclerView.apply {
            layoutManager = gridLayoutManager //GridLayoutManager(context, spanCount)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            adapter = feedAdapter
        }
    }

    override fun render(state: State) {
        when (state) {
            is FeedViewModelState.Loading -> {
                renderLoading(state)
            }
            is FeedViewModelState.TryAgain -> {
                renderTryAgain()
            }
            is FeedViewModelState.FeedList -> {
                renderFeed(state)
            }
        }
    }

    private fun renderLoading(state: FeedViewModelState.Loading) {
        loading.visibility = if (state.visible) View.VISIBLE else View.GONE
    }

    private fun renderFeed(state: FeedViewModelState.FeedList) {
        with(state) {
            categoryLabel.text = feed?.category
            feedAdapter.feed = feed
        }
    }

    private fun renderTryAgain() {
        tryAgainView.visibility = View.VISIBLE
    }

}
