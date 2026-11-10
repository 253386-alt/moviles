package com.uniconet.app.presentation.foro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniconet.app.data.Anuncio
import com.uniconet.app.data.ComentarioForo
import com.uniconet.app.data.ForoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ForoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ForoRepository(application.applicationContext)

    val anuncios: StateFlow<List<Anuncio>> = repo.anuncios.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val comentarios: StateFlow<List<ComentarioForo>> = repo.comentarios.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun publicar(titulo: String, contenido: String, destinatarios: String, archivoNombre: String = "") {
        viewModelScope.launch { repo.publicar(titulo, contenido, destinatarios, archivoNombre) }
    }

    fun guardarBorrador(titulo: String, contenido: String, destinatarios: String, archivoNombre: String = "") {
        viewModelScope.launch { repo.guardarBorrador(titulo, contenido, destinatarios, archivoNombre) }
    }

    fun eliminar(id: String) {
        viewModelScope.launch { repo.eliminar(id) }
    }

    fun toggleLike(id: String) {
        viewModelScope.launch { repo.toggleLike(id) }
    }

    fun agregarComentario(anuncioId: String, autor: String, texto: String) {
        viewModelScope.launch { repo.agregarComentario(anuncioId, autor, texto) }
    }

    fun eliminarComentario(id: String) {
        viewModelScope.launch { repo.eliminarComentario(id) }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ForoViewModel(application) as T
            }
        }
    }
}
