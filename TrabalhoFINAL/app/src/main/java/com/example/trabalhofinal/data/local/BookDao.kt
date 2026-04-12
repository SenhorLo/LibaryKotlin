package com.example.trabalhofinal.data.local

import androidx.room.*
import com.example.trabalhofinal.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books WHERE title LIKE :searchQuery OR category LIKE :searchQuery")
    fun searchBooks(searchQuery: String): Flow<List<Book>>
    @Query("SELECT COUNT(*) FROM books WHERE isRented = 1")
    suspend fun getRentedCount(): Int

    @Query("SELECT COUNT(*) FROM books WHERE isRented = 0")
    suspend fun getAvailableCount(): Int
}
