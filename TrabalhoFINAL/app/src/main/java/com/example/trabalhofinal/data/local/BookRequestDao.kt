package com.example.trabalhofinal.data.local

import androidx.room.*
import com.example.trabalhofinal.data.model.BookRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface BookRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: BookRequest)

    @Delete
    suspend fun deleteRequest(request: BookRequest)

    @Query("SELECT * FROM book_requests")
    fun getAllRequests(): Flow<List<BookRequest>>

    @Query("SELECT COUNT(*) FROM book_requests")
    suspend fun getRequestCount(): Int
}
