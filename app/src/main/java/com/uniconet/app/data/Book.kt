package com.uniconet.app.data

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String,
    val titulo: String,
    val autor: String,
    val categoria: String,
    val estado: String = "Disponible",
    val paginas: String = "",
    val descargado: Boolean = false,
    val archivoNombre: String = ""
)
