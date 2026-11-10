package com.uniconet.app.presentation.biblioteca

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniconet.app.data.Book
import com.uniconet.app.data.BookDownloadHelper
import com.uniconet.app.data.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel compartido para Estudiante (descargar) y Maestro (subir).
// Mantiene datos en base local (DataStore) hasta que haya backend.
class BibliotecaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = BookRepository(application.applicationContext)

    val libros: StateFlow<List<Book>> = repo.libros.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun subirLibro(titulo: String, autor: String, categoria: String, archivoNombre: String = "") {
        viewModelScope.launch {
            repo.subirLibro(titulo, autor, categoria, archivoNombre)
        }
    }

    fun descargarLibro(book: Book, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val ruta = BookDownloadHelper.descargar(getApplication(), book)
                repo.marcarDescargado(book.id)
                onResult(true, ruta)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun eliminarLibro(id: String) {
        viewModelScope.launch {
            repo.eliminarLibro(id)
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BibliotecaViewModel(application) as T
            }
        }
    }
}
