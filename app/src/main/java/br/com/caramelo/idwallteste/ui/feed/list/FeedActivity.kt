package br.com.caramelo.idwallteste.ui.feed.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        viewPager.adapter = Adapter(supportFragmentManager)
    }

    private class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val names = DogCategory.values().map { it.name }
        private val list = mutableListOf<FeedFragment>()

        init {
            DogCategory.values().forEach {
                list.add(FeedFragment.newInstance(it))
            }
        }

        override fun getItem(position: Int): Fragment = list[position]

        override fun getCount(): Int = list.size

        override fun getPageTitle(position: Int): CharSequence? = names[position]
    }
}
