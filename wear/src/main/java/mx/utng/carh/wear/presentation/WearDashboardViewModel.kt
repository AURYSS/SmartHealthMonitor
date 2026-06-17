package mx.utng.carh.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mx.utng.carh.wear.data.SmartHealthRepository

// Clase temporal para que compile hasta tener Room en Wear
data class LecturaFC(
    val id: Int,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean
)

class WearDashboardViewModel : ViewModel() {
    // Reutiliza el repositorio local del módulo wear
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { value: Int -> if (value == 0) 72 else value } 
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72,
        )

    // Flow de historial (puedes conectarlo a Room después)
    val historial: StateFlow<List<LecturaFC>> = flowOf(
        listOf(
            LecturaFC(1, 80, "10:00", true),
            LecturaFC(2, 110, "10:15", false),
            LecturaFC(3, 75, "10:30", true)
        )
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
}
