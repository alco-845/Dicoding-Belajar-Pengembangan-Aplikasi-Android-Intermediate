package com.alcorp.storyapp.ui.regis

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.storyapp.R
import com.alcorp.storyapp.databinding.ActivityRegisBinding
import com.alcorp.storyapp.helper.LoadingDialog
import com.alcorp.storyapp.helper.checkEmailFormat
import com.alcorp.storyapp.viewmodel.AuthViewModel
import com.alcorp.storyapp.viewmodel.ViewModelFactory

class RegisActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisBinding
    private lateinit var loadingDialog: LoadingDialog
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisBinding.inflate(layoutInflater)
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

    private fun init() {
        setupView()
        loadingDialog = LoadingDialog(this)
        binding.tvLogin.setOnClickListener(this)
        binding.btnRegis.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view) {
            binding.tvLogin -> {
                finish()
            }

            binding.btnRegis -> {
                val username = binding.edtUsername.text.toString().trim()
                val email = binding.edtEmailRegis.text.toString().trim()
                val password = binding.edtPassRegis.text.toString().trim()

                if (username.isNotEmpty()) {
                    if (checkEmailFormat(email)) {
                        if (password.length >= 8) {
                            authViewModel.signUpUser(username, email, password)
                            authViewModel.regisUser.observe(this) { finish() }
                            authViewModel.isLoading.observe(this) {
                                showLoading(it)
                            }

                            authViewModel.message.observe(this ) {
                                Toast.makeText(this@RegisActivity, it, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.password_min_length), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.email_validate), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.username_validate), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.showDialog() else loadingDialog.hideDialog()
    }
}