package com.uniconet.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.uniconet.app.ui.theme.PaletaEstudiante
import com.uniconet.app.ui.theme.PaletaMaestro
import com.uniconet.app.ui.theme.PaletaPerfil

@Composable
fun FondoDegradado(
    modifier: Modifier = Modifier,
    paleta: PaletaPerfil = PaletaEstudiante,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(paleta.arriba, paleta.medio, paleta.abajo)
                )
            )
            .background(
                Brush.horizontalGradient(
                    listOf(paleta.izquierda, Color.Transparent, paleta.derecha)
                )
            ),
        content = content
    )
}