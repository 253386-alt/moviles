package com.uniconet.app.network

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Conexión a la API REST (Retrofit + OkHttp) lista para AWS Elastic IP.
 *
 * - Timeouts configurables en ApiConfig
 * - Logging BODY en debug (solo visible en logcat)
 * - Json con kotlinx.serialization
 *
 * Uso:
 *   val api = NetworkModule.createApi()
 *   val libros = api.getBooks()
 */
object NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val authInterceptor = Interceptor { chain ->
        // Aquí puedes leer tu token de DataStore y agregarlo:
        // val token = "Bearer tu-token"
        // val request = chain.request().newBuilder().addHeader("Authorization", token).build()
        chain.proceed(chain.request())
    }

    private fun okHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    fun createRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttp())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    // Crea la API lista para inyectar en ViewModels/Repos
    fun createApi(): UniconetApi = createRetrofit().create(UniconetApi::class.java)
}

/**
 * Utilidad para llamar a la API y caer a base local si no hay servidor.
 * Ejemplo sin excepción:
 *
 *   val resultado = ApiCall.tryApi { api.getBooks() }
 *   val libros = resultado ?: localBooks
 */
object ApiCall {
    suspend fun <T> tryApi(block: suspend () -> T): T? = runCatching { block() }.getOrNull()

    suspend fun <T> tryApiOrNull(block: suspend () -> T): Result<T> = runCatching { block() }
}
