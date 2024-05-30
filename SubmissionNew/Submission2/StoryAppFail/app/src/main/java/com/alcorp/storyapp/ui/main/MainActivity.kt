package com.alcorp.storyapp.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.storyapp.R
import com.alcorp.storyapp.databinding.ActivityMainBinding
import com.alcorp.storyapp.helper.LoadingDialog
import com.alcorp.storyapp.helper.PrefData
import com.alcorp.storyapp.helper.PrefData.token
import com.alcorp.storyapp.ui.add.AddStoryActivity
import com.alcorp.storyapp.ui.login.LoginActivity
import com.alcorp.storyapp.viewmodel.MainViewModel
import com.alcorp.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var token: String
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun checkLogin() {
        val token = PrefData.token
        if (token == null && token == "") {
            val i = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun init() {
        binding.refreshMain.setOnRefreshListener(this)
        binding.btnAdd.setOnClickListener(this)
        loadingDialog = LoadingDialog(this)

        pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        token = pref.getString("token", "").toString()

        checkLogin()
        getData()
//        load()
    }

    override fun onRefresh() {
        getData()
        binding.refreshMain.isRefreshing = false
    }

    private fun getData() {
        val mainAdapter = MainAdapter()

        PrefData.token = "Bearer $token"

        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvStory.adapter = mainAdapter

        binding.rvStory.adapter = mainAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                mainAdapter.retry()
            }
        )

        mainViewModel.getListStory().observe(this) {
            mainAdapter.submitData(lifecycle, it)
        }
    }

//    private fun load() {
//        mainViewModel.isLoading.observe(this) {
//            showLoading(it)
//        }
//        mainViewModel.message.observe(this ) {
//            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_settings).setOnMenuItemClickListener(this)
        menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(this)
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.showDialog() else loadingDialog.hideDialog()
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