package com.uniconet.app.presentation.estudiante.validar

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferior
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.grupos.GruposViewModel
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.NegroSeccion
import com.uniconet.app.ui.theme.NegroTarjeta
import com.uniconet.app.ui.theme.PaletaEstudiante
import com.uniconet.app.ui.theme.TextoSecundario

// Grupo-especial Estudiante según Figma (43:642) - Funcional con base local.
// Campos arreglados: vacíos con placeholder (no con texto fijo), con iconos y validación.
@Composable
fun ValidarScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    onIrAAjustes: () -> Unit = {},
    viewModel: GruposViewModel = viewModel(factory = GruposViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    var busqueda by remember { mutableStateOf("") }

    // Campos del formulario en vacío para que el placeholder se vea
    var nombre by remember { mutableStateOf("") }
    var integrantes by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var maestro by remember { mutableStateOf("") }

    var mostrarCalendario by remember { mutableStateOf(false) }

    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarEliminado by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val grupos by viewModel.grupos.collectAsState()
    val filtrados = grupos.filter {
        it.nombre.contains(busqueda, ignoreCase = true) ||
                it.descripcion.contains(busqueda, ignoreCase = true) ||
                it.maestro.contains(busqueda, ignoreCase = true)
    }

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
                IconButton(onClick = onRegresar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar grupos, temas o maestros", color = TextoSecundario) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = TextoSecundario) },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            SeccionCristal {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Pildora("Crear Grupo Especial", fondo = Color.White, colorTexto = AzulAccionUni)
                    Pildora("Filtro", fondo = CristalNegro, colorTexto = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TarjetaInterna {
                    // Fila 1: Nombre (ancho completo) — requerido
                    CampoEditable(
                        label = "Nombre del grupo *",
                        value = nombre,
                        onValueChange = { nombre = it; error = null },
                        placeholder = "Ej: Grupo especial física",
                        leadingIcon = { Icon(Icons.Default.Groups, null, tint = TextoSecundario, modifier = Modifier.size(18.dp)) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fila 2: Integrantes (como Figma "20/30") + Fecha con calendario (solo selección)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CampoEditable(
                            label = "Integrantes",
                            value = integrantes,
                            onValueChange = { integrantes = it },
                            placeholder = "Ej: 20/30",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number,
                            leadingIcon = { Icon(Icons.Default.Groups, null, tint = TextoSecundario, modifier = Modifier.size(18.dp)) }
                        )
                        CampoFecha(
                            value = fecha,
                            onClick = { mostrarCalendario = true },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fila 3: Información (descripción) ancho completo
                    CampoEditable(
                        label = "Información",
                        value = info,
                        onValueChange = { info = it },
                        placeholder = "Sin datos registrados",
                        leadingIcon = { Icon(Icons.Default.Info, null, tint = TextoSecundario, modifier = Modifier.size(18.dp)) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fila 4: Maestro ancho completo
                    CampoEditable(
                        label = "Maestro",
                        value = maestro,
                        onValueChange = { maestro = it },
                        placeholder = "Sin asignación registrada",
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = TextoSecundario, modifier = Modifier.size(18.dp)) }
                    )

                    error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = Color(0xFFFFE0E0), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    BotonAccion("Crear grupo") {
                        if (nombre.isBlank()) {
                            error = "Escribe el nombre del grupo para crear"
                        } else {
                            error = null
                            viewModel.crear(
                                nombre = nombre.trim(),
                                integrantes = integrantes.trim(),
                                descripcion = info.trim(),
                                fecha = fecha.trim(),
                                maestro = maestro.trim()
                            )
                            // Limpiar todo el formulario
                            nombre = ""
                            integrantes = ""
                            info = ""
                            fecha = ""
                            maestro = ""
                            mostrarExito = true
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SeccionCristal {
                Pildora("Grupos especiales", fondo = Color.White, colorTexto = AzulAccionUni)
                if (filtrados.isEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Todavía no hay grupos. Crea el primero arriba.", color = TextoSecundario, fontSize = 13.sp)
                }
                filtrados.forEach { grupo ->
                    Spacer(modifier = Modifier.height(12.dp))
                    TarjetaInterna {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Pildora(grupo.nombre, fondo = CristalNegro, colorTexto = Color.White)
                            Text(grupo.estado, color = Color.White, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(grupo.descripcion.ifBlank { "Información del grupo pendiente." }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CampoInfo("Integrantes", grupo.integrantes.ifBlank { "Pendiente" }, Modifier.weight(1f))
                            CampoInfo("Fecha", grupo.fecha.ifBlank { "Pendiente de definir" }, Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        CampoInfo("Maestro", grupo.maestro.ifBlank { "Sin asignación registrada" }, Modifier.fillMaxWidth())

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            onClick = {
                                viewModel.eliminar(grupo.id)
                                mostrarEliminado = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = CristalNegro,
                            border = BorderStroke(1.dp, BordeCristal),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Eliminar grupo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        BarraInferior(
            seleccionado = "Validar",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "Grupo fue creado con éxito",
            mensaje = "Tu grupo especial ya está listo para continuar.",
            textoBoton = "Entendido",
            onAceptar = { mostrarExito = false }
        )
        AlertaExitoGenerica(
            visible = mostrarEliminado,
            titulo = "Grupo Fue Eliminado con éxito",
            mensaje = "El grupo se eliminó de tu lista local.",
            textoBoton = "Entendido",
            onAceptar = { mostrarEliminado = false }
        )

        if (mostrarCalendario) {
            DialogoFecha(
                onDismiss = { mostrarCalendario = false },
                onFechaElegida = { fechaElegida ->
                    fecha = fechaElegida
                    mostrarCalendario = false
                }
            )
        }
    }
}

@Composable
private fun SeccionCristal(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = NegroSeccion, border = BorderStroke(1.dp, BordeCristal)) {
        Column(modifier = Modifier.padding(14.dp)) { content() }
    }
}

@Composable
private fun TarjetaInterna(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = NegroTarjeta, border = BorderStroke(1.dp, BordeCristal)) {
        Column(modifier = Modifier.padding(12.dp)) { content() }
    }
}

@Composable
private fun Pildora(titulo: String, fondo: Color, colorTexto: Color) {
    Surface(shape = RoundedCornerShape(20.dp), color = fondo) {
        Text(text = titulo, color = colorTexto, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

@Composable
private fun CampoInfo(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = CristalNegro, border = BorderStroke(1.dp, BordeCristal)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(titulo, color = TextoSecundario, fontSize = 11.sp)
            Text(valor, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoFecha(onDismiss: () -> Unit, onFechaElegida: (String) -> Unit) {
    val state = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        val date = java.util.Date(millis)
                        val fmt = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        fmt.timeZone = java.util.TimeZone.getTimeZone("UTC")
                        onFechaElegida(fmt.format(date))
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text("Aceptar", color = AzulAccionUni, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
                }
                if (state.selectedDateMillis != null) {
                    TextButton(onClick = { onFechaElegida("") }) {
                        Text("Limpiar", color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }
    ) {
        DatePicker(
            state = state,
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = Color(0xFF1E1E24),
                titleContentColor = Color.White,
                headlineContentColor = Color.White,
                weekdayContentColor = Color.White.copy(alpha = 0.6f),
                subheadContentColor = Color.White.copy(alpha = 0.7f),
                dayContentColor = Color.White,
                selectedDayContainerColor = AzulAccionUni,
                selectedDayContentColor = Color.White,
                todayContentColor = Color.White
            ),
            showModeToggle = false
        )
    }
}

@Composable
private fun CampoFecha(value: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Fecha", color = TextoSecundario, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            color = CristalNegro,
            border = BorderStroke(1.dp, BordeCristal),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (value.isBlank()) "Pendiente de definir" else value,
                    color = if (value.isBlank()) Color.White.copy(alpha = 0.5f) else Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                Text("📅", fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CampoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(label, color = TextoSecundario, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp) },
            leadingIcon = leadingIcon,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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

@Composable
private fun BotonAccion(texto: String, onClick: () -> Unit) {
    Button(onClick = onClick, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}
