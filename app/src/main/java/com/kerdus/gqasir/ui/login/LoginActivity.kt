package com.kerdus.gqasir.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kerdus.gqasir.Main2Activity
import com.kerdus.gqasir.R
import com.kerdus.gqasir.databinding.ActivityLoginBinding
import com.kerdus.gqasir.ui.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        observeViewModel()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Log.d("LoginActivity", "Coba login dengan username: $username")
                viewModel.login(username, password)
            } else {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                Log.e("LoginActivity", "Login gagal: Field kosong")
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.loginResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Log.d("LoginActivity", "Login berhasil: $message")
                AlertDialog.Builder(this).apply {
                    setTitle("Login Berhasil")
                    setMessage(message)
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@LoginActivity, Main2Activity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }

        viewModel.errorMessage.observe(this) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                Log.e("LoginActivity", "Login gagal: $message")
            }
        }
    }
}
