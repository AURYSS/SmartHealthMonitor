package mx.utng.carh.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mx.utng.carh.wear.data.SmartHealthRepository

class WearDashboardViewModel : ViewModel() {
    // Reutiliza el repositorio local del módulo wear
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { value: Int -> if (value == 0) 72 else value } // valor por defecto si no hay datos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72,
        )
}
