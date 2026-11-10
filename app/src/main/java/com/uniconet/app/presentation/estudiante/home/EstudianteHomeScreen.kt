package com.uniconet.app.presentation.estudiante.home

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.uniconet.app.presentation.components.BarraInferior
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.components.SeccionComentarios
import com.uniconet.app.presentation.foro.ForoViewModel
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.NegroSeccion
import com.uniconet.app.ui.theme.NegroTarjeta
import com.uniconet.app.ui.theme.PaletaEstudiante
import com.uniconet.app.ui.theme.TextoSecundario
import com.uniconet.app.presentation.components.DialogoVisorArchivo

// Home Estudiante según Figma (43:209) - Recuadros NEGROS.
// Lee los anuncios publicados por Maestro desde la base local.
// Ahora incluye: ver archivos, compartir y comentarios.
@Composable
fun EstudianteHomeScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    onIrAAjustes: () -> Unit = {},
    viewModel: ForoViewModel = viewModel(factory = ForoViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    val anuncios by viewModel.anuncios.collectAsState()
    val comentarios by viewModel.comentarios.collectAsState()
    val visibles = anuncios.filter { !it.borrador }
    var archivoAVer by remember { mutableStateOf<Pair<String, String>?>(null) }
    var anuncioExpandido by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    FondoDegradado(paleta = PaletaEstudiante) {
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
                        Text("¡Hola, Sebas!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = NegroSeccion,
                border = BorderStroke(1.dp, BordeCristal)
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
                        Text("Nuevos temas", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (visibles.isEmpty()) {
                        Text("Todavía no hay anuncios. Revisa de nuevo en unos minutos.", color = TextoSecundario, fontSize = 13.sp)
                    }

                    visibles.forEach { anuncio ->
                        val coms = comentarios.filter { it.anuncioId == anuncio.id }
                        val expandido = anuncioExpandido == anuncio.id
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = NegroTarjeta,
                            border = BorderStroke(1.dp, BordeCristal)
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
                                    Text(anuncio.fecha, color = TextoSecundario, fontSize = 11.sp)
                                }

                                if (anuncio.titulo.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(anuncio.titulo, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = anuncio.contenido,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp
                                )

                                if (anuncio.archivoNombre.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        onClick = { archivoAVer = anuncio.archivoNombre to anuncio.titulo.ifBlank { anuncio.contenido.take(30) } },
                                        shape = RoundedCornerShape(12.dp),
                                        color = CristalNegro,
                                        border = BorderStroke(1.dp, BordeCristal),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Attachment, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("Archivo adjunto", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                Text(anuncio.archivoNombre, color = TextoSecundario, fontSize = 11.sp, maxLines = 1)
                                            }
                                            Text("Ver", color = AzulAccionUni, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = anuncio.destinatarios,
                                    color = TextoSecundario,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { viewModel.toggleLike(anuncio.id) }
                                        ) {
                                            Icon(
                                                Icons.Default.ThumbUp,
                                                contentDescription = "Me gusta",
                                                tint = if (anuncio.meGusta) AzulAccionUni else TextoSecundario,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(anuncio.likes.toString(), color = if (anuncio.meGusta) AzulAccionUni else TextoSecundario, fontSize = 12.sp)
                                        }
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
                                            val texto = "Uniconet - ${anuncio.titulo}\n${anuncio.contenido}\nDestinatarios: ${anuncio.destinatarios}\nArchivo: ${anuncio.archivoNombre.ifBlank { "---" }}"
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
                                        onAgregar = { txt -> viewModel.agregarComentario(anuncio.id, "Estudiante · Sebas", txt) },
                                        onEliminar = { id -> viewModel.eliminarComentario(id) }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        BarraInferior(
            seleccionado = "Inicio",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
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
