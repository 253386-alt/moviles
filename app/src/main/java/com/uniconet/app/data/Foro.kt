package com.uniconet.app.data

import kotlinx.serialization.Serializable

@Serializable
data class Anuncio(
    val id: String,
    val autor: String,
    val fecha: String,
    val titulo: String = "",
    val contenido: String,
    val destinatarios: String = "Todos los estudiantes de la carrera",
    val likes: Int = 0,
    val meGusta: Boolean = false,
    val borrador: Boolean = false,
    val archivoNombre: String = ""
)
