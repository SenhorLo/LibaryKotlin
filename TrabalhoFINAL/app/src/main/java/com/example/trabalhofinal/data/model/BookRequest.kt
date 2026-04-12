package com.example.trabalhofinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_requests")
data class BookRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val description: String?,
    val thumbnail: String?,
    val category: String?,
    val requestedBy: String // Username who requested
)
