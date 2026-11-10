package com.uniconet.app.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

// Helpers para archivos subidos por Maestro (lector de nombres, etc.)
object MaestroArchivoHelper {

    fun nombreVisible(context: Context, uri: Uri?): String {
        if (uri == null) return ""
        // Intentar DISPLAY_NAME primero
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0 && cursor.moveToFirst()) {
                    cursor.getString(idx) ?: uri.lastPathSegment ?: "archivo"
                } else {
                    uri.lastPathSegment ?: "archivo"
                }
            } ?: (uri.lastPathSegment ?: "archivo")
        } catch (_: Exception) {
            uri.lastPathSegment ?: "archivo"
        }
    }

    fun extensionNombre(nombre: String): String {
        val pos = nombre.lastIndexOf('.')
        return if (pos >= 0) nombre.substring(pos + 1, nombre.length) else ""
    }
}
