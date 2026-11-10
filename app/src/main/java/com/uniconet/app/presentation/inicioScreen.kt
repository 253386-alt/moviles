package com.uniconet.app.presentation

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniconet.app.R

private val FondoInicioArriba = Color(0xFF747475)
private val FondoInicioMedio = Color(0xFF000000)
private val FondoInicioAbajo = Color(0xFF13387C)
private val TextoSecundarioInicio = Color(0xFFAEB8D9)

@Composable
fun InicioScreen(
    viewModel: InicioViewModel = viewModel(),
    onIrAPersonajes: () -> Unit = {},
    onIrADetalle: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FondoInicioArriba, FondoInicioMedio, FondoInicioAbajo)
                )
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x992B2F73),
                        Color.Transparent,
                        Color(0x991F5FB8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_app_1024_cuadrado),
                contentDescription = "logo",
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Uniconect",
                fontSize = 13.sp,
                color = TextoSecundarioInicio,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Quién eres?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona tu perfil para continuar.",
                fontSize = 15.sp,
                color = TextoSecundarioInicio,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            TarjetaPerfil(
                titulo = "Maestro",
                descripcion = "Gestiona clases y alumnos",
                imagen = R.drawable.maestro,
                onClick = {
                    viewModel.onBotonDosClick()
                    onIrADetalle()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TarjetaPerfil(
                titulo = "Estudiante",
                descripcion = "Accede a tus materias y recursos",
                imagen = R.drawable.alumno,
                onClick = {
                    viewModel.onBotonUnoClick()
                    onIrAPersonajes()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Al continuar aceptas los Términos de uso y la Política de privacidad de Uniconect.",
                fontSize = 11.sp,
                color = TextoSecundarioInicio,
                textAlign = TextAlign.Center
            )
        }
    }
}
private val ColorBoton = Color(0xFF1A1C24).copy(alpha = 0.7f)
private val ColorBotonBorde = Color.White.copy(alpha = 0.2f)
private val ColorCirculoFlecha = Color.White.copy(alpha = 0.15f)

@Composable
fun TarjetaPerfil(
    titulo: String,
    descripcion: String,
    imagen: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = ColorBoton,
        border = BorderStroke(1.dp, ColorBotonBorde)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imagen),
                contentDescription = titulo,
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = descripcion,
                    fontSize = 13.sp,
                    color = TextoSecundarioInicio
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(ColorCirculoFlecha, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "›", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}