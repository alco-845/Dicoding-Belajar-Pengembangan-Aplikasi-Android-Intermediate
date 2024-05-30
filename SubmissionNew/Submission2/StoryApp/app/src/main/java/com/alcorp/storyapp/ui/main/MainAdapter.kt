package com.alcorp.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.storyapp.R
import com.alcorp.storyapp.data.remote.response.ListStoryResponse
import com.alcorp.storyapp.databinding.ItemStoryBinding
import com.alcorp.storyapp.ui.detail.DetailActivity
import com.alcorp.storyapp.ui.detail.DetailActivity.Companion.STORY_ID
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MainAdapter : PagingDataAdapter<ListStoryResponse, MainAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                Glide.with(itemView.context)
                    .load(this?.photoUrl)
                    .apply(
                        RequestOptions
                        .centerCropTransform()
                        .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.default_image))
                        .error(ContextCompat.getDrawable(itemView.context, R.drawable.default_image))
                    )
                    .into(binding.ivStoryItem)

                binding.tvNameItem.text = this?.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(STORY_ID, this?.id)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(binding.ivStoryItem, "story"),
                            Pair(binding.tvNameItem, "name"),
                        )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    inner class ViewHolder(val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryResponse>() {
            override fun areItemsTheSame(oldItem: ListStoryResponse, newItem: ListStoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryResponse, newItem: ListStoryResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}