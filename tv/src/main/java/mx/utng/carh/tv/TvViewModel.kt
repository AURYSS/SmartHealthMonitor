package mx.utng.carh.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.carh.smarthealthmonitor.dataa.SmartHealthRepository
import mx.utng.carh.smarthealthmonitor.data.db.LecturaFC
import mx.utng.carh.tv.mqtt.MqttTvSubscriber

class TvViewModel : ViewModel() {

    private val _fc = MutableStateFlow(0)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    // Historial de lecturas desde Room DAO
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList())

    private val mqttSubscriber = MqttTvSubscriber { tvMsg ->
        // Actualizamos el flujo local para que la UI reaccione
        _fc.value = tvMsg.bpm
        
        // También podríamos guardarlo en el Room local de la TV si quisiéramos
        viewModelScope.launch {
            SmartHealthRepository.actualizarFC(tvMsg.bpm)
        }
    }

    init {
        mqttSubscriber.connect()
    }

    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}
