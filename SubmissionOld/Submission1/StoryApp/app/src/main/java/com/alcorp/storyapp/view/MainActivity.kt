package com.alcorp.storyapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.storyapp.R
import com.alcorp.storyapp.adapter.StoryAdapter
import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.databinding.ActivityMainBinding
import com.alcorp.storyapp.model.ListStory
import com.alcorp.storyapp.view.add.AddStoryActivity
import com.alcorp.storyapp.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var token: String

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

        val service = ApiConfig().getRetrofit().getListStory("Bearer $token")
        service.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                val responseBody = response.body()
                if (responseBody != null && !responseBody.error) {
                    storyAdapter = StoryAdapter(responseBody.listStory)
                    storyAdapter.notifyDataSetChanged()

                    binding.rvStory.setHasFixedSize(true)
                    binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvStory.adapter = storyAdapter

                    Toast.makeText(this@MainActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
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
        menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(this)
        return true
    }

    private fun addStory() {
        val i = Intent(this@MainActivity, AddStoryActivity::class.java)
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