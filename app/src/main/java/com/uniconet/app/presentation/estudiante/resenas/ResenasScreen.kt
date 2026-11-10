package com.uniconet.app.presentation.estudiante.resenas

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferior
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

// Reseñas Estudiante según Figma (43:827) - Recuadros NEGROS como tus archivos.
@Composable
fun ResenasScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    onIrAAjustes: () -> Unit = {}
) {
    var nombreMaestro by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarExitoEliminado by remember { mutableStateOf(false) }

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

            SeccionNegra {
                // Crear reseña
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PildoraUniConect("Crear reseña", Color.White, AzulAccionUni)
                    Text("Nuevo", color = TextoSecundario, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Escribe una nueva reseña para compartir tu opinión.",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Puedes escribir tu experiencia, destacar lo que más te gustó y guardarla para publicarla más tarde.",
                    color = TextoSecundario,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Nombre Del Maestro", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                CampoResena(
                    value = nombreMaestro,
                    onValueChange = { nombreMaestro = it },
                    placeholder = "Prof. ..."
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Comentario", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                CampoResena(
                    value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = "Cuéntanos tu experiencia y lo que más te gustó.",
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { mostrarExito = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Guardar reseña", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reseñas top
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PildoraUniConect("Reseñas", Color.White, AzulAccionUni)
                    Text("3 pendientes", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Todavía no hay reseñas disponibles.",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Cuando se publiquen nuevas reseñas, aparecerán aquí con su autor, estado y resumen.",
                    color = TextoSecundario,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = TextoSecundario, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Estado: pendiente de publicación",
                        color = TextoSecundario,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lista según Figma
                ResenaItem(
                    nombre = "Marcelo",
                    estado = "Publica",
                    contenido = "Contenido de la reseña pendiente.",
                    mostrarAcciones = false
                )

                Spacer(modifier = Modifier.height(10.dp))

                ResenaItem(
                    nombre = "Reseña pendiente",
                    estado = "Borrador",
                    contenido = "Contenido de la reseña pendiente.",
                    mostrarAcciones = true,
                    onEliminar = { mostrarExitoEliminado = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ResenaItem(
                    nombre = "Reseña pendiente",
                    estado = "En Revision",
                    contenido = "Contenido de la reseña pendiente.",
                    mostrarAcciones = true,
                    onEliminar = { mostrarExitoEliminado = true }
                )
            }
        }

        BarraInferior(
            seleccionado = "Maestro",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "La publicación se realizó con éxito",
            mensaje = "Tu reseña quedó guardada correctamente.",
            textoBoton = "Entendido",
            onAceptar = { mostrarExito = false }
        )

        AlertaExitoGenerica(
            visible = mostrarExitoEliminado,
            titulo = "La publicación se Elimino con éxito",
            mensaje = "Tu reseña fue eliminada correctamente.",
            textoBoton = "Entendido",
            onAceptar = { mostrarExitoEliminado = false }
        )
    }
}

@Composable
private fun CampoResena(value: String, onValueChange: (String) -> Unit, placeholder: String, minLines: Int = 1) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp) },
        minLines = minLines,
        shape = RoundedCornerShape(14.dp),
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

@Composable
private fun ResenaItem(nombre: String, estado: String, contenido: String, mostrarAcciones: Boolean, onEliminar: () -> Unit = {}) {
    TarjetaNegra {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PildoraUniConect(nombre, CristalNegro, Color.White)
            PildoraUniConect(estado, Color.White, AzulAccionUni)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(contenido, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)

        if (mostrarAcciones) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    onClick = onEliminar,
                    shape = RoundedCornerShape(10.dp),
                    color = CristalNegro,
                    border = BorderStroke(1.dp, BordeCristal),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                Surface(
                    onClick = { /* publicar */ },
                    shape = RoundedCornerShape(10.dp),
                    color = AzulAccionUni,
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Publicar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
