package com.example.trabalhofinal.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.data.model.Book
import com.example.trabalhofinal.databinding.ActivityAddEditBookBinding
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class AddEditBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBookBinding
    private val viewModel: BookViewModel by viewModels()
    private var bookId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getIntExtra("BOOK_ID", 0)
        if (bookId != 0) {
            binding.etTitle.setText(intent.getStringExtra("TITLE"))
            binding.etAuthor.setText(intent.getStringExtra("AUTHOR"))
            binding.etCategory.setText(intent.getStringExtra("CATEGORY"))
            binding.etDescription.setText(intent.getStringExtra("DESCRIPTION"))
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val author = binding.etAuthor.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Título e Autor são obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (bookId == 0) {
                // Inserir novo (id 0 faz o Room auto-gerar)
                val newBook = Book(
                    title = title,
                    author = author,
                    category = category,
                    description = description,
                    thumbnail = null
                )
                viewModel.insert(newBook)
                Toast.makeText(this, "Livro cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                // Atualizar existente
                val updatedBook = Book(
                    id = bookId,
                    title = title,
                    author = author,
                    category = category,
                    description = description,
                    thumbnail = null
                )
                viewModel.update(updatedBook)
                Toast.makeText(this, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
