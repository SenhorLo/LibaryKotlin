package com.example.trabalhofinal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trabalhofinal.R
import com.example.trabalhofinal.data.model.Book
import com.example.trabalhofinal.databinding.ItemBookBinding

class BookAdapter(
    private val onPrimaryClick: (Book) -> Unit,
    private val onSecondaryClick: ((Book) -> Unit)? = null,
    private val primaryButtonText: String,
    private val secondaryButtonText: String? = null
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.tvTitle.text = book.title
            binding.tvAuthor.text = book.author
            binding.tvCategory.text = book.category
            binding.tvStatus.text = if (book.isRented) "Alugado por: ${book.rentedBy}" else "Disponível"
            binding.tvStatus.setTextColor(if (book.isRented) 0xFFFF0000.toInt() else 0xFF008000.toInt())
            
            Glide.with(binding.ivThumbnail)
                .load(book.thumbnail)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivThumbnail)

            binding.btnPrimary.text = primaryButtonText
            binding.btnPrimary.setOnClickListener { onPrimaryClick(book) }

            if (secondaryButtonText != null && onSecondaryClick != null) {
                binding.btnSecondary.text = secondaryButtonText
                binding.btnSecondary.visibility = View.VISIBLE
                binding.btnSecondary.setOnClickListener { onSecondaryClick.invoke(book) }
            } else {
                binding.btnSecondary.visibility = View.GONE
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
    }
}
