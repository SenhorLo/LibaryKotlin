package com.example.trabalhofinal.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trabalhofinal.databinding.ActivityAdminRequestsBinding
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class AdminRequestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminRequestsBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar manual se necessário, ou usar a que já existe no layout?
        // O layout atual não tem Toolbar. Vamos adicionar uma se quisermos suporte a NoActionBar.

        val adapter = RequestAdapter(
            onApprove = { request ->
                viewModel.approveRequest(request)
                Toast.makeText(this, "Livro adicionado à biblioteca!", Toast.LENGTH_SHORT).show()
            },
            onReject = { request ->
                viewModel.rejectRequest(request)
                Toast.makeText(this, "Solicitação rejeitada", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvRequests.layoutManager = LinearLayoutManager(this)
        binding.rvRequests.adapter = adapter

        viewModel.allRequests.observe(this) { requests ->
            adapter.submitList(requests)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
