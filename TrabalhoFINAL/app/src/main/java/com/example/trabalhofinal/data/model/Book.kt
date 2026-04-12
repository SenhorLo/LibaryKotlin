package com.example.trabalhofinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val description: String?,
    val thumbnail: String?,
    val category: String?,
    val isRented: Boolean = false,
    val rentedBy: String? = null // Username who rented the book
)
