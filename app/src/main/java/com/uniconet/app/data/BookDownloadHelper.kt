package com.uniconet.app.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Descarga real a carpeta Descargas/Uniconet usando MediaStore (Android 10+).
// En versiones viejas guarda en Downloads legacy.
object BookDownloadHelper {

    suspend fun descargar(context: Context, book: Book): String = withContext(Dispatchers.IO) {
        val nombre = "${book.titulo.replace(Regex("[\\\\/:*?\"<>|]"), "_")}.pdf"
        val contenido = buildString {
            appendLine("UNICONET - Biblioteca")
            appendLine("Título: ${book.titulo}")
            appendLine("Autor: ${book.autor}")
            appendLine("Categoría: ${book.categoria}")
            appendLine("Páginas: ${book.paginas.ifBlank { "---" }}")
            appendLine()
            appendLine("Este es el texto de muestra generado localmente.")
            appendLine("Mientras no haya backend, el contenido real lo sube el Maestro")
            appendLine("y queda guardado en la base local (DataStore Uniconet).")
            appendLine("Descargado el: ${java.time.LocalDateTime.now()}")
        }.toByteArray(Charsets.UTF_8)

        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, nombre)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Uniconet")
            }
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: error("No se pudo crear el archivo en Descargas")

        resolver.openOutputStream(uri)?.use { out ->
            out.write(contenido)
        } ?: error("No se pudo escribir el archivo")

        "Descargas/Uniconet/$nombre"
    }
}
