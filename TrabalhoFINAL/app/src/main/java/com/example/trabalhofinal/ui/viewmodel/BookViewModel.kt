package com.example.trabalhofinal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.trabalhofinal.data.local.AppDatabase
import com.example.trabalhofinal.data.model.Book
import com.example.trabalhofinal.data.model.User
import com.example.trabalhofinal.data.model.BookRequest
import com.example.trabalhofinal.data.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository
    val allBooks: LiveData<List<Book>>
    val allRequests: LiveData<List<BookRequest>>
    
    private val _remoteBooks = MutableLiveData<List<Book>>()
    val remoteBooks: LiveData<List<Book>> = _remoteBooks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _authStatus = MutableLiveData<AuthResult>()
    val authStatus: LiveData<AuthResult> = _authStatus

    private val _stats = MutableLiveData<Map<String, Int>>()
    val stats: LiveData<Map<String, Int>> = _stats

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BookRepository(database.bookDao(), database.userDao(), database.bookRequestDao())
        allBooks = repository.allBooks.asLiveData()
        allRequests = repository.allRequests.asLiveData()
    }

    sealed class AuthResult {
        object Success : AuthResult()
        data class Error(val message: String) : AuthResult()
        data class LoginSuccess(val user: User) : AuthResult()
    }

    fun register(username: String, pass: String) = viewModelScope.launch {
        if (pass.length < 6) {
            _authStatus.postValue(AuthResult.Error("A senha deve ter no mínimo 6 caracteres"))
            return@launch
        }
        val existing = repository.getUserByUsername(username)
        if (existing != null) {
            _authStatus.postValue(AuthResult.Error("Usuário já existe"))
            return@launch
        }
        val success = repository.registerUser(User(username, pass))
        if (success) _authStatus.postValue(AuthResult.Success)
        else _authStatus.postValue(AuthResult.Error("Erro ao registrar"))
    }

    fun login(username: String, pass: String) = viewModelScope.launch {
        if (username == "admin" && pass == "admin") {
            _authStatus.postValue(AuthResult.LoginSuccess(User("admin", "admin")))
            return@launch
        }
        val user = repository.login(username, pass)
        if (user != null) {
            _authStatus.postValue(AuthResult.LoginSuccess(user))
        } else {
            _authStatus.postValue(AuthResult.Error("Usuário ou senha inválidos"))
        }
    }

    // Book Operations
    fun insert(book: Book) = viewModelScope.launch { repository.insert(book) }
    fun update(book: Book) = viewModelScope.launch { repository.update(book) }
    fun delete(book: Book) = viewModelScope.launch { repository.delete(book) }

    // Request Operations
    fun requestBook(book: Book, username: String) = viewModelScope.launch {
        val request = BookRequest(
            title = book.title,
            author = book.author,
            description = book.description,
            thumbnail = book.thumbnail,
            category = book.category,
            requestedBy = username
        )
        repository.insertRequest(request)
    }

    fun approveRequest(request: BookRequest) = viewModelScope.launch {
        val book = Book(
            title = request.title,
            author = request.author,
            description = request.description,
            thumbnail = request.thumbnail,
            category = request.category
        )
        repository.insert(book)
        repository.deleteRequest(request)
    }

    fun rejectRequest(request: BookRequest) = viewModelScope.launch {
        repository.deleteRequest(request)
    }

    fun rentBook(book: Book, username: String) = viewModelScope.launch {
        if (!book.isRented) {
            repository.update(book.copy(isRented = true, rentedBy = username))
        }
    }

    fun returnBook(book: Book) = viewModelScope.launch {
        repository.update(book.copy(isRented = false, rentedBy = null))
    }

    // Stats
    fun loadStats() = viewModelScope.launch {
        val userCount = repository.getUserCount()
        val rentedCount = repository.getRentedCount()
        val availableCount = repository.getAvailableCount()
        _stats.postValue(mapOf(
            "users" to userCount,
            "rented" to rentedCount,
            "available" to availableCount
        ))
    }

    // Search
    fun searchLocal(query: String): LiveData<List<Book>> {
        return repository.searchLocal(query).asLiveData()
    }

    // Search
    fun searchRemote(query: String) = viewModelScope.launch {
        if (query.isBlank()) return@launch
        _isLoading.postValue(true)
        try {
            val results = repository.searchRemote(query)
            _remoteBooks.postValue(results)
        } catch (e: Exception) {
            _remoteBooks.postValue(emptyList())
        } finally {
            _isLoading.postValue(false)
        }
    }
}
