package com.uniconet.app.presentation.grupos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniconet.app.data.GrupoEspecial
import com.uniconet.app.data.GrupoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GruposViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = GrupoRepository(application.applicationContext)

    val grupos: StateFlow<List<GrupoEspecial>> = repo.grupos.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun crear(nombre: String, integrantes: String, descripcion: String, fecha: String, maestro: String) {
        viewModelScope.launch {
            repo.crear(nombre, integrantes, descripcion, fecha, maestro)
        }
    }

    fun eliminar(id: String) {
        viewModelScope.launch { repo.eliminar(id) }
    }

    fun aceptar(id: String) {
        viewModelScope.launch { repo.aceptar(id) }
    }

    fun denegar(id: String) {
        viewModelScope.launch { repo.denegar(id) }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GruposViewModel(application) as T
            }
        }
    }
}
