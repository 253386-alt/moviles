package com.uniconet.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private val Context.dataStoreGrupos by preferencesDataStore(name = "uniconet_grupos")

// Base local para grupos especiales: creada por Estudiante, revisada por Maestro.
class GrupoRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val key = stringPreferencesKey("grupos_json")

    val grupos: Flow<List<GrupoEspecial>> = context.dataStoreGrupos.data.map { prefs ->
        val s = prefs[key]
        if (s.isNullOrBlank()) defaultGrupos() else decode(s)
    }

    suspend fun crear(nombre: String, integrantes: String, descripcion: String, fecha: String, maestro: String) {
        val base = current()
        val nuevo = GrupoEspecial(
            id = "g_${System.currentTimeMillis()}",
            nombre = nombre.trim(),
            integrantes = integrantes.trim().ifBlank { "1 estudiante" },
            estado = "Pendiente",
            descripcion = descripcion.trim().ifBlank { "Información del grupo pendiente." },
            fecha = fecha.trim().ifBlank { "Pendiente de definir" },
            maestro = maestro.trim().ifBlank { "Sin asignación registrada" }
        )
        save(listOf(nuevo) + base)
    }

    suspend fun eliminar(id: String) {
        save(current().filter { it.id != id })
    }

    suspend fun aceptar(id: String) {
        val updated = current().map { if (it.id == id) it.copy(estado = "Aceptado") else it }
        save(updated)
    }

    suspend fun denegar(id: String) {
        val updated = current().map { if (it.id == id) it.copy(estado = "Denegado") else it }
        save(updated)
    }

    private suspend fun current(): List<GrupoEspecial> {
        val prefs = context.dataStoreGrupos.data.first()
        val s = prefs[key]
        return if (s.isNullOrBlank()) defaultGrupos() else decode(s)
    }

    private suspend fun save(list: List<GrupoEspecial>) {
        context.dataStoreGrupos.edit { p -> p[key] = encode(list) }
    }

    private fun decode(s: String): List<GrupoEspecial> = runCatching {
        json.decodeFromString(ListSerializer(GrupoEspecial.serializer()), s)
    }.getOrDefault(emptyList())

    private fun encode(list: List<GrupoEspecial>): String =
        json.encodeToString(ListSerializer(GrupoEspecial.serializer()), list)

    private fun defaultGrupos(): List<GrupoEspecial> = listOf(
        GrupoEspecial("1", "Grupo especial física", "20/30", "Pendiente 20/30", "Información del grupo pendiente.", "Pendiente de definir", "Sin asignación registrada"),
        GrupoEspecial("2", "Grupo pendiente", "30/30", "Lleno 30/30", "Información del grupo pendiente.", "Pendiente de definir", "Sin asignación registrada"),
        GrupoEspecial("3", "Grupo pendiente", "Sin maestro", "Sin Maestro", "Información del grupo pendiente.", "Pendiente de definir", "Sin asignación registrada"),
        GrupoEspecial("4", "Grupo Especial 1", "12 estudiantes · Solicitud enviada hoy", "Pendiente", "El grupo solicita acceso a recursos especiales para completar su proyecto final.", "Hoy", "Sin asignación registrada"),
        GrupoEspecial("5", "Grupo Especial 2", "8 estudiantes · Solicitud enviada ayer", "Pendiente", "El grupo necesita acceso a materiales adicionales para su taller de investigación.", "Ayer", "Sin asignación registrada"),
        GrupoEspecial("6", "Grupo Especial 3", "15 estudiantes · Solicitud enviada hace 2 días", "Pendiente", "El grupo solicita acceso a espacios de trabajo y recursos de apoyo para su proyecto.", "Hace 2 días", "Sin asignación registrada")
    )
}
