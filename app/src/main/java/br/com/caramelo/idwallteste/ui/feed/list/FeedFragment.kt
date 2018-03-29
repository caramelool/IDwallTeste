package br.com.caramelo.idwallteste.ui.feed.list

import android.app.Activity
import android.arch.lifecycle.Observer
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
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.ui.base.BaseFragment
import br.com.caramelo.idwallteste.ui.feed.detail.EXTRA_URL
import br.com.caramelo.idwallteste.ui.feed.detail.FeedDetailActivity
import kotlinx.android.synthetic.main.fragment_feed.*


const val PARAM_FEED_CATEGORY = "param_category"

class FeedFragment : BaseFragment() {

    companion object {
        fun newInstance(category: DogCategory): FeedFragment {
            val fragment = FeedFragment()
            fragment.arguments = Bundle()
            fragment.arguments?.putString(PARAM_FEED_CATEGORY, category.name)
            return fragment
        }
    }

    private val viewModel by lazy {
        providesOf(FeedViewModel::class, FeedFactory(arguments))
    }

    private val adapter = FeedAdapter { view, url ->

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
//        val transition = fragmentManager?.beginTransaction()
//                ?.addSharedElement(view, view.transitionName)
//
//        FeedDialogFragment.newInstance(url)
//                .show(transition, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spanCount = resources.getInteger(R.integer.span_count)
        val layoutManager = StaggeredGridLayoutManager(spanCount, OrientationHelper.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = layoutManager //GridLayoutManager(context, spanCount)

        recyclerView.setItemViewCacheSize(20)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        recyclerView.adapter = adapter

        viewModel.feedLiveData?.value?.let {
            categoryLabel.text = it.category
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.feedLiveData?.observe(this, feedObserver)
    }

    private val loadingObserver = Observer<Boolean> {
        loading.visibility = if (it == true) View.VISIBLE else View.GONE
    }

    private val feedObserver = Observer<Feed> { feed ->
        categoryLabel.text = feed?.category
        adapter.feed = feed
    }

}
