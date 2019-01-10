package br.com.caramelo.idwallteste.ui.feed.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.ext.load
import kotlinx.android.synthetic.main.adapter_feed.view.*

class FeedAdapter(
    private val onItemClickListener: (view: View, url: String) -> Unit
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    var feed: Feed? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val list: List<String>
        get() = feed?.list ?: listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            with(itemView) {
                val url = list[position]

                imageView.load(url)

                setOnClickListener {
                    onItemClickListener(imageView, url)
                }
            }

        }

    }
}