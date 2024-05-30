package com.alcorp.storyapp.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.storyapp.databinding.ActivityLoginBinding
import com.alcorp.storyapp.helper.LoadingDialog
import com.alcorp.storyapp.ui.main.MainActivity
import com.alcorp.storyapp.ui.regis.RegisActivity
import com.alcorp.storyapp.viewmodel.AuthViewModel
import com.alcorp.storyapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun checkLogin() {
        pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        val token = pref.getString("token", "")
        if (token != null && token != "") {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun init() {
        checkLogin()
        setupView()
        loadingDialog = LoadingDialog(this)
        binding.tvRegis.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view) {
            binding.tvRegis -> {
                val intent = Intent(this, RegisActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogin -> {
                val email = binding.edtEmailLogin.text.toString().trim()
                val password = binding.edtPassLogin.text.toString().trim()

                authViewModel.signInUser(email, password)
                authViewModel.loginUser.observe(this) {
                    val name = it.loginResult.name
                    val token = it.loginResult.token

                    prefEdit = pref.edit()
                    prefEdit.putString("name", name)
                    prefEdit.putString("token", token)
                    prefEdit.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                authViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                authViewModel.message.observe(this ) {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.showDialog() else loadingDialog.hideDialog()
    }
}