package com.alcorp.storyapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.storyapp.R
import com.alcorp.storyapp.databinding.ActivityDetailBinding
import com.alcorp.storyapp.helper.LoadingDialog
import com.alcorp.storyapp.viewmodel.DetailViewModel
import com.alcorp.storyapp.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var token: String
    private lateinit var storyId: String
    private lateinit var loadingDialog: LoadingDialog
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        supportActionBar?.title = resources.getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        token = pref.getString("token", "").toString()
        storyId = intent.getStringExtra(STORY_ID).toString()
        loadingDialog = LoadingDialog(this)

        binding.refreshDetail.setOnRefreshListener(this)

        getData()
        load()
    }

    private fun getData() {
        detailViewModel.getDetailStory("Bearer $token", storyId)
        detailViewModel.detailStory.observe(this) {
            Glide.with(this@DetailActivity)
                .load(it.story.photoUrl)
                .placeholder(ContextCompat.getDrawable(this@DetailActivity, R.drawable.default_image))
                .error(ContextCompat.getDrawable(this@DetailActivity, R.drawable.default_image))
                .into(binding.ivImageStory)

            binding.txtName.text = it.story.name
            binding.txtDescription.text = it.story.description
        }
    }

    private fun load() {
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.message.observe(this ) {
            Toast.makeText(this@DetailActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRefresh() {
        getData()
        binding.refreshDetail.isRefreshing = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.showDialog() else loadingDialog.hideDialog()
    }

    companion object {
        const val STORY_ID = "STORY_ID"
    }
}