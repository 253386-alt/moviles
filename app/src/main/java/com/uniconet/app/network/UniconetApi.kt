package com.uniconet.app.network

import com.uniconet.app.network.dto.AddComentarioRequest
import com.uniconet.app.network.dto.AnuncioDto
import com.uniconet.app.network.dto.ApiMessage
import com.uniconet.app.network.dto.BookDto
import com.uniconet.app.network.dto.ChangePasswordRequest
import com.uniconet.app.network.dto.ComentarioDto
import com.uniconet.app.network.dto.CreateGrupoRequest
import com.uniconet.app.network.dto.GrupoActionRequest
import com.uniconet.app.network.dto.GrupoDto
import com.uniconet.app.network.dto.LoginRequest
import com.uniconet.app.network.dto.LoginResponse
import com.uniconet.app.network.dto.PublishAnuncioRequest
import com.uniconet.app.network.dto.UploadBookRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Endpoints REST para Uniconet en AWS.
 * Todos cuelgan de ApiConfig.BASE_URL (ej: http://<IP>:8080/api/)
 *
 * - GET  /books
 * - POST /books
 * - DELETE /books/{id}
 *
 * - GET  /foro/anuncios
 * - POST /foro/anuncios
 * - DELETE /foro/anuncios/{id}
 * - GET  /foro/anuncios/{id}/comentarios
 * - POST /foro/anuncios/{id}/comentarios
 *
 * - GET  /grupos
 * - POST /grupos
 * - DELETE /grupos/{id}
 * - PUT  /grupos/{id}/accion
 *
 * - POST /auth/login
 * - PUT  /auth/change-password
 */
interface UniconetApi {

    // ── Auth ───────────────────────────────────────────────
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @PUT("auth/change-password")
    suspend fun changePassword(@Body body: ChangePasswordRequest): ApiMessage

    // ── Libros ─────────────────────────────────────────────
    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @POST("books")
    suspend fun uploadBook(@Body body: UploadBookRequest): BookDto

    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: String): ApiMessage

    // ── Foro ───────────────────────────────────────────────
    @GET("foro/anuncios")
    suspend fun getAnuncios(): List<AnuncioDto>

    @POST("foro/anuncios")
    suspend fun publishAnuncio(@Body body: PublishAnuncioRequest): AnuncioDto

    @DELETE("foro/anuncios/{id}")
    suspend fun deleteAnuncio(@Path("id") id: String): ApiMessage

    @GET("foro/anuncios/{id}/comentarios")
    suspend fun getComentarios(@Path("id") anuncioId: String): List<ComentarioDto>

    @POST("foro/anuncios/{id}/comentarios")
    suspend fun addComentario(@Path("id") anuncioId: String, @Body body: AddComentarioRequest): ComentarioDto

    // ── Grupos especiales ──────────────────────────────────
    @GET("grupos")
    suspend fun getGrupos(): List<GrupoDto>

    @POST("grupos")
    suspend fun createGrupo(@Body body: CreateGrupoRequest): GrupoDto

    @DELETE("grupos/{id}")
    suspend fun deleteGrupo(@Path("id") id: String): ApiMessage

    @PUT("grupos/{id}/accion")
    suspend fun grupoAccion(@Path("id") id: String, @Body body: GrupoActionRequest): GrupoDto
}
