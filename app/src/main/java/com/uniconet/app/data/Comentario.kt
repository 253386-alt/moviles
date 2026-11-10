package com.uniconet.app.data

import kotlinx.serialization.Serializable

@Serializable
data class ComentarioForo(
    val id: String,
    val anuncioId: String,
    val autor: String,
    val texto: String,
    val fecha: String = "Hoy"
)
