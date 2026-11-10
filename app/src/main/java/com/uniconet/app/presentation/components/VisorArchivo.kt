package com.uniconet.app.presentation.components

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image as ImageIcon
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniconet.app.data.FileStorageHelper
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.TextoSecundario
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Visor de archivos para Estudiante: imagen real, PDF con PdfRenderer, resto texto.
// Compartir con Intent ACTION_SEND.
@Composable
fun DialogoVisorArchivo(
    archivoNombre: String?,
    titulo: String,
    onDismiss: () -> Unit
) {
    if (archivoNombre.isNullOrBlank()) return
    val context = LocalContext.current
    var bitmapImagen by remember { mutableStateOf<Bitmap?>(null) }
    var bitmapPdf by remember { mutableStateOf<Bitmap?>(null) }
    var errorPdf by remember { mutableStateOf<String?>(null) }

    val esImagen = FileStorageHelper.esImagen(archivoNombre)
    val esPdf = FileStorageHelper.esPdf(archivoNombre)

    LaunchedEffect(archivoNombre) {
        withContext(Dispatchers.IO) {
            try {
                if (esImagen) {
                    bitmapImagen = FileStorageHelper.bitmapInterno(context, archivoNombre)
                } else if (esPdf) {
                    bitmapPdf = renderPdfPrimeraPagina(context, archivoNombre)
                    if (bitmapPdf == null) errorPdf = "No se pudo renderizar el PDF aquí."
                }
            } catch (e: Exception) {
                errorPdf = "Error: ${e.message}"
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black.copy(alpha = 0.92f),
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(archivoNombre ?: "", color = TextoSecundario, fontSize = 11.sp)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, null, tint = Color.White)
                }
            }
        },
        text = {
            Column {
                when {
                    bitmapImagen != null -> {
                        Image(
                            bitmap = bitmapImagen!!.asImageBitmap(),
                            contentDescription = archivoNombre,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .background(CristalNegro, RoundedCornerShape(16.dp))
                        )
                    }
                    esPdf && bitmapPdf != null -> {
                        Image(
                            bitmap = bitmapPdf!!.asImageBitmap(),
                            contentDescription = archivoNombre,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .background(Color.White, RoundedCornerShape(16.dp))
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Vista previa: página 1 del PDF", color = TextoSecundario, fontSize = 11.sp)
                    }
                    esPdf -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = CristalNegro,
                            border = BorderStroke(1.dp, BordeCristal),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Description, null, tint = Color.White, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    errorPdf ?: "Tu PDF está guardado. Ábrelo con el visor del sistema para ver todas las páginas.",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        abrirPdfConVisor(context, archivoNombre)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.OpenInNew, null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text("Abrir con visor", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    else -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = CristalNegro,
                            border = BorderStroke(1.dp, BordeCristal),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Icon(if (esImagen) Icons.Default.ImageIcon else Icons.Default.Description, null, tint = Color.White, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Vista previa no disponible para este archivo.\nNombre: $archivoNombre",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val archivo = FileStorageHelper.archivoInterno(context, archivoNombre)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "*/*"
                            putExtra(Intent.EXTRA_TEXT, "Uniconet - $titulo\nArchivo: $archivoNombre\n${if (archivo != null) "Guardado en: ${archivo.absolutePath}" else ""}")
                            if (archivo != null && archivo.exists()) {
                                // Compartir como texto (no como stream en esta demo local)
                            }
                        }
                        context.startActivity(Intent.createChooser(intent, "Compartir"))
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Compartir", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CristalNegro, contentColor = Color.White)
                ) {
                    Text("Cerrar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    )
}

private fun renderPdfPrimeraPagina(context: android.content.Context, nombre: String): Bitmap? {
    val file = FileStorageHelper.archivoInterno(context, nombre) ?: return null
    if (!file.exists()) return null
    var pfd: ParcelFileDescriptor? = null
    var renderer: PdfRenderer? = null
    return try {
        pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        renderer = PdfRenderer(pfd)
        if (renderer.pageCount == 0) return null
        renderer.openPage(0).use { page ->
            val bmp = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bmp, android.graphics.Rect(0, 0, page.width, page.height), null, 4)
            bmp
        }
    } catch (_: Exception) {
        null
    } finally {
        try { renderer?.close() } catch (_: Exception) {}
        try { pfd?.close() } catch (_: Exception) {}
    }
}

private fun abrirPdfConVisor(context: android.content.Context, nombre: String) {
    val file = FileStorageHelper.archivoInterno(context, nombre) ?: return
    if (!file.exists()) return
    // Usamos FileProvider si está configurado, si no abrimos con file:// (Android 7+ necesita FileProvider)
    // Por ahora intentamos compartir para abrir
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file), "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Abrir PDF"))
    } catch (_: Exception) {
        // Fallback: compartir texto con ruta
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Archivo guardado en: ${file.absolutePath}")
        }
        context.startActivity(Intent.createChooser(intent, "Abrir PDF"))
    }
}
