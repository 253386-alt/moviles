package com.uniconet.app.network

/**
 * Configuración de la API para conectar a AWS con IP elástica.
 *
 * TODO (cuando tengas la IP elástica de AWS):
 * 1. Cambia ELASTIC_IP por tu IP real, ej: "18.123.45.67"
 * 2. Si el backend corre en https, usa https://
 * 3. Si corre en otro puerto, ej: :8080 o :5000, inclúyelo
 * 4. Termina siempre con /api/ (el Retrofit necesita la carpeta base)
 *
 * Ejemplo final:
 *   BASE_URL = "http://18.123.45.67:8080/api/"
 *   BASE_URL = "https://tu-dominio.com/api/"
 *
 * Mientras no haya servidor, la app sigue funcionando con base local
 * (DataStore) y solo intenta la API en segundo plano.
 */
object ApiConfig {
    // IP elástica de AWS (EC2, us-east-1)
    const val ELASTIC_IP: String = "52.7.137.127"
    const val PORT: String = ":8080"
    const val BASE_URL: String = "http://$ELASTIC_IP$PORT/api/"

    // Si quieres forzar HTTPS más adelante, cambia a https://
    // const val BASE_URL: String = "https://$ELASTIC_IP/api/"

    const val TIMEOUT_SECONDS: Long = 15
    const val CONNECT_TIMEOUT_SECONDS: Long = 10
    const val READ_TIMEOUT_SECONDS: Long = 15
}
