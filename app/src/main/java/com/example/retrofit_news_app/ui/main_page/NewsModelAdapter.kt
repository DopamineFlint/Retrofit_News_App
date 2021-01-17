package com.example.retrofit_news_app.ui.main_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.retrofit_news_app.R
import com.example.retrofit_news_app.data.ArticlesModel
import com.example.retrofit_news_app.databinding.NewsPostItemBinding

class NewsModelAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<ArticlesModel, NewsModelAdapter.ArticlesViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val binding =
            NewsPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticlesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class ArticlesViewHolder(private val binding: NewsPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(article: ArticlesModel) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_launcher_foreground)
                    .into(newsPostImage)

                newsAuthor.text = article.author
                newsTitle.text = article.title
                newsDescription.text = article.description
                newsOriginalUrl.text = article.url
                newsPublishTime.text = article.publishedAt
                newsMainContent.text = article.content
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: ArticlesModel)
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<ArticlesModel>() {
            override fun areItemsTheSame(oldItem: ArticlesModel, newItem: ArticlesModel) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: ArticlesModel, newItem: ArticlesModel) =
                oldItem == newItem
        }
    }
}