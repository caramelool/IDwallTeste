package br.com.caramelo.idwallteste.ui.feed.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.data.model.entity.Feed
import br.com.caramelo.idwallteste.ext.load

class FeedAdapter(
        private val onItemClickListener: (view: View, url: String) -> Unit
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    var feed: Feed? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int = feed?.list?.size ?: 0

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class FeedViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            val url = feed?.list?.get(position) ?: return

            imageView.load(url)

            itemView.setOnClickListener {
                onItemClickListener(imageView, url)
            }
        }

    }
}