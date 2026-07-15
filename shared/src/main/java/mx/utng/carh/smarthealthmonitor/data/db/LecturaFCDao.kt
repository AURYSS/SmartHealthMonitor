package mx.utng.carh.smarthealthmonitor.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturaFCDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: LecturaFC): Long

    @Upsert
    suspend fun upsert(lectura: LecturaFC)

    @Query("SELECT * FROM lecturas_fc ORDER BY timestamp DESC")
    fun obtenerTodas(): Flow<List<LecturaFC>>

    @Query ("""
        SELECT * FROM lecturas_fc
        ORDER BY timestamp DESC
        LIMIT 50""")
    fun obtenerUltimas(): Flow<List<LecturaFC>>

    @Query("UPDATE lecturas_fc SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarSincronizado(id: Long)

    @Query("SELECT * FROM lecturas_fc WHERE sincronizado = 0")
    suspend fun obtenerNoSincronizados(): List<LecturaFC>

    @Query("SELECT COUNT(*) FROM lecturas_fc")
    suspend fun contarRegistros(): Int

    @Query("""
    DELETE FROM lecturas_fc
    WHERE timestamp < :limite""")
    suspend fun limpiarViejos(limite: Long): Int
}
