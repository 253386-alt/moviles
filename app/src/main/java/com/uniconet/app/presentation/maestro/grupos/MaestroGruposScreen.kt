package com.uniconet.app.presentation.maestro.grupos

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.uniconet.app.presentation.components.AlertaExitoGenerica
import com.uniconet.app.presentation.components.BarraInferiorMaestro
import com.uniconet.app.presentation.components.FondoDegradado
import com.uniconet.app.presentation.components.PildoraUniConect
import com.uniconet.app.presentation.components.SeccionNegra
import com.uniconet.app.presentation.components.TarjetaNegra
import com.uniconet.app.presentation.grupos.GruposViewModel
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.PaletaMaestro
import com.uniconet.app.ui.theme.TextoSecundario

// Grupos_Especiales Maestro según Figma (105:722) - Funcional con base local.
// Maestro acepta/deniega solicitudes creadas por Estudiantes.
@Composable
fun MaestroGruposScreen(
    onRegresar: () -> Unit = {},
    onNavegar: (String) -> Unit = {},
    viewModel: GruposViewModel = viewModel(factory = GruposViewModel.factory(LocalContext.current.applicationContext as android.app.Application))
) {
    var mostrarExito by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf("") }

    val grupos by viewModel.grupos.collectAsState()
    val pendientes = grupos.filter { it.estado.contains("Pendiente", ignoreCase = true) }

    FondoDegradado(paleta = PaletaMaestro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 110.dp)
        ) {
            Column {
                Text("Uniconet", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, letterSpacing = 2.sp)
                Text("Grupos especiales", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Revisa las solicitudes pendientes y decide si aceptar o denegar el acceso.",
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
                    Text("Solicitudes pendientes", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    PildoraUniConect("${pendientes.size} solicitudes", Color.White, AzulAccionUni)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (pendientes.isEmpty()) {
                    Text("No tienes solicitudes pendientes. 🎉", color = TextoSecundario, fontSize = 13.sp)
                } else {
                    pendientes.forEach { s ->
                        TarjetaNegra {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(44.dp).background(CristalNegro, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(s.nombre.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(s.nombre, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("${s.integrantes} · ${s.fecha}", color = TextoSecundario, fontSize = 12.sp)
                                }
                                PildoraUniConect("Pendiente", CristalNegro, Color.White)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(s.descripcion, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Maestro: ${s.maestro}", color = TextoSecundario, fontSize = 11.sp)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.aceptar(s.id)
                                        mensajeExito = "\"${s.nombre}\" fue aceptado. El grupo puede continuar."
                                        mostrarExito = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulAccionUni, contentColor = Color.White),
                                    modifier = Modifier.weight(1f).height(44.dp)
                                ) {
                                    Text("Aceptar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Surface(
                                    onClick = {
                                        viewModel.denegar(s.id)
                                        mensajeExito = "\"${s.nombre}\" fue denegado."
                                        mostrarExito = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = CristalNegro,
                                    border = BorderStroke(1.dp, BordeCristal),
                                    modifier = Modifier.weight(1f).height(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("Denegar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Historial rápido de revisados
                val revisados = grupos.filter { it.estado == "Aceptado" || it.estado == "Denegado" }
                if (revisados.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Revisados", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    revisados.forEach { r ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(r.nombre, color = Color.White, fontSize = 13.sp)
                            PildoraUniConect(r.estado, CristalNegro, Color.White)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        BarraInferiorMaestro(
            seleccionado = "Grupos",
            onItemClick = onNavegar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        AlertaExitoGenerica(
            visible = mostrarExito,
            titulo = "Acción registrada",
            mensaje = mensajeExito,
            textoBoton = "Entendido",
            onAceptar = { mostrarExito = false }
        )
    }
}
