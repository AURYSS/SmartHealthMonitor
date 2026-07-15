package mx.utng.carh.smarthealthmonitor.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

object NeonDatabaseManager {
    private const val TAG = "NeonDatabase"
    
    // Configuración desde tu string de conexión
    private const val URL = "jdbc:postgresql://ep-tiny-waterfall-att7q8lb-pooler.c-9.us-east-1.aws.neon.tech:5432/neondb"
    private const val USER = "neondb_owner"
    private const val PASSWORD = "npg_dDpzv0jhPT1r"

    init {
        // Forzar la carga del driver en Android
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "Driver de PostgreSQL no encontrado", e)
        }
    }

    private suspend fun getConnection(): Connection? = withContext(Dispatchers.IO) {
        return@withContext try {
            // Ponemos todos los parámetros en la URL para evitar usar el objeto Properties
            val fullUrl = "$URL?user=$USER&password=$PASSWORD&ssl=true&sslmode=require"
            DriverManager.getConnection(fullUrl)
        } catch (e: Throwable) { 
            Log.e(TAG, "❌ Fallo de conexión (Neon): ${e.message}")
            null
        }
    }

    /**
     * Inserta una lectura en la tabla 'lecturas_fc' con el esquema oficial
     */
    suspend fun insertarLecturaFC(bpm: Int, dispositivo: String = "app") = withContext(Dispatchers.IO) {
        val conn = getConnection() ?: return@withContext
        try {
            // Ajustado a tu esquema: bpm, estado, dispositivo, hora
            val sql = "INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora) VALUES (?, ?, ?, ?)"
            val statement = conn.prepareStatement(sql)
            
            // Lógica para el estado
            val estado = when {
                bpm > 100 -> "FC Alta"
                bpm < 60 -> "FC Baja"
                else -> "Normal"
            }
            
            // Formatear hora (HH:mm)
            val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            val horaActual = sdf.format(java.util.Date())
            
            statement.setInt(1, bpm)
            statement.setString(2, estado)
            statement.setString(3, dispositivo)
            statement.setString(4, horaActual)
            
            val rowsInserted = statement.executeUpdate()
            if (rowsInserted > 0) {
                Log.d(TAG, "✅ Guardado en Neon (lecturas_fc): $bpm bpm | $dispositivo")
            }
            statement.close()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error insertando en Neon: ${e.message}")
        } finally {
            conn.close()
        }
    }
}
