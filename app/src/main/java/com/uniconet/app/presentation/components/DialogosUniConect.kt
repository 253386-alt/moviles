package com.uniconet.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
// Cambiar contraseña desde Ajustes y Recuperar desde Login
// Recuadros negros como en la app. Ambos comparten validación de 4+ chars.
@Composable
fun DialogoCambiarContrasena(
    visible: Boolean,
    error: String?,
    onCancelar: () -> Unit,
    onGuardar: (actual: String, nueva: String, confirmar: String) -> Boolean
) {
    if (!visible) return
    var actual by remember { mutableStateOf("") }
    var nueva by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var mostrar by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        containerColor = Color.Black.copy(alpha = 0.92f),
        shape = RoundedCornerShape(24.dp),
        title = {
            Column {
                Text("Cambiar contraseña", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Actualiza tu clave de acceso", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        },
        text = {
            Column {
                CampoPass(label = "Contraseña actual", value = actual, onValueChange = { actual = it }, placeholder = "••••••••", mostrar = mostrar, onMostrar = { mostrar = !mostrar })
                Spacer(modifier = Modifier.height(8.dp))
                CampoPass(label = "Nueva contraseña", value = nueva, onValueChange = { nueva = it }, placeholder = "Mínimo 4 caracteres", mostrar = mostrar, onMostrar = { mostrar = !mostrar })
                Spacer(modifier = Modifier.height(8.dp))
                CampoPass(label = "Confirmar nueva contraseña", value = confirmar, onValueChange = { confirmar = it }, placeholder = "Repíte tu nueva contraseña", mostrar = mostrar, onMostrar = { mostrar = !mostrar })
                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color(0xFFFFE0E0), fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onCancelar) { Text("Cancelar", color = Color.White.copy(alpha = 0.7f)) }
                Button(
                    onClick = { if (onGuardar(actual, nueva, confirmar)) { actual = ""; nueva = ""; confirmar = "" } },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
                ) { Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            }
        }
    )
}

@Composable
fun DialogoOlvideContrasena(
    visible: Boolean,
    correo: String,
    error: String?,
    onCancelar: () -> Unit,
    onGuardar: (nueva: String, confirmar: String) -> Boolean
) {
    if (!visible) return
    var nueva by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var mostrar by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        containerColor = Color.Black.copy(alpha = 0.92f),
        shape = RoundedCornerShape(24.dp),
        title = {
            Column {
                Text("Recuperar contraseña", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Escribe una nueva contraseña para $correo", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        },
        text = {
            Column {
                CampoPass(label = "Nueva contraseña", value = nueva, onValueChange = { nueva = it }, placeholder = "Mínimo 4 caracteres", mostrar = mostrar, onMostrar = { mostrar = !mostrar })
                Spacer(modifier = Modifier.height(8.dp))
                CampoPass(label = "Confirmar nueva contraseña", value = confirmar, onValueChange = { confirmar = it }, placeholder = "Repíte tu nueva contraseña", mostrar = mostrar, onMostrar = { mostrar = !mostrar })
                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color(0xFFFFE0E0), fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onCancelar) { Text("Cancelar", color = Color.White.copy(alpha = 0.7f)) }
                Button(
                    onClick = { if (onGuardar(nueva, confirmar)) { nueva = ""; confirmar = "" } },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
                ) { Text("Actualizar", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            }
        }
    )
}

@Composable
private fun CampoPass(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, mostrar: Boolean, onMostrar: () -> Unit) {
    Column {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.4f)) },
            trailingIcon = {
                androidx.compose.material3.IconButton(onClick = onMostrar) {
                    Icon(
                        if (mostrar) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            },
            visualTransformation = if (mostrar) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CristalNegro,
                unfocusedContainerColor = CristalNegro,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AzulAccionUni,
                unfocusedBorderColor = BordeCristal
            )
        )
    }
}

// Alertas según Figma (frames 79:20, 79:33, 79:45, 79:58)
// Recuadro negro transparente como el resto de la app.
@Composable
fun AlertDialogNegro(
    visible: Boolean,
    icono: ImageVector,
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
    botonAccion: @Composable () -> Unit
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black.copy(alpha = 0.85f),
        shape = RoundedCornerShape(24.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(CristalNegro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
        },
        title = {
            Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Text(mensaje, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { botonAccion() }
        },
        dismissButton = null,
        properties = androidx.compose.ui.window.DialogProperties(dismissOnBackPress = false)
    )
}

// Alertas concretas de Figma
@Composable
fun AlertaCerrarSesion(visible: Boolean, onCancelar: () -> Unit, onConfirmar: () -> Unit) {
    AlertDialogNegro(
        visible = visible,
        icono = Icons.Default.WarningAmber,
        titulo = "¿Cerrar sesión?",
        mensaje = "Tendrás que iniciar sesión de nuevo para acceder a tus recursos.",
        onDismiss = onCancelar
    ) {
        TextButton(onClick = onCancelar) { Text("Cancelar", color = Color.White.copy(alpha = 0.7f)) }
        Button(
            onClick = onConfirmar,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
        ) { Text("Cerrar sesión", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
    }
}

@Composable
fun AlertaErrorAcceso(visible: Boolean, onReintentar: () -> Unit, onOlvide: () -> Unit = {}) {
    AlertDialogNegro(
        visible = visible,
        icono = Icons.Default.ErrorOutline,
        titulo = "Acceso denegado",
        mensaje = "Correo o contraseña incorrectos. Por favor, verifica tus datos e intenta de nuevo.",
        onDismiss = onReintentar
    ) {
        TextButton(onClick = onOlvide) { Text("¿Olvidaste tu contraseña?", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp) }
        Button(
            onClick = onReintentar,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
        ) { Text("Intentar de nuevo", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
    }
}

@Composable
fun AlertaAccesoCorrecto(visible: Boolean, onContinuar: () -> Unit) {
    AlertDialogNegro(
        visible = visible,
        icono = Icons.Default.CheckCircle,
        titulo = "¡Bienvenido!",
        mensaje = "Sesión iniciada correctamente. Ya puedes acceder a tus recursos.",
        onDismiss = onContinuar
    ) {
        Button(
            onClick = onContinuar,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Continuar", fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun AlertaSesionExpirada(visible: Boolean, onSalir: () -> Unit, onLogin: () -> Unit) {
    AlertDialogNegro(
        visible = visible,
        icono = Icons.Default.WarningAmber,
        titulo = "Sesión expirada",
        mensaje = "Tu sesión ha expirado por inactividad. Por favor, inicia sesión nuevamente para continuar.",
        onDismiss = onSalir
    ) {
        TextButton(onClick = onSalir) { Text("Salir", color = Color.White.copy(alpha = 0.7f)) }
        Button(
            onClick = onLogin,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
        ) { Text("Iniciar sesión", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
    }
}

@Composable
fun AlertaExitoGenerica(visible: Boolean, titulo: String, mensaje: String, textoBoton: String, onAceptar: () -> Unit) {
    AlertDialogNegro(
        visible = visible,
        icono = Icons.Default.Check,
        titulo = titulo,
        mensaje = mensaje,
        onDismiss = onAceptar
    ) {
        Button(
            onClick = onAceptar,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) { Text(textoBoton, fontWeight = FontWeight.Bold) }
    }
}

// Validación por correo (Figma 105:804 / 105:849): OTP de 6 dígitos
@Composable
fun DialogoValidacionCorreo(
    visible: Boolean,
    codigo: String,
    onCodigoChange: (String) -> Unit,
    onCerrar: () -> Unit,
    onReenviar: () -> Unit
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onCerrar,
        containerColor = Color.Black.copy(alpha = 0.85f),
        shape = RoundedCornerShape(24.dp),
        icon = {
            Box(
                modifier = Modifier.size(56.dp).background(CristalNegro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
        },
        title = {
            Column {
                Text("Validación por correo electrónico", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Te enviamos un código de 6 dígitos a tu correo institucional. Ingresa el código para continuar.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column {
                Text("Código de 6 dígitos", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { v -> onCodigoChange(v.filter { it.isDigit() }.take(6)) },
                    singleLine = true,
                    placeholder = { Text("••••••", color = Color.White.copy(alpha = 0.4f)) },
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CristalNegro,
                        unfocusedContainerColor = CristalNegro,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AzulAccionUni,
                        unfocusedBorderColor = BordeCristal
                    )
                )
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onCerrar) { Text("Cerrar", color = Color.White.copy(alpha = 0.7f)) }
                Button(
                    onClick = onReenviar,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White)
                ) { Text("Reenviar", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            }
        }
    )
}
