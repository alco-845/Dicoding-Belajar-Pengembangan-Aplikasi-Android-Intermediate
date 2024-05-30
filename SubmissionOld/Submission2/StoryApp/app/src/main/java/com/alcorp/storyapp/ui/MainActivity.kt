package com.alcorp.storyapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.storyapp.R
import com.alcorp.storyapp.adapter.LoadingStateAdapter
import com.alcorp.storyapp.adapter.StoryAdapter
import com.alcorp.storyapp.databinding.ActivityMainBinding
import com.alcorp.storyapp.helper.PrefData
import com.alcorp.storyapp.ui.add.AddStoryActivity
import com.alcorp.storyapp.ui.login.LoginActivity
import com.alcorp.storyapp.ui.map.MapActivity
import com.alcorp.storyapp.viewmodel.MainViewModel
import com.alcorp.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var token: String
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        checkLogin()
    }

    private fun init() {
        binding.refreshMain.setOnRefreshListener(this)
        pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        token = pref.getString("token", "").toString()

        if (checkNetwork(this)){
            loadData()
        } else {
            Toast.makeText(this@MainActivity, resources.getString(R.string.toast_network), Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        Toast.makeText(this, getString(R.string.toast_wait), Toast.LENGTH_SHORT).show()

        PrefData.token = "Bearer $token"

        val adapter = StoryAdapter()

        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvStory.adapter = adapter

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        mainViewModel.getStory().observe(this) { story ->
            adapter.submitData(lifecycle, story)
        }
    }

    private fun checkLogin() {
        if (token == null && token == "") {
            val i = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_add).setOnMenuItemClickListener(this)
        menu.findItem(R.id.menu_map).setOnMenuItemClickListener(this)
        menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(this)
        return true
    }

    private fun addStory() {
        val i = Intent(this@MainActivity, AddStoryActivity::class.java)
        startActivity(i)
    }

    private fun openMap() {
        val i = Intent(this@MainActivity, MapActivity::class.java)
        startActivity(i)
    }

    private fun logOut() {
        prefEdit = pref.edit()
        prefEdit.clear().apply()

        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                addStory()
                return true
            }
            R.id.menu_map -> {
                openMap()
                return true
            }
            R.id.menu_logout -> {
                logOut()
                return true
            }
        }
        return true
    }

    private fun checkNetwork(context: Context?): Boolean{
        val cm: ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                cm.getNetworkCapabilities(cm.activeNetwork)
            } else {
                cm.activeNetworkInfo
            }
        return network != null
    }

    override fun onRefresh() {
        if (checkNetwork(this)){
            loadData()
        } else {
            Toast.makeText(this@MainActivity, resources.getString(R.string.toast_network), Toast.LENGTH_SHORT).show()
        }
        binding.refreshMain.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}