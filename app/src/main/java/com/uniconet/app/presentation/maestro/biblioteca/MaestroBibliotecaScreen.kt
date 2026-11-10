package com.uniconet.app.presentation.maestro.biblioteca

import android.net.Uri
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
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
import com.uniconet.app.data.MaestroArchivoHelper
import com.uniconet.app.data.FileStorageHelper
import com.uniconet.app.presentation.biblioteca.BibliotecaViewModel
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferiorMaestro
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.components.PildoraUniConect
import com.uniconet.app.presentation.components.SelectorArchivo
import com.uniconet.app.presentation.components.SeccionNegra
import com.uniconet.app.presentation.components.TarjetaNegra
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.PaletaMaestro
import com.uniconet.app.ui.theme.TextoSecundario

// Biblioteca Maestro (admin) según Figma (134:119) - Recuadros NEGROS.
// Subida funcional con base local (DataStore Uniconet).
@Composable
fun MaestroBibliotecaScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    viewModel: BibliotecaViewModel = viewModel(factory = BibliotecaViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    var busqueda by remember { mutableStateOf("") }
    var tituloLibro by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var archivoSeleccionado by remember { mutableStateOf<Uri?>(null) }
    var nombreArchivo by remember { mutableStateOf<String?>(null) }
    var mostrarExito by remember { mutableStateOf(false) }
    var errorSubida by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    val libros by viewModel.libros.collectAsState()
    val filtrado = libros.filter {
        it.titulo.contains(busqueda, ignoreCase = true) || it.autor.contains(busqueda, ignoreCase = true)
    }

    FondoDegradado(paleta = PaletaMaestro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 110.dp)
        ) {
            Column {
                Text("Uniconet", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, letterSpacing = 2.sp)
                Text("Biblioteca", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Administra libros, revisa el catálogo y sube nuevos títulos para tu comunidad.",
                    color = TextoSecundario,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SeccionNegra {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Subir libro", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Agrega un nuevo título al catálogo de la institución.", color = TextoSecundario, fontSize = 12.sp)
                    }
                    PildoraUniConect("PDF / EPUB", CristalNegro, Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                SelectorArchivo(
                    titulo = "Arrastra un archivo o selecciona uno",
                    subtitulo = "Sube la portada, el archivo del libro y completa los datos básicos del título.",
                    archivoUri = archivoSeleccionado,
                    nombreArchivo = nombreArchivo,
                    onArchivoElegido = { uri, nombre ->
                        archivoSeleccionado = uri
                        nombreArchivo = nombre
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                CampoAdmin(label = "Título del libro", value = tituloLibro, onValueChange = { tituloLibro = it }, placeholder = "Ej: Introducción a la literatura contemporánea")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoAdmin(label = "Autor", value = autor, onValueChange = { autor = it }, placeholder = "Ej: María Gómez", modifier = Modifier.weight(1f))
                    CampoAdmin(label = "Categoría", value = categoria, onValueChange = { categoria = it }, placeholder = "Ej: Literatura", modifier = Modifier.weight(1f))
                }

                errorSubida?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color(0xFFFFE0E0), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (tituloLibro.isBlank() || autor.isBlank()) {
                            errorSubida = "Completa título y autor para subir el libro."
                        } else {
                            errorSubida = null
                            scope.launch {
                                val nombreSeguro = if (archivoSeleccionado != null) FileStorageHelper.copiarArchivo(context, archivoSeleccionado, nombreArchivo ?: "archivo") else ""
                                val nombreFinal = nombreSeguro.ifBlank { nombreArchivo ?: "" }
                                viewModel.subirLibro(tituloLibro, autor, categoria, nombreFinal)
                                tituloLibro = ""
                                autor = ""
                                categoria = ""
                                archivoSeleccionado = null
                                nombreArchivo = null
                                mostrarExito = true
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Subir libro", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SeccionNegra {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Catálogo de libros", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Revisa los títulos disponibles y administra el inventario.", color = TextoSecundario, fontSize = 12.sp)
                    }
                    PildoraUniConect("${libros.size} títulos", Color.White, AzulAccionUni)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar por título o autor", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.White.copy(alpha = 0.6f)) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CristalNegro,
                        unfocusedContainerColor = CristalNegro,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = BordeCristal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                filtrado.forEach { libro ->
                    TarjetaNegra {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(CristalNegro, RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(libro.titulo, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(libro.autor, color = TextoSecundario, fontSize = 12.sp)
                                Text(libro.categoria, color = TextoSecundario, fontSize = 11.sp)
                                if (libro.archivoNombre.isNotBlank()) {
                                    Text("Archivo: ${libro.archivoNombre}", color = Color(0xFF4CAF50), fontSize = 11.sp)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                PildoraUniConect(libro.estado, Color.White, AzulAccionUni)
                                IconButton(onClick = { viewModel.eliminarLibro(libro.id) }) {
                                    Icon(Icons.Default.Delete, null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        BarraInferiorMaestro(
            seleccionado = "Biblioteca",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "Libro subido",
            mensaje = "El libro quedó guardado en la base local y ya es visible para Estudiantes.",
            textoBoton = "Aceptar",
            onAceptar = { mostrarExito = false }
        )
    }
}

@Composable
private fun CampoAdmin(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
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
