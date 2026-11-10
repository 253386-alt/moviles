package com.uniconet.app.network.dto

import kotlinx.serialization.Serializable

// ── Auth ───────────────────────────────────────────────────
@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val nombre: String = "", val rol: String = "")

@Serializable
data class ChangePasswordRequest(val currentPassword: String, val newPassword: String)

@Serializable
data class ApiMessage(val message: String = "")

// ── Libros / Biblioteca ───────────────────────────────────
@Serializable
data class BookDto(
    val id: String = "",
    val titulo: String = "",
    val autor: String = "",
    val categoria: String = "",
    val estado: String = "Disponible",
    val paginas: String = "",
    val archivoNombre: String = ""
)

@Serializable
data class UploadBookRequest(val titulo: String, val autor: String, val categoria: String, val archivoNombre: String = "")

// ── Foro ───────────────────────────────────────────────────
@Serializable
data class AnuncioDto(
    val id: String = "",
    val autor: String = "",
    val fecha: String = "",
    val titulo: String = "",
    val contenido: String = "",
    val destinatarios: String = "Todos los estudiantes de la carrera",
    val likes: Int = 0,
    val borrador: Boolean = false,
    val archivoNombre: String = ""
)

@Serializable
data class PublishAnuncioRequest(
    val titulo: String,
    val contenido: String,
    val destinatarios: String,
    val archivoNombre: String = ""
)

@Serializable
data class ComentarioDto(
    val id: String = "",
    val anuncioId: String = "",
    val autor: String = "",
    val texto: String = "",
    val fecha: String = ""
)

@Serializable
data class AddComentarioRequest(val autor: String, val texto: String)

// ── Grupos especiales ────────────────────────────────────
@Serializable
data class GrupoDto(
    val id: String = "",
    val nombre: String = "",
    val integrantes: String = "",
    val estado: String = "Pendiente",
    val descripcion: String = "",
    val fecha: String = "",
    val maestro: String = ""
)

@Serializable
data class CreateGrupoRequest(
    val nombre: String,
    val integrantes: String,
    val descripcion: String,
    val fecha: String,
    val maestro: String
)

@Serializable
data class GrupoActionRequest(val accion: String = "") // aceptar / denegar
