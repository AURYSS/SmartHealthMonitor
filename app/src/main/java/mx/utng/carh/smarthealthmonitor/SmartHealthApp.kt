package mx.utng.carh.smarthealthmonitor

import android.app.Application
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.utng.carh.smarthealthmonitor.dataa.SmartHealthRepository
import mx.utng.carh.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApp : Application() {
    private val applicationScope = MainScope()
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        // 1. Inicializar el Repositorio (Room)
        SmartHealthRepository.init(this)

        // 2. Inicializar MQTT con callback para actualizar el Repo
        mqttService = MqttAppService { bpm ->
            applicationScope.launch {
                SmartHealthRepository.actualizarFC(bpm)
            }
        }
        mqttService.connect()
    }
}

