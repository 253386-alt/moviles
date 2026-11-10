package com.uniconet.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Recuadro inferior en negro como en tus archivos (NegroBarra 0.85)
private val FondoBarra = Color.Black.copy(alpha = 0.85f)
private val ColorActivo = Color(0xFF2E6FBF)
private val ColorInactivo = Color.White.copy(alpha = 0.5f)

data class ElementoNavegacion(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

@Composable
fun BarraInferior(
    seleccionado: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ElementoNavegacion("inicio", "Inicio", Icons.Default.Home),
        ElementoNavegacion("libros", "Libros", Icons.Default.Book),
        ElementoNavegacion("maestro", "Maestro", Icons.Default.School),
        ElementoNavegacion("validar", "Validar", Icons.Default.CheckCircle)
    )
    BarraUniConect(items = items, seleccionado = seleccionado, onItemClick = onItemClick, modifier = modifier)
}

// Barra para Maestro según Figma (Home Maestro / Grupos_Especiales):
// Inicio / Biblioteca / Grupos  (3 items)
@Composable
fun BarraInferiorMaestro(
    seleccionado: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ElementoNavegacion("inicio", "Inicio", Icons.Default.Home),
        ElementoNavegacion("biblioteca", "Biblioteca", Icons.Default.Book),
        ElementoNavegacion("grupos", "Grupos", Icons.Default.Groups)
    )
    BarraUniConect(items = items, seleccionado = seleccionado, onItemClick = onItemClick, modifier = modifier)
}

@Composable
private fun BarraUniConect(
    items: List<ElementoNavegacion>,
    seleccionado: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp),
        shape = RoundedCornerShape(32.dp),
        color = FondoBarra,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val esSeleccionado = seleccionado.equals(item.ruta, ignoreCase = true) ||
                        seleccionado.equals(item.etiqueta, ignoreCase = true)
                val colorContenido = if (esSeleccionado) ColorActivo else ColorInactivo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onItemClick(item.ruta) }
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = item.icono,
                        contentDescription = item.etiqueta,
                        tint = colorContenido,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.etiqueta,
                        color = colorContenido,
                        fontSize = 11.sp,
                        fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
