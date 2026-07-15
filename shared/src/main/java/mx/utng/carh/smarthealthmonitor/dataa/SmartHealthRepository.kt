package mx.utng.carh.smarthealthmonitor.dataa

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mx.utng.carh.smarthealthmonitor.data.db.LecturaFC
import mx.utng.carh.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.carh.smarthealthmonitor.data.db.SmartHealthDB
import mx.utng.carh.smarthealthmonitor.data.NeonDatabaseManager

object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        val horaActual = sdf.format(java.util.Date())
        val estado = when {
            bpm > 100 -> "FC Alta"
            bpm < 60 -> "FC Baja"
            else -> "Normal"
        }

        // Guardar en Room (local)
        dao?.insertar(LecturaFC(
            bpm = bpm,
            estado = estado,
            dispositivo = "app",
            hora = horaActual,
            sincronizado = false
        ))
        
        // Guardar en Neon (remoto)
        NeonDatabaseManager.insertarLecturaFC(bpm, "app")
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerTodas() ?: emptyFlow()
}
