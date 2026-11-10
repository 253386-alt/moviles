package com.uniconet.app.data

import kotlinx.serialization.Serializable

@Serializable
data class GrupoEspecial(
    val id: String,
    val nombre: String,
    val integrantes: String,
    val estado: String,
    val descripcion: String,
    val fecha: String = "Pendiente de definir",
    val maestro: String = "Sin asignación registrada"
)
