package com.example.trabalhofinal.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.trabalhofinal.databinding.ActivityLoginBinding
import com.example.trabalhofinal.ui.admin.AdminActivity
import com.example.trabalhofinal.ui.user.UserActivity
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carregar imagem de livros
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.ivLogo)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.authStatus.observe(this) { result ->
            when (result) {
                is BookViewModel.AuthResult.LoginSuccess -> {
                    if (result.user.username == "admin") {
                        startActivity(Intent(this, AdminActivity::class.java))
                    } else {
                        val intent = Intent(this, UserActivity::class.java)
                        intent.putExtra("USERNAME", result.user.username)
                        startActivity(intent)
                    }
                    finish()
                }
                is BookViewModel.AuthResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}
