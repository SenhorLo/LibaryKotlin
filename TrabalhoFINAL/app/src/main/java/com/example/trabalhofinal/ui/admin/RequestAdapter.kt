package com.example.trabalhofinal.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trabalhofinal.R
import com.example.trabalhofinal.data.model.BookRequest
import com.example.trabalhofinal.databinding.ItemBookBinding

class RequestAdapter(
    private val onApprove: (BookRequest) -> Unit,
    private val onReject: (BookRequest) -> Unit
) : ListAdapter<BookRequest, RequestAdapter.RequestViewHolder>(RequestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RequestViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: BookRequest) {
            binding.tvTitle.text = request.title
            binding.tvAuthor.text = "Solicitado por: ${request.requestedBy}"
            binding.tvCategory.text = request.category ?: "Sem categoria"
            
            Glide.with(binding.root.context)
                .load(request.thumbnail)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivThumbnail)

            binding.btnPrimary.text = "Aprovar"
            binding.btnSecondary.text = "Rejeitar"

            binding.btnPrimary.setOnClickListener { onApprove(request) }
            binding.btnSecondary.setOnClickListener { onReject(request) }
        }
    }

    class RequestDiffCallback : DiffUtil.ItemCallback<BookRequest>() {
        override fun areItemsTheSame(oldItem: BookRequest, newItem: BookRequest) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BookRequest, newItem: BookRequest) = oldItem == newItem
    }
}
