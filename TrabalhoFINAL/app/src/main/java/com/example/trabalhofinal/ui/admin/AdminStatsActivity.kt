package com.example.trabalhofinal.ui.admin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.databinding.ActivityAdminStatsBinding
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class AdminStatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminStatsBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.stats.observe(this) { stats ->
            binding.tvUserCount.text = "Usuários Cadastrados: ${stats["users"] ?: 0}"
            binding.tvRentedCount.text = "Livros Alugados: ${stats["rented"] ?: 0}"
            binding.tvAvailableCount.text = "Livros Disponíveis: ${stats["available"] ?: 0}"
        }

        viewModel.loadStats()
    }

    fun onBackClick(view: android.view.View?) {
        finish()
    }
}
