package com.uniconet.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uniconet.app.presentation.InicioScreen
import com.uniconet.app.presentation.estudiante.EstudianteScreen
import com.uniconet.app.presentation.estudiante.ajustes.EstudianteAjustesScreen
import com.uniconet.app.presentation.estudiante.home.EstudianteHomeScreen
import com.uniconet.app.presentation.estudiante.libros.LibrosScreen
import com.uniconet.app.presentation.estudiante.resenas.ResenasScreen
import com.uniconet.app.presentation.estudiante.validar.ValidarScreen
import com.uniconet.app.presentation.maestro.MaestroScreen
import com.uniconet.app.presentation.maestro.ajustes.MaestroAjustesScreen
import com.uniconet.app.presentation.maestro.biblioteca.MaestroBibliotecaScreen
import com.uniconet.app.presentation.maestro.grupos.MaestroGruposScreen
import com.uniconet.app.presentation.maestro.home.MaestroHomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {

        composable("inicio") {
            InicioScreen(
                onIrAPersonajes = { navController.navigate("estudiante") },
                onIrADetalle = { navController.navigate("maestro") }
            )
        }

        // ---------- ESTUDIANTE (Figma: Inicio/Libros/Validar/Maestros) ----------
        composable("estudiante") {
            EstudianteScreen(
                onRegresar = { navController.popBackStack() },
                onLoginExitoso = { navController.navigate("estudiante_home") }
            )
        }

        composable("estudiante_home") {
            EstudianteHomeScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarEstudiante(destino) },
                onIrAAjustes = { navController.navigate("estudiante_ajustes") }
            )
        }

        composable("estudiante_libros") {
            LibrosScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarEstudiante(destino) },
                onIrAAjustes = { navController.navigate("estudiante_ajustes") }
            )
        }

        composable("estudiante_validar") {
            ValidarScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarEstudiante(destino) },
                onIrAAjustes = { navController.navigate("estudiante_ajustes") }
            )
        }

        // En Figma la pestaña "Maestros" es la pantalla de Reseñas (43:827)
        composable("estudiante_maestros") {
            ResenasScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarEstudiante(destino) },
                onIrAAjustes = { navController.navigate("estudiante_ajustes") }
            )
        }

        // ---------- MAESTRO (Figma: Inicio/Biblioteca/Grupos) ----------
        composable("maestro") {
            MaestroScreen(
                onRegresar = { navController.popBackStack() },
                onLoginExitoso = { navController.navigate("maestro_home") }
            )
        }

        composable("maestro_home") {
            MaestroHomeScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarMaestro(destino) },
                onIrAAjustes = { navController.navigate("maestro_ajustes") }
            )
        }

        composable("maestro_biblioteca") {
            MaestroBibliotecaScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarMaestro(destino) }
            )
        }

        composable("maestro_grupos") {
            MaestroGruposScreen(
                onRegresar = { navController.popBackStack() },
                onNavegar = { destino -> navController.navegarMaestro(destino) }
            )
        }

        composable("estudiante_ajustes") {
            EstudianteAjustesScreen(
                onRegresar = { navController.popBackStack() },
                onCerrarSesion = {
                    navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } }
                },
                onNavegar = { destino -> navController.navegarEstudiante(destino) }
            )
        }

        composable("maestro_ajustes") {
            MaestroAjustesScreen(
                onRegresar = { navController.popBackStack() },
                onCerrarSesion = {
                    navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } }
                },
                onNavegar = { destino -> navController.navegarMaestro(destino) }
            )
        }
    }
}

// Barra Estudiante: Inicio / Libros / Maestro(Reseñas) / Validar
private fun rutaEstudiante(item: String) = when (item.lowercase()) {
    "inicio"  -> "estudiante_home"
    "libros"  -> "estudiante_libros"
    "maestro" -> "estudiante_maestros"
    "validar" -> "estudiante_validar"
    else      -> "estudiante_home"
}

private fun NavController.navegarEstudiante(item: String) {
    val destino = rutaEstudiante(item)
    navigate(destino) {
        popUpTo("estudiante_home") { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

// Barra Maestro según Figma: Inicio / Biblioteca / Grupos
private fun rutaMaestro(item: String) = when (item.lowercase()) {
    "inicio"     -> "maestro_home"
    "biblioteca" -> "maestro_biblioteca"
    "libros"     -> "maestro_biblioteca"
    "grupos"     -> "maestro_grupos"
    else         -> "maestro_home"
}

private fun NavController.navegarMaestro(item: String) {
    val destino = rutaMaestro(item)
    navigate(destino) {
        popUpTo("maestro_home") { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
