package com.example.trabalhofinal.data.repository

import com.example.trabalhofinal.data.local.BookDao
import com.example.trabalhofinal.data.local.UserDao
import com.example.trabalhofinal.data.local.BookRequestDao
import com.example.trabalhofinal.data.model.Book
import com.example.trabalhofinal.data.model.User
import com.example.trabalhofinal.data.model.BookRequest
import com.example.trabalhofinal.data.remote.GoogleBooksService
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookRepository(
    private val bookDao: BookDao,
    private val userDao: UserDao,
    private val requestDao: BookRequestDao
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val googleBooksService = retrofit.create(GoogleBooksService::class.java)

    // User Operations
    suspend fun registerUser(user: User): Boolean {
        return try {
            userDao.registerUser(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun login(username: String, password: String) = userDao.login(username, password)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
    suspend fun getUserCount() = userDao.getUserCount()

    // Book Requests
    val allRequests: Flow<List<BookRequest>> = requestDao.getAllRequests()
    suspend fun insertRequest(request: BookRequest) = requestDao.insertRequest(request)
    suspend fun deleteRequest(request: BookRequest) = requestDao.deleteRequest(request)

    // Local Data
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    suspend fun insert(book: Book) = bookDao.insertBook(book)
    suspend fun update(book: Book) = bookDao.updateBook(book)
    suspend fun delete(book: Book) = bookDao.deleteBook(book)
    fun searchLocal(query: String) = bookDao.searchBooks("%$query%")
    suspend fun getRentedCount() = bookDao.getRentedCount()
    suspend fun getAvailableCount() = bookDao.getAvailableCount()

    // Remote Data
    suspend fun searchRemote(query: String): List<Book> {
        return try {
            val response = googleBooksService.searchBooks(query)
            response.items?.map { item ->
                Book(
                    title = item.volumeInfo.title,
                    author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                    description = item.volumeInfo.description,
                    thumbnail = item.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:"),
                    category = item.volumeInfo.categories?.firstOrNull() ?: "General"
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
