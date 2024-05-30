package com.alcorp.storyapp.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.storyapp.R
import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.databinding.ActivityRegisterBinding
import com.alcorp.storyapp.model.FileResponse
import com.alcorp.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegis.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_wait), Toast.LENGTH_SHORT).show()

            val txtUsername = binding.edtUsername.text.toString().trim()
            val txtEmail = binding.edtEmailRegis.text.toString().trim()
            val txtPass = binding.edtPassRegis.text.toString().trim()

            val service = ApiConfig.getApiService().regisUser(txtUsername, txtEmail, txtPass)
            service.enqueue(object : Callback<FileResponse> {
                override fun onResponse(call: Call<FileResponse>, response: Response<FileResponse>) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<FileResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}