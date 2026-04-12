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
import com.example.trabalhofinal.data.model.Book
import com.example.trabalhofinal.databinding.FragmentLibraryBinding
import com.example.trabalhofinal.ui.BookAdapter
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookViewModel by viewModels()
    private var username: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString("USERNAME")

        val adapter = BookAdapter(
            onPrimaryClick = { book ->
                if (book.isRented) {
                    if (book.rentedBy == username) {
                        viewModel.returnBook(book)
                    } else {
                        Toast.makeText(context, "Este livro já está alugado por outro usuário", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    username?.let { viewModel.rentBook(book, it) }
                }
            },
            primaryButtonText = "Alugar/Devolver"
        )

        binding.rvLibrary.layoutManager = LinearLayoutManager(context)
        binding.rvLibrary.adapter = adapter

        viewModel.allBooks.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
        }

        binding.searchLocal.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchLocal(it).observe(viewLifecycleOwner) { books ->
                        adapter.submitList(books)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.allBooks.observe(viewLifecycleOwner) { books ->
                        adapter.submitList(books)
                    }
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
