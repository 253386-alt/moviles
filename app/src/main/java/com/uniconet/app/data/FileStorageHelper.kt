package com.uniconet.app.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Guarda archivos subidos por Maestro en almacenamiento interno
// y permite a Estudiante visualizarlos (imagen/PDF).
object FileStorageHelper {

    private fun dir(context: Context): File {
        val d = File(context.filesDir, "uniconet_uploads")
        if (!d.exists()) d.mkdirs()
        return d
    }

    suspend fun copiarArchivo(context: Context, uri: Uri?, nombreOriginal: String): String = withContext(Dispatchers.IO) {
        if (uri == null || nombreOriginal.isBlank()) return@withContext ""
        try {
            val nombreSeguro = nombreOriginal.replace(Regex("[\\\\/:*?\"<>|]"), "_")
            val dest = File(dir(context), nombreSeguro)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(dest).use { out ->
                    input.copyTo(out)
                }
            }
            nombreSeguro
        } catch (_: Exception) {
            nombreOriginal
        }
    }

    fun archivoInterno(context: Context, nombre: String): File? {
        val f = File(dir(context), nombre)
        return if (f.exists()) f else null
    }

    fun bitmapInterno(context: Context, nombre: String): Bitmap? {
        val f = archivoInterno(context, nombre) ?: return null
        return runCatching { BitmapFactory.decodeFile(f.absolutePath) }.getOrNull()
    }

    fun esImagen(nombre: String): Boolean {
        val ext = nombre.lowercase().substringAfterLast(".", "")
        return ext in listOf("jpg", "jpeg", "png", "webp", "gif")
    }

    fun esPdf(nombre: String): Boolean = nombre.lowercase().endsWith(".pdf")

    fun leerTexto(context: Context, nombre: String): String? {
        val f = archivoInterno(context, nombre) ?: return null
        return runCatching {
            f.readText(Charsets.UTF_8).take(4000)
        }.getOrNull()
    }
}
