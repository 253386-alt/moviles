package com.uniconet.app.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta base histórica (se mantiene para compatibilidad)
val FondoArriba = Color(0xFFB5444A)
val FondoMedio = Color(0xFFE2402A)
val FondoAbajo = Color(0xFF6A5AA8)
val TextoSecundario = Color(0xFFF0DADA)
val BotonPrincipal = Color(0xFF5B5BE6)

val AzulUni = Color(0xFF4A5D8F)
val MoradoUni = Color(0xFF6A3FB5)
val RojoUni = Color(0xFFC0392B)
val FondoClaro = Color(0xFFF5F6FA)
val TextoOscuro = Color(0xFF1C1B1F)

val AzulUniClaro = Color(0xFF9FB1E0)
val MoradoUniClaro = Color(0xFFD0BCFF)
val RojoUniClaro = Color(0xFFFFB4AB)

// ─────────────────────────────────────────────────────────────
// SISTEMA UNICONECT - COLORES NEGROS (como en tus archivos)
// Todos los recuadros / tarjetas usan estas variantes negras
// ─────────────────────────────────────────────────────────────

/** Barra inferior */
val NegroBarra = Color.Black.copy(alpha = 0.85f)

/** Sección principal (ej: Tablón de Anuncios, Grupos, Libros) */
val NegroSeccion = Color.Black.copy(alpha = 0.70f)

/** Tarjeta interna dentro de una sección */
val NegroTarjeta = Color.Black.copy(alpha = 0.50f)

/** Tarjeta de login / tarjeta cristal (EstudianteScreen) */
val NegroLogin = Color.Black.copy(alpha = 0.80f)

/** Botón secundario oscuro (Google) */
val NegroBotonSecundario = Color.Black.copy(alpha = 0.80f)

/** Cristal sólido oscuro (#26262B usado en chips y campos) */
val CristalNegro = Color(0xFF26262B)

/** Borde sutil blanco sobre negro */
val BordeCristal = Color.White.copy(alpha = 0.25f)
val BordeCristalSuave = Color.White.copy(alpha = 0.12f)

// Acciones / acentos (no son fondos de recuadros)
val AzulAccionUni = Color(0xFF2E6FBF)
val RojoUrgenteUni = Color(0xFFE53935)
val VerdeEventoUni = Color(0xFF4CAF50)
