package com.uniconet.app.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "uniconet_books")

// Base local (DataStore) para mantener datos mientras no haya backend.
// Los libros subidos por Maestro se guardan y el Estudiante los ve.
// Las descargas se persisten para saber qué ya descargó.
class BookRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val keyBooks = stringPreferencesKey("books_json")
    private val keyDownloads = stringPreferencesKey("downloads_json")

    // Libros locales con estado de descarga aplicado
    val libros: Flow<List<Book>> = context.dataStore.data.map { prefs ->
        val booksJson = prefs[keyBooks]
        val downloadsJson = prefs[keyDownloads]
        val base = if (booksJson.isNullOrBlank()) defaultBooks() else decodeBooks(booksJson)
        val downloadedIds = decodeDownloads(downloadsJson)
        base.map { it.copy(descargado = downloadedIds.contains(it.id)) }
    }

    suspend fun subirLibro(titulo: String, autor: String, categoria: String, archivoNombre: String = "") {
        val base = currentBooks()
        val nuevo = Book(
            id = "local_${base.size + 1}_${System.currentTimeMillis()}",
            titulo = titulo.trim(),
            autor = autor.trim().ifBlank { "---" },
            categoria = categoria.trim().ifBlank { "General" },
            estado = "Disponible",
            paginas = "",
            archivoNombre = archivoNombre.trim()
        )
        val actualizado = listOf(nuevo) + base
        context.dataStore.edit { prefs ->
            prefs[keyBooks] = encodeBooks(actualizado)
        }
    }

    suspend fun marcarDescargado(id: String) {
        val prefs = context.dataStore.data.first()
        val ids = decodeDownloads(prefs[keyDownloads]).toMutableSet()
        ids.add(id)
        context.dataStore.edit { p ->
            p[keyDownloads] = encodeDownloads(ids)
        }
    }

    suspend fun eliminarLibro(id: String) {
        val base = currentBooks().filter { it.id != id }
        context.dataStore.edit { prefs ->
            prefs[keyBooks] = encodeBooks(base)
        }
    }

    private suspend fun currentBooks(): List<Book> {
        val prefs = context.dataStore.data.first()
        val jsonStr = prefs[keyBooks]
        return if (jsonStr.isNullOrBlank()) defaultBooks() else decodeBooks(jsonStr)
    }

    private fun decodeBooks(s: String): List<Book> = runCatching {
        json.decodeFromString(ListSerializer(Book.serializer()), s)
    }.getOrDefault(emptyList())

    private fun encodeBooks(list: List<Book>): String =
        json.encodeToString(ListSerializer(Book.serializer()), list)

    private fun decodeDownloads(s: String?): Set<String> {
        if (s.isNullOrBlank()) return emptySet()
        return runCatching {
            json.decodeFromString(ListSerializer(String.serializer()), s).toSet()
        }.getOrDefault(emptySet())
    }

    private fun encodeDownloads(ids: Set<String>): String =
        json.encodeToString(ListSerializer(String.serializer()), ids.toList())

    private fun defaultBooks(): List<Book> = listOf(
        Book("1", "Algebra Lineal", "Stanley Grossman", "Matemáticas", "Disponible", "680 págs."),
        Book("2", "Introducción a la literatura", "María Gómez", "Literatura", "Disponible", "320 págs."),
        Book("3", "Ciencias para el siglo XXI", "Carlos Rivera", "Ciencias", "Disponible", "450 págs."),
        Book("4", "Historia institucional", "Editorial Uniconect", "Historia", "Disponible", "210 págs."),
        Book("5", "Cálculo Diferencial e Integral", "Purcell & Varberg", "Matemáticas", "Disponible", "612 págs."),
        Book("6", "Física Universitaria Vol. 1", "Sears & Zemansky", "Física", "Prestado", "848 págs."),
        Book("7", "Programación en Kotlin", "Antonio Leal", "Programación", "Disponible", "320 págs."),
        Book("8", "Álgebra Lineal (ed. 2024)", "Howard Anton", "Matemáticas", "Disponible", "540 págs."),
        Book("9", "Química General", "Theodore Brown", "Química", "Disponible", "980 págs."),
        Book("10", "Otro Libro", "Texto informativo", "General", "Disponible", ""),
        Book("11", "Otro Libro", "Texto informativo", "General", "Disponible", ""),
        Book("12", "Otro Libro", "Texto informativo", "General", "Disponible", "")
    )
}
