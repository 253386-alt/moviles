package com.uniconet.app.presentation.estudiante.libros

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.uniconet.app.data.Book
import com.uniconet.app.presentation.biblioteca.BibliotecaViewModel
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferior
import com.uniconet.app.presentation.components.BuscadorUniConect
import com.uniconet.app.presentation.components.DialogoVisorArchivo
import com.uniconet.app.presentation.components.EncabezadoConPerfil
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.components.PildoraUniConect
import com.uniconet.app.presentation.components.SeccionNegra
import com.uniconet.app.presentation.components.TarjetaNegra
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.PaletaEstudiante
import com.uniconet.app.ui.theme.TextoSecundario

// Biblioteca / Libros Estudiante según Figma (65:11, 105:298, 85:411) - Recuadros NEGROS.
// Descarga, visualización de archivos y compartir funcionales.
@Composable
fun LibrosScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    onIrAAjustes: () -> Unit = {},
    viewModel: BibliotecaViewModel = viewModel(factory = BibliotecaViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    var busqueda by remember { mutableStateOf("") }
    var mostrarFiltros by remember { mutableStateOf(false) }
    var carreraSeleccionada by remember { mutableStateOf("Ingeniería") }
    var materiaSeleccionada by remember { mutableStateOf("Matemáticas") }
    var mostrarDescarga by remember { mutableStateOf(false) }
    var mensajeDescarga by remember { mutableStateOf("") }
    var descargandoId by remember { mutableStateOf<String?>(null) }
    var archivoAVer by remember { mutableStateOf<Pair<String, String>?>(null) }
    val context = LocalContext.current

    val libros by viewModel.libros.collectAsState()

    val filtrados = libros.filter { libro ->
        val ok = libro.titulo.contains(busqueda, ignoreCase = true) ||
                libro.autor.contains(busqueda, ignoreCase = true) ||
                libro.categoria.contains(busqueda, ignoreCase = true)
        ok
    }

    val destacado = filtrados.firstOrNull() ?: Book("0", "Algebra Lineal", "Stanley Grossman", "Matemáticas", "Disponible", "680 págs.")

    fun compartirLibro(libro: Book) {
        val texto = "Uniconet - ${libro.titulo}\nAutor: ${libro.autor}\nCategoría: ${libro.categoria}\nArchivo: ${libro.archivoNombre.ifBlank { "---" }}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir libro"))
    }

    FondoDegradado(paleta = PaletaEstudiante) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 110.dp)
        ) {
            EncabezadoConPerfil(
                nombre = "¡Hola, Sebas!",
                onClickPerfil = onIrAAjustes,
                onRegresar = onRegresar
            )

            Spacer(modifier = Modifier.height(16.dp))

            BuscadorUniConect(
                valor = busqueda,
                onValorChange = { busqueda = it },
                placeholder = "Buscar libros, temas o maestros"
            )

            Spacer(modifier = Modifier.height(14.dp))

            SeccionNegra {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PildoraUniConect(
                        if (destacado.descargado) "Descargado" else "Descargar",
                        Color.White, AzulAccionUni
                    )
                    Text("Destacado", color = TextoSecundario, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(CristalNegro, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(destacado.titulo, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Text(destacado.autor, color = TextoSecundario, fontSize = 13.sp)
                    }
                }

                if (destacado.archivoNombre.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Archivo: ${destacado.archivoNombre}", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            if (destacado.archivoNombre.isNotBlank()) archivoAVer = destacado.archivoNombre to destacado.titulo
                            else mensajeDescarga = "Este libro aún no tiene archivo adjunto. Pide al Maestro que suba uno."
                                .also { mostrarDescarga = true }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
                        modifier = Modifier.weight(1f).height(46.dp)
                    ) {
                        Text("Abrir", fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        onClick = {
                            if (descargandoId == null) {
                                descargandoId = destacado.id
                                viewModel.descargarLibro(destacado) { ok, ruta ->
                                    descargandoId = null
                                    mensajeDescarga = if (ok) ruta ?: "Descarga exitosa" else "Error: $ruta"
                                    mostrarDescarga = true
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (destacado.descargado) AzulAccionUni else CristalNegro,
                        border = BorderStroke(1.dp, BordeCristal),
                        modifier = Modifier.weight(1f).height(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (descargandoId == destacado.id) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Download, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        if (destacado.descargado) "Descargado" else "Descargar",
                                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    onClick = { compartirLibro(destacado) },
                    shape = RoundedCornerShape(12.dp),
                    color = CristalNegro,
                    border = BorderStroke(1.dp, BordeCristal),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Compartir libro", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                onClick = { mostrarFiltros = !mostrarFiltros },
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, BordeCristal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FilterList, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Filtros", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Carrera o materia para encontrar recursos relevantes", color = TextoSecundario, fontSize = 11.sp)
                        }
                    }
                    Text(if (mostrarFiltros) "▲" else "›", color = Color.White, fontSize = 18.sp)
                }
            }

            if (mostrarFiltros) {
                Spacer(modifier = Modifier.height(10.dp))
                SeccionNegra {
                    Text("Carrera", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChipRow(
                        items = listOf("Ingeniería", "Derecho", "Medicina", "Arquitectura", "Ciencias"),
                        seleccionado = carreraSeleccionada,
                        onSeleccion = { carreraSeleccionada = it }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Materia", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChipRow(
                        items = listOf("Matemáticas", "Física", "Química", "Historia", "Literatura"),
                        seleccionado = materiaSeleccionada,
                        onSeleccion = { materiaSeleccionada = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Explorar (${filtrados.size})", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            if (filtrados.isEmpty()) {
                Text("No se encontraron libros.", color = TextoSecundario, fontSize = 13.sp)
            } else {
                filtrados.chunked(2).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        fila.forEach { libro ->
                            TarjetaNegra(modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .background(CristalNegro, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Libro", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(libro.titulo, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                                Text("${libro.autor} • ${libro.paginas}", color = TextoSecundario, fontSize = 11.sp, maxLines = 1)
                                if (libro.archivoNombre.isNotBlank()) {
                                    Text("Archivo: ${libro.archivoNombre}", color = Color(0xFF4CAF50), fontSize = 10.sp, maxLines = 1)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                PildoraUniConect(
                                    if (libro.descargado) "Descargado" else libro.estado,
                                    Color.White, AzulAccionUni
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Surface(
                                        onClick = {
                                            if (descargandoId == null) {
                                                descargandoId = libro.id
                                                viewModel.descargarLibro(libro) { ok, ruta ->
                                                    descargandoId = null
                                                    mensajeDescarga = if (ok) ruta ?: "Descarga exitosa" else "Error: $ruta"
                                                    mostrarDescarga = true
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (libro.descargado) AzulAccionUni else CristalNegro,
                                        border = BorderStroke(1.dp, BordeCristal),
                                        modifier = Modifier.weight(1f).height(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            if (descargandoId == libro.id) {
                                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                                            } else {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Download, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        if (libro.descargado) "OK" else "Descargar",
                                                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Surface(
                                        onClick = { compartirLibro(libro) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = CristalNegro,
                                        border = BorderStroke(1.dp, BordeCristal),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Share, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    onClick = {
                                        if (libro.archivoNombre.isNotBlank()) archivoAVer = libro.archivoNombre to libro.titulo
                                        else {
                                            mensajeDescarga = "Este libro aún no tiene archivo para abrir."
                                            mostrarDescarga = true
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = AzulAccionUni,
                                    modifier = Modifier.fillMaxWidth().height(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("Abrir archivo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        BarraInferior(
            seleccionado = "Libros",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        AlertaExitoGenerica(
            visible = mostrarDescarga,
            titulo = if (mensajeDescarga.startsWith("Error")) "No se pudo descargar" else "Descarga exitosa",
            mensaje = mensajeDescarga.ifBlank { "Tu recurso se descargó correctamente." },
            textoBoton = "Aceptar",
            onAceptar = { mostrarDescarga = false }
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
private fun ChipRow(items: List<String>, seleccionado: String, onSeleccion: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.chunked(3).forEach { chunk ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                chunk.forEach { item ->
                    val activa = item == seleccionado
                    Surface(
                        onClick = { onSeleccion(item) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (activa) Color.White else CristalNegro,
                        border = BorderStroke(1.dp, BordeCristal)
                    ) {
                        Text(
                            text = item,
                            color = if (activa) AzulAccionUni else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
