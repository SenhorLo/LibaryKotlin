package com.example.trabalhofinal.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.trabalhofinal.databinding.ActivityAdminBinding
import com.example.trabalhofinal.ui.BookAdapter
import com.example.trabalhofinal.ui.viewmodel.BookViewModel

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbarAdmin)

        // Load Images for Cards
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=400&q=80")
            .into(binding.ivRequestIcon)

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=400&q=80")
            .into(binding.ivStatsIcon)

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=400&q=80")
            .into(binding.ivApiIcon)

        // Card Click Listeners
        binding.cardRequests.setOnClickListener {
            startActivity(Intent(this, AdminRequestsActivity::class.java))
        }

        binding.cardStats.setOnClickListener {
            startActivity(Intent(this, AdminStatsActivity::class.java))
        }

        binding.cardApiSearch.setOnClickListener {
            val intent = Intent(this, com.example.trabalhofinal.ui.user.UserActivity::class.java)
            intent.putExtra("IS_ADMIN", true)
            startActivity(intent)
        }

        // Setup RecyclerView for existing books
        val adapter = BookAdapter(
            onPrimaryClick = { book ->
                val intent = Intent(this, AddEditBookActivity::class.java)
                intent.putExtra("BOOK_ID", book.id)
                intent.putExtra("TITLE", book.title)
                intent.putExtra("AUTHOR", book.author)
                intent.putExtra("CATEGORY", book.category)
                intent.putExtra("DESCRIPTION", book.description)
                startActivity(intent)
            },
            onSecondaryClick = { book ->
                viewModel.delete(book)
            },
            primaryButtonText = "Editar",
            secondaryButtonText = "Excluir"
        )

        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter

        viewModel.allBooks.observe(this) { books ->
            adapter.submitList(books)
        }

        binding.fabAddBook.setOnClickListener {
            startActivity(Intent(this, AddEditBookActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(com.example.trabalhofinal.R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            com.example.trabalhofinal.R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val intent = Intent(this, com.example.trabalhofinal.ui.login.LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
