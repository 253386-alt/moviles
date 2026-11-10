package com.uniconet.app.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.core.content.ContextCompat
import com.uniconet.app.data.NotificacionHelper

// Opción de Notificaciones con permiso real y prueba local.
// Pide POST_NOTIFICATIONS en Android 13+ y muestra notificación de prueba.
@Composable
fun FilaNotificaciones() {
    val context = LocalContext.current
    var permisoOtorgado by remember { mutableStateOf(NotificacionHelper.tienePermiso(context)) }
    var activadas by remember { mutableStateOf(permisoOtorgado) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permisoOtorgado = granted
        activadas = granted
        if (granted) NotificacionHelper.mostrarPrueba(context)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Notificaciones", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(
                if (permisoOtorgado) "Recibirás avisos del Foro, Biblioteca y Grupos" else "Activa para recibir avisos del Foro y Biblioteca",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
        Switch(
            checked = activadas,
            onCheckedChange = { nuevo ->
                if (nuevo) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            activadas = true
                            NotificacionHelper.mostrarPrueba(context)
                        } else {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        activadas = true
                        NotificacionHelper.mostrarPrueba(context)
                    }
                } else {
                    activadas = false
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
    }

    permisoOtorgado.let { otorgado ->
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if (otorgado) "Permiso concedido ✓ (toca el switch para recibir prueba)"
            else "Permiso no concedido. Activa el interruptor para permitir.",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
        if (otorgado) {
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextButton(onClick = { NotificacionHelper.mostrarPrueba(context) }) {
                Text("Enviar notificación de prueba", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
