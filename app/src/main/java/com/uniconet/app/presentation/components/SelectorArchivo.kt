package com.uniconet.app.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniconet.app.data.MaestroArchivoHelper
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.TextoSecundario

// Selector de archivo funcional para Maestro (Foro y Biblioteca).
// Al tocar abre el selector del celular y muestra el nombre real.
// Botón X para quitar. Al subir se guarda el nombre en la base local.
@Composable
fun SelectorArchivo(
    titulo: String,
    subtitulo: String,
    archivoUri: Uri?,
    nombreArchivo: String?,
    onArchivoElegido: (Uri?, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val nombre = MaestroArchivoHelper.nombreVisible(context, uri)
        onArchivoElegido(uri, nombre)
    }

    Surface(
        onClick = { picker.launch("*/*") },
        shape = RoundedCornerShape(16.dp),
        color = if (archivoUri != null) CristalNegro.copy(alpha = 0.85f) else CristalNegro,
        border = BorderStroke(
            1.dp,
            if (archivoUri != null) AzulAccionUni.copy(alpha = 0.6f) else BordeCristal
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (archivoUri != null) Icons.Default.CheckCircle else Icons.Default.AttachFile,
                contentDescription = null,
                tint = if (archivoUri != null) Color(0xFF4CAF50) else Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (archivoUri != null) nombreArchivo ?: "Archivo seleccionado" else titulo,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (archivoUri != null) "Archivo listo para subir" else subtitulo,
                    color = TextoSecundario,
                    fontSize = 11.sp
                )
            }
            if (archivoUri != null) {
                IconButton(onClick = { onArchivoElegido(null, "") }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Clear, contentDescription = "Quitar", tint = TextoSecundario, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
