package com.example.trabalhofinal.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.databinding.ActivityRegisterBinding
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterConfirm.setOnClickListener {
            val username = binding.etRegUsername.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(username, password)
        }

        binding.tvBackToLogin.setOnClickListener {
            finish()
        }

        viewModel.authStatus.observe(this) { result ->
            when (result) {
                is BookViewModel.AuthResult.Success -> {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
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
