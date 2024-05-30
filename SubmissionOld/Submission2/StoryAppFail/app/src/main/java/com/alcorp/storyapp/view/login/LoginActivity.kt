package com.alcorp.storyapp.view.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.storyapp.R
import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.databinding.ActivityLoginBinding
import com.alcorp.storyapp.model.User
import com.alcorp.storyapp.view.MainActivity
import com.alcorp.storyapp.view.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLogin()
        setupView()
        setupAction()
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

    private fun setupAction() {
        binding.tvRegis.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_wait), Toast.LENGTH_SHORT).show()

            val txtEmail = binding.edtEmailLogin.text.toString().trim()
            val txtPass = binding.edtPassLogin.text.toString().trim()

            val service = ApiConfig.getRetrofit().loginUser(txtEmail, txtPass)
            service.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val name = responseBody.loginResult.name
                        val token = responseBody.loginResult.token

                        prefEdit = pref.edit()
                        prefEdit.putString("name", name)
                        prefEdit.putString("token", token)
                        prefEdit.apply()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
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
}