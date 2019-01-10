package br.com.caramelo.idwallteste.ui.feed.category

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import br.com.caramelo.idwallteste.ui.feed.list.FeedFragment

class CategoryAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    var list: List<DogCategory> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        val category = list[position]
        return FeedFragment.newInstance(
            category
        )
    }

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name
}