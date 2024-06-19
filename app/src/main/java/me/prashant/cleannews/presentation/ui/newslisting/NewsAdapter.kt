package me.prashant.cleannews.presentation.ui.newslisting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import me.prashant.cleannews.data.di.DataModule
import me.prashant.cleannews.databinding.ItemNewsBinding
import me.prashant.cleannews.presentation.model.ArticleUiModel
import javax.inject.Inject

class NewsAdapter
    @Inject
    constructor() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
        private val newsList = mutableListOf<ArticleUiModel>()

        fun submitList(newsList: List<ArticleUiModel>) {
            this.newsList.clear()
            this.newsList.addAll(newsList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): NewsViewHolder {
            val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NewsViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: NewsViewHolder,
            position: Int,
        ) {
            holder.bind(newsList[position])
        }

        override fun getItemCount(): Int = newsList.size

        inner class NewsViewHolder(
            private val binding: ItemNewsBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(article: ArticleUiModel) {
                binding.apply {
                    title.text = article.title
                    author.text = article.author ?: "Unknown Author"
                    time.text = article.publishedAt
                    Glide
                        .with(binding.root.context)
                        .setDefaultRequestOptions(RequestOptions().transform(RoundedCorners(12)))
                        .load(article.imageUrl)
                        .into(newsImage)
                }
            }
        }
    }
