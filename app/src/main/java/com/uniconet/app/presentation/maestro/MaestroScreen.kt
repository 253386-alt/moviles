package com.uniconet.app.presentation.maestro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniconet.app.R
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.ui.theme.BotonPrincipal
import com.uniconet.app.ui.theme.PaletaMaestro
import com.uniconet.app.ui.theme.TextoSecundario

@Composable
fun MaestroScreen(
    viewModel: MaestroViewModel = viewModel(factory = MaestroViewModel.factory(LocalContext.current.applicationContext as android.app.Application)),
    onRegresar: () -> Unit = {},
    onLoginExitoso: () -> Unit = {}
) {
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarOlvide by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }

    FondoDegradado(paleta = PaletaMaestro) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Surface(
                onClick = onRegresar,
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {
                Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_app_1024_cuadrado),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "U n i c o n e t",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f),
                letterSpacing = 4.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Maestro",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ingresa con tu correo institucional para continuar como Maestro.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.82f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.68f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp)) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Correo institucional",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = viewModel.correo,
                        onValueChange = { viewModel.onCorreoChange(it) },
                        placeholder = { Text("Ej: nombre@institucion.edu", color = Color(0xFF8A8D98)) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, null, tint = Color(0xFF6E7180), modifier = Modifier.size(20.dp))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = camposBlancos(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Contraseña",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = viewModel.contrasena,
                        onValueChange = { viewModel.onContrasenaChange(it) },
                        placeholder = { Text("********", color = Color(0xFF8A8D98)) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, null, tint = Color(0xFF6E7180), modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                Icon(
                                    if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (mostrarContrasena) "Ocultar" else "Mostrar",
                                    tint = Color(0xFF6E7180),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = camposBlancos(),
                        visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { if (viewModel.onIniciarClick()) onLoginExitoso() }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.clickable { mostrarOlvide = true }
                        )
                    }

                    viewModel.error?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF8E2B2B).copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                color = Color.White,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (viewModel.onIniciarClick()) onLoginExitoso()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BotonPrincipal,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text("Iniciar sesión", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "O accede rápido",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {},
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.65f),
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
                modifier = Modifier.height(50.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Continuar con Google", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.size(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.buscar),
                        contentDescription = "Google",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Al continuar aceptas los Términos de uso y la Política de privacidad de Uniconet.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        com.uniconet.app.presentation.components.DialogoOlvideContrasena(
            visible = mostrarOlvide,
            correo = viewModel.correo.ifBlank { "maestro@gmail.com" },
            error = viewModel.error,
            onCancelar = { mostrarOlvide = false },
            onGuardar = { nueva, confirmar ->
                if (viewModel.recuperarContrasena(nueva, confirmar)) {
                    mostrarOlvide = false
                    mostrarExito = true
                    true
                } else false
            }
        )

        com.uniconet.app.presentation.components.AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "Contraseña actualizada",
            mensaje = "Ya puedes entrar con tu nueva contraseña.",
            textoBoton = "Aceptar",
            onAceptar = { mostrarExito = false }
        )
    }
}

@Composable
private fun camposBlancos() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color.White.copy(alpha = 0.9f),
    focusedTextColor = Color(0xFF1C1B1F),
    unfocusedTextColor = Color(0xFF1C1B1F),
    focusedBorderColor = BotonPrincipal,
    unfocusedBorderColor = Color.Transparent,
    focusedSupportingTextColor = BotonPrincipal,
    focusedLeadingIconColor = BotonPrincipal,
    unfocusedLeadingIconColor = Color(0xFF6E7180),
    focusedTrailingIconColor = BotonPrincipal,
    unfocusedTrailingIconColor = Color(0xFF6E7180)
)
