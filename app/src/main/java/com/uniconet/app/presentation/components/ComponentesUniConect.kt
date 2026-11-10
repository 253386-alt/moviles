package com.uniconet.app.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.NegroSeccion
import com.uniconet.app.ui.theme.NegroTarjeta

// ─────────────────────────────────────────────────────────────
// SISTEMA DE DISEÑO UNICONECT
// Recuadros en negro/transparente como en tus archivos actuales.
// Fondo degradado => azul (Maestro) o rojo/morado (Estudiante).
// ─────────────────────────────────────────────────────────────

@Composable
fun EncabezadoConPerfil(
    nombre: String,
    onClickPerfil: () -> Unit = {},
    onRegresar: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            onClick = onClickPerfil,
            shape = RoundedCornerShape(30.dp),
            color = CristalNegro,
            border = BorderStroke(1.dp, BordeCristal)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        IconButton(onClick = onRegresar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun BuscadorUniConect(
    valor: String,
    onValorChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.6f)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.White.copy(alpha = 0.6f)
            )
        },
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
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SeccionNegra(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = NegroSeccion,
        border = BorderStroke(1.dp, BordeCristal)
    ) {
        Column(modifier = Modifier.padding(14.dp)) { content() }
    }
}

@Composable
fun TarjetaNegra(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = NegroTarjeta,
        border = BorderStroke(1.dp, BordeCristal)
    ) {
        Column(modifier = Modifier.padding(12.dp)) { content() }
    }
}

@Composable
fun PildoraUniConect(titulo: String, fondo: Color, colorTexto: Color) {
    Surface(shape = RoundedCornerShape(20.dp), color = fondo) {
        Text(
            text = titulo,
            color = colorTexto,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PildoraIconoUniConect(titulo: String, fondo: Color, colorTexto: Color, icono: ImageVector) {
    Surface(shape = RoundedCornerShape(20.dp), color = fondo) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorTexto,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = titulo,
                color = colorTexto,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PildoraInteractivaUniConect(titulo: String, activa: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (activa) Color.White else CristalNegro,
        border = BorderStroke(1.dp, BordeCristal)
    ) {
        Text(
            text = titulo,
            color = if (activa) AzulAccionUni else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun BotonAccionUniConect(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulAccionUni,
            contentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CampoInfoUniConect(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = CristalNegro,
        border = BorderStroke(1.dp, BordeCristal)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(titulo, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
            Text(valor, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}
