package com.example.trabalhofinal.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trabalhofinal.databinding.FragmentSearchBinding
import com.example.trabalhofinal.ui.BookAdapter
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isAdmin = activity?.intent?.getBooleanExtra("IS_ADMIN", false) ?: false
        val username = activity?.intent?.getStringExtra("USERNAME") ?: "Desconhecido"

        val adapter = BookAdapter(
            onPrimaryClick = { book ->
                if (isAdmin) {
                    viewModel.insert(book)
                    Toast.makeText(context, "${book.title} adicionado à biblioteca!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.requestBook(book, username)
                    Toast.makeText(context, "Solicitação enviada ao Admin!", Toast.LENGTH_SHORT).show()
                }
            },
            primaryButtonText = if (isAdmin) "Adicionar" else "Solicitar"
        )

        binding.rvSearch.layoutManager = LinearLayoutManager(context)
        binding.rvSearch.adapter = adapter

        // Observar resultados da busca
        viewModel.remoteBooks.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
            binding.tvEmpty.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
        }

        // Observar estado de carregamento
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) binding.tvEmpty.visibility = View.GONE
        }

        binding.searchRemote.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        viewModel.searchRemote(it)
                        binding.searchRemote.clearFocus() // Fecha o teclado
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
