package com.uniconet.app.presentation.maestro

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uniconet.app.data.AuthRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MaestroViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AuthRepository(application.applicationContext)

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var passActual by mutableStateOf("maestro123")
        private set

    init {
        viewModelScope.launch {
            repo.passMaestro.collectLatest { passActual = it }
        }
    }

    fun onCorreoChange(nuevo: String) {
        correo = nuevo
        error = null
    }

    fun onContrasenaChange(nueva: String) {
        contrasena = nueva
        error = null
    }

    fun onIniciarClick(): Boolean {
        if (correo.isBlank() || contrasena.isBlank()) {
            error = "Completa todos los campos"
            return false
        }
        if (correo.trim().equals("sebas@gmail.com", ignoreCase = true)) {
            error = "Este correo solo es válido para Estudiantes"
            return false
        }
        if (!correo.trim().equals("maestro@gmail.com", ignoreCase = true)) {
            error = "Correo incorrecto. Revisa tu correo y vuelve a intentar."
            return false
        }
        if (contrasena != passActual) {
            error = "Contraseña incorrecta. Inténtalo de nuevo."
            return false
        }
        error = null
        return true
    }

    fun cambiarContrasena(actual: String, nueva: String, confirmar: String): Boolean {
        if (actual != passActual) {
            error = "La contraseña actual es incorrecta"
            return false
        }
        if (nueva.length < 4) {
            error = "La nueva contraseña debe tener mínimo 4 caracteres"
            return false
        }
        if (nueva != confirmar) {
            error = "Las contraseñas nuevas no coinciden"
            return false
        }
        viewModelScope.launch {
            repo.setPassMaestro(nueva)
        }
        passActual = nueva
        error = null
        return true
    }

    fun recuperarContrasena(nueva: String, confirmar: String): Boolean {
        if (nueva.length < 4) {
            error = "La nueva contraseña debe tener mínimo 4 caracteres"
            return false
        }
        if (nueva != confirmar) {
            error = "Las contraseñas nuevas no coinciden"
            return false
        }
        viewModelScope.launch {
            repo.setPassMaestro(nueva)
        }
        passActual = nueva
        error = null
        return true
    }

    companion object {
        fun factory(application: Application): androidx.lifecycle.ViewModelProvider.Factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return MaestroViewModel(application) as T
            }
        }
    }
}
