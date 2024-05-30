package com.alcorp.storyapp.view.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.storyapp.R
import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.databinding.ActivityDetailStoryBinding
import com.alcorp.storyapp.model.DetailStory
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var storyId: String

    companion object {
        const val STORY_ID = "STORY_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        supportActionBar?.title = resources.getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        val token = pref.getString("token", "").toString()
        storyId = intent.getStringExtra(STORY_ID).toString()

        val service = ApiConfig().getRetrofit().getDetailStory("Bearer $token", storyId)
        service.enqueue(object : Callback<DetailStory> {
            override fun onResponse(call: Call<DetailStory>, response: Response<DetailStory>) {
                val responseBody = response.body()
                if (responseBody != null && !responseBody.error) {
                    Glide.with(this@DetailStoryActivity)
                        .load(responseBody.story.photoUrl)
                        .into(binding.ivImageStory)

                    binding.txtName.text = responseBody.story.name
                    binding.txtDescription.text = responseBody.story.description

                    Toast.makeText(this@DetailStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailStory>, t: Throwable) {
                Toast.makeText(this@DetailStoryActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}