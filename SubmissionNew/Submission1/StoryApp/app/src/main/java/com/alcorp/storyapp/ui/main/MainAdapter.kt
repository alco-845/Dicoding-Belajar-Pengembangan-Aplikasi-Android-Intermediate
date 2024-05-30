package com.alcorp.storyapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.storyapp.R
import com.alcorp.storyapp.data.remote.response.ListStoryResponse
import com.alcorp.storyapp.databinding.ItemStoryBinding
import com.alcorp.storyapp.ui.detail.DetailActivity
import com.alcorp.storyapp.ui.detail.DetailActivity.Companion.STORY_ID
import com.bumptech.glide.Glide

class MainAdapter(private val context: Context, private val listStory: List<ListStoryResponse>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listStory[position]) {
                Glide.with(itemView.context)
                    .load(photoUrl)
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.default_image))
                    .error(ContextCompat.getDrawable(context, R.drawable.default_image))
                    .into(binding.ivStoryItem)

                binding.tvNameItem.text = name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(STORY_ID, id)

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

    override fun getItemCount(): Int = listStory.size

    inner class ViewHolder(val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root)
}