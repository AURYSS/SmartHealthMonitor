package mx.utng.carh.tv.data.remote

import mx.utng.carh.tv.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"

    const val AUTH_HEADER  = "Bearer ${BuildConfig.NEON_API_KEY}"
    const val CONN_STRING  = "postgresql://neondb_owner:npg_dDpzv0jhPT1r@${BuildConfig.NEON_HOST}/neondb?sslmode=require"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build())
            .build()
            .create(NeonApiService::class.java)
    }
}
