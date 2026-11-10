package com.uniconet.app.network

import android.content.Context
import com.uniconet.app.data.AuthRepository
import com.uniconet.app.data.BookRepository
import com.uniconet.app.data.ForoRepository
import com.uniconet.app.data.GrupoRepository

/**
 * Contenedor manual de dependencias (sin Hilt para no complicar).
 * Guarda los repos locales y la API REST lista para AWS.
 *
 * En tu Application o MainActivity puedes hacer:
 *   val app = AppContainer(context)
 *   val api = app.api
 */
class AppContainer(context: Context) {
    val authRepository = AuthRepository(context.applicationContext)
    val bookRepository = BookRepository(context.applicationContext)
    val foroRepository = ForoRepository(context.applicationContext)
    val grupoRepository = GrupoRepository(context.applicationContext)

    // Cliente Retrofit a AWS (IP elástica). Si el servidor no responde,
    // los repos locales siguen funcionando.
    val api: UniconetApi = NetworkModule.createApi()
}
