package mx.utng.carh.smarthealthmonitor.data.db

import androidx.room.*

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val hora: String = "", // Se asigna en el repositorio o se formatea al mostrar
    val esNormal: Boolean = valorBpm in 60..100
)