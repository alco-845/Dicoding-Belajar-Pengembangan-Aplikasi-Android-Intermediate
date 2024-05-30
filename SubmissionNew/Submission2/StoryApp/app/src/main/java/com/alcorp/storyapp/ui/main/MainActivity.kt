package com.alcorp.storyapp.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.storyapp.R
import com.alcorp.storyapp.databinding.ActivityMainBinding
import com.alcorp.storyapp.helper.LoadingDialog
import com.alcorp.storyapp.helper.PrefData
import com.alcorp.storyapp.ui.add.AddStoryActivity
import com.alcorp.storyapp.ui.login.LoginActivity
import com.alcorp.storyapp.ui.maps.MapsActivity
import com.alcorp.storyapp.viewmodel.MainViewModel
import com.alcorp.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var token: String
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun checkLogin() {
        if (token == null && token == "") {
            val i = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun init() {
        pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        token = pref.getString("token", "").toString()

        binding.refreshMain.setOnRefreshListener(this)
        binding.btnAdd.setOnClickListener(this)
        loadingDialog = LoadingDialog(this)
        checkLogin()
        getData()
    }

    override fun onRefresh() {
        getData()
        binding.refreshMain.isRefreshing = false
    }

    private fun getData() {
        PrefData.token = "Bearer $token"

        val adapter = MainAdapter()

        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = GridLayoutManager(this@MainActivity, 2)
        binding.rvStory.adapter = adapter

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        mainViewModel.getListStory().observe(this) { story ->
            adapter.submitData(lifecycle, story)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_maps).setOnMenuItemClickListener(this)
        menu.findItem(R.id.menu_settings).setOnMenuItemClickListener(this)
        menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(this)
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_maps -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                return true
            }
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            R.id.menu_logout -> {
                prefEdit = pref.edit()
                prefEdit.clear().apply()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return true
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnAdd -> {
                startActivityForResult(Intent(this@MainActivity, AddStoryActivity::class.java), 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1){
            getData()
        }
    }
}