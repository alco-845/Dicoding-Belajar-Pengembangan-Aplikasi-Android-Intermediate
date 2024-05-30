package com.alcorp.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.storyapp.R
import com.alcorp.storyapp.model.ListStoryResponse
import com.alcorp.storyapp.view.detail.DetailStoryActivity
import com.alcorp.storyapp.view.detail.DetailStoryActivity.Companion.STORY_ID
import com.bumptech.glide.Glide

class StoryAdapter(private val listStory: List<ListStoryResponse>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.ivStoryItem)
        private var tvName: TextView = itemView.findViewById(R.id.tvNameItem)

        fun bind(story: ListStoryResponse) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imgPhoto)

            tvName.text = story.name

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(STORY_ID, story.id);

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "story"),
                        Pair(tvName, "name"),
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}