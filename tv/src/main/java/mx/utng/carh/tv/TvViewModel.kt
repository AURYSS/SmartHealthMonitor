package mx.utng.carh.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.carh.smarthealthmonitor.data.db.LecturaFC
import mx.utng.carh.tv.data.TvNeonRepository
import mx.utng.carh.tv.data.remote.LecturaFcDto

data class TvUiState(
    val lecturas: List<LecturaFC> = emptyList(),
    val estadisticas: List<LecturaFC> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

fun LecturaFcDto.toLecturaFC(): LecturaFC {
    return LecturaFC(
        id = this.id,
        bpm = this.bpm,
        estado = this.estado,
        dispositivo = this.dispositivo,
        hora = this.hora,
        sincronizado = true
    )
}

class TvViewModel(private val context: Context) : ViewModel() {
    private val neonRepo = TvNeonRepository()
    private val _state   = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init { cargarDatos() }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading=true) }
            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val stats    = neonRepo.obtenerEstadisticas()
                _state.update { it.copy(
                    lecturas  = lecturas.map { it.toLecturaFC() },
                    estadisticas = stats.map { it.toLecturaFC() },
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(error=e.message, isLoading=false) }
            }
        }
    }
    fun refresh() = cargarDatos()
}
