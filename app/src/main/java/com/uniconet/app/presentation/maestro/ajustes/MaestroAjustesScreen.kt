package com.uniconet.app.presentation.maestro.ajustes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniconet.app.presentation.components.AlertaCerrarSesion
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferiorMaestro
import com.uniconet.app.presentation.components.DialogoCambiarContrasena
import com.uniconet.app.presentation.components.EncabezadoConPerfil
import com.uniconet.app.presentation.components.FilaNotificaciones
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.maestro.MaestroViewModel
import com.uniconet.app.ui.theme.PaletaMaestro

// Cuenta Maestro: sin Mi perfil ni Privacidad. Solo Notificaciones + Cambiar Contraseña + Cerrar sesión.
@Composable
fun MaestroAjustesScreen(
    onRegresar: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    viewModel: MaestroViewModel = viewModel(factory = MaestroViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrarCambiar by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }

    FondoDegradado(paleta = PaletaMaestro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 110.dp)
        ) {
            EncabezadoConPerfil(
                nombre = "¡Hola, Maestro!",
                onClickPerfil = {},
                onRegresar = onRegresar
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Configuraciones", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Administra tu cuenta y ajustes personales", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaNotificaciones()
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    OpcionMaestro(icono = Icons.Default.Lock, titulo = "Cambiar Contraseña", subtitulo = "Actualiza tu clave de acceso", onClick = { mostrarCambiar = true })
                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)
                    OpcionMaestro(icono = Icons.AutoMirrored.Filled.ExitToApp, titulo = "Cerrar sesión", subtitulo = "Salir de tu cuenta actual", onClick = { mostrarConfirmacion = true })
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

        AlertaCerrarSesion(
            visible = mostrarConfirmacion,
            onCancelar = { mostrarConfirmacion = false },
            onConfirmar = { mostrarConfirmacion = false; onCerrarSesion() }
        )

        DialogoCambiarContrasena(
            visible = mostrarCambiar,
            error = viewModel.error,
            onCancelar = { mostrarCambiar = false },
            onGuardar = { actual, nueva, confirmar ->
                if (viewModel.cambiarContrasena(actual, nueva, confirmar)) {
                    mostrarCambiar = false
                    mostrarExito = true
                    true
                } else false
            }
        )

        AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "Contraseña actualizada",
            mensaje = "Tu nueva contraseña ya quedó guardada. La próxima vez entra con la nueva.",
            textoBoton = "Aceptar",
            onAceptar = { mostrarExito = false }
        )
    }
}

@Composable
private fun OpcionMaestro(icono: ImageVector, titulo: String, subtitulo: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitulo, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
    }
}
