package com.uniconet.app.presentation.maestro.home

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.uniconet.app.data.FileStorageHelper
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferiorMaestro
import com.uniconet.app.presentation.components.DialogoVisorArchivo
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.components.SeccionComentarios
import com.uniconet.app.presentation.components.SelectorArchivo
import com.uniconet.app.presentation.foro.ForoViewModel
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.NegroSeccion
import com.uniconet.app.ui.theme.NegroTarjeta
import com.uniconet.app.ui.theme.PaletaMaestro
import com.uniconet.app.ui.theme.TextoSecundario

// Home Maestro según Figma (105:641 / 134:21) - Foro funcional con base local.
// Maestro crea/anuncia, ve y borra. Estudiante ve lo mismo.
@Composable
fun MaestroHomeScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    onIrAAjustes: () -> Unit = {},
    viewModel: ForoViewModel = viewModel(factory = ForoViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    var mostrarEditor by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var destinatarios by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var mostrarExito by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf("") }
    var archivoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var nombreArchivo by remember { mutableStateOf<String?>(null) }
    var archivoAVer by remember { mutableStateOf<Pair<String, String>?>(null) }
    var anuncioExpandido by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    val anuncios by viewModel.anuncios.collectAsState()
    val comentarios by viewModel.comentarios.collectAsState()

    FondoDegradado(paleta = PaletaMaestro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 110.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    onClick = onIrAAjustes,
                    shape = RoundedCornerShape(30.dp),
                    color = CristalNegro,
                    border = BorderStroke(1.dp, BordeCristal)
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(36.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("¡Hola, Maestro!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White, modifier = Modifier.size(26.dp))
                    }
                    IconButton(onClick = onRegresar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                onClick = { mostrarEditor = !mostrarEditor },
                shape = RoundedCornerShape(20.dp),
                color = NegroSeccion,
                border = BorderStroke(1.dp, BordeCristal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).background(AzulAccionUni, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Crear nuevo anuncio", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Publica una novedad para tus estudiantes", color = TextoSecundario, fontSize = 12.sp)
                    }
                }
            }

            if (mostrarEditor) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = NegroSeccion,
                    border = BorderStroke(1.dp, BordeCristal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nuevo anuncio", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Redacta y publica una novedad para tus estudiantes", color = TextoSecundario, fontSize = 12.sp)
                            }
                            Text("✕", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(4.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        CampoEditable(label = "Título", value = titulo, onValueChange = { titulo = it }, placeholder = "Revisión de avances y convocatoria a próximos talleres")
                        Spacer(modifier = Modifier.height(8.dp))
                        CampoEditable(label = "Destinatarios", value = destinatarios, onValueChange = { destinatarios = it }, placeholder = "Todos los estudiantes de la carrera")
                        Spacer(modifier = Modifier.height(8.dp))
                        CampoEditable(label = "Contenido", value = contenido, onValueChange = { contenido = it }, placeholder = "Comparto la convocatoria para la revisión de avances y los próximos talleres. Por favor revisen los materiales adjuntos y confirmen su asistencia antes del viernes.", minLines = 3)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Archivos adjuntos", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        SelectorArchivo(
                            titulo = "Arrastra archivos o toca para subir",
                            subtitulo = "PDF, DOCX o imágenes",
                            archivoUri = archivoUri,
                            nombreArchivo = nombreArchivo,
                            onArchivoElegido = { uri, nombre ->
                                archivoUri = uri
                                nombreArchivo = nombre
                            }
                        )

                        error?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it, color = Color(0xFFFFE0E0), fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                onClick = {
                                    if (titulo.isBlank() || contenido.isBlank()) error = "Completa título y contenido para guardar"
                                    else {
                                        error = null
                                        scope.launch {
                                            val nombreSeguro = if (archivoUri != null) FileStorageHelper.copiarArchivo(context, archivoUri, nombreArchivo ?: "archivo") else ""
                                            val nombreFinal = nombreSeguro.ifBlank { nombreArchivo ?: "" }
                                            viewModel.guardarBorrador(titulo, contenido, destinatarios.ifBlank { "Todos los estudiantes de la carrera" }, nombreFinal)
                                            mensajeExito = "Borrador guardado"
                                            mostrarExito = true
                                            titulo = ""
                                            destinatarios = ""
                                            contenido = ""
                                            archivoUri = null
                                            nombreArchivo = null
                                            mostrarEditor = false
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = CristalNegro,
                                border = BorderStroke(1.dp, BordeCristal),
                                modifier = Modifier.weight(1f).height(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("Guardar borrador", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                            Button(
                                onClick = {
                                    if (titulo.isBlank() || contenido.isBlank()) error = "Completa título y contenido para publicar"
                                    else {
                                        error = null
                                        scope.launch {
                                            val nombreSeguro = if (archivoUri != null) FileStorageHelper.copiarArchivo(context, archivoUri, nombreArchivo ?: "archivo") else ""
                                            val nombreFinal = nombreSeguro.ifBlank { nombreArchivo ?: "" }
                                            viewModel.publicar(titulo, contenido, destinatarios.ifBlank { "Todos los estudiantes de la carrera" }, nombreFinal)
                                            mensajeExito = "Anuncio publicado. Ya es visible para Estudiantes."
                                            mostrarExito = true
                                            titulo = ""
                                            destinatarios = ""
                                            contenido = ""
                                            archivoUri = null
                                            nombreArchivo = null
                                            mostrarEditor = false
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
                                modifier = Modifier.weight(1f).height(46.dp)
                            ) {
                                Text("Publicar ahora", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = NegroSeccion,
                border = BorderStroke(1.dp, BordeCristal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
                            Text(
                                text = "Foro",
                                color = AzulAccionUni,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Anuncios recientes", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Últimas publicaciones", color = TextoSecundario, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (anuncios.isEmpty()) {
                        Text("Todavía no hay anuncios. Crea el primero arriba.", color = TextoSecundario, fontSize = 13.sp)
                    }

                    anuncios.forEach { anuncio ->
                        val coms = comentarios.filter { it.anuncioId == anuncio.id }
                        val expandido = anuncioExpandido == anuncio.id
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = NegroTarjeta,
                            border = BorderStroke(1.dp, BordeCristal),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(shape = RoundedCornerShape(20.dp), color = CristalNegro) {
                                        Text(
                                            text = anuncio.autor,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(anuncio.fecha, color = TextoSecundario, fontSize = 11.sp)
                                        IconButton(onClick = { viewModel.eliminar(anuncio.id) }, modifier = Modifier.size(28.dp)) {
                                            Icon(Icons.Default.Delete, null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                                if (anuncio.titulo.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(anuncio.titulo, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(anuncio.contenido, color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                                if (anuncio.archivoNombre.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        onClick = { archivoAVer = anuncio.archivoNombre to anuncio.titulo.ifBlank { anuncio.contenido.take(30) } },
                                        shape = RoundedCornerShape(12.dp),
                                        color = CristalNegro,
                                        border = BorderStroke(1.dp, BordeCristal),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AttachFile, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Archivo: ${anuncio.archivoNombre}", color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                            Text("Ver", color = AzulAccionUni, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                if (anuncio.borrador) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Borrador", color = TextoSecundario, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { anuncioExpandido = if (expandido) null else anuncio.id }
                                        ) {
                                            Icon(Icons.AutoMirrored.Filled.Comment, null, tint = TextoSecundario, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(coms.size.toString(), color = TextoSecundario, fontSize = 12.sp)
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            val texto = "Uniconet - ${anuncio.titulo}\n${anuncio.contenido}\nArchivo: ${anuncio.archivoNombre.ifBlank { "---" }}"
                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_TEXT, texto)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "Compartir"))
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Share, null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                                    }
                                }
                                if (expandido) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    SeccionComentarios(
                                        anuncioId = anuncio.id,
                                        comentarios = coms,
                                        onAgregar = { txt -> viewModel.agregarComentario(anuncio.id, "Maestro", txt) },
                                        onEliminar = { id -> viewModel.eliminarComentario(id) }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        BarraInferiorMaestro(
            seleccionado = "Inicio",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "Anuncio listo",
            mensaje = mensajeExito,
            textoBoton = "Entendido",
            onAceptar = { mostrarExito = false }
        )

        if (archivoAVer != null) {
            DialogoVisorArchivo(
                archivoNombre = archivoAVer?.first,
                titulo = archivoAVer?.second ?: "Archivo",
                onDismiss = { archivoAVer = null }
            )
        }
    }
}

@Composable
private fun CampoEditable(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, minLines: Int = 1) {
    Column {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp) },
            minLines = minLines,
            singleLine = minLines == 1,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CristalNegro,
                unfocusedContainerColor = CristalNegro,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AzulAccionUni,
                unfocusedBorderColor = BordeCristal
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
