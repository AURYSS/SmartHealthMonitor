package mx.utng.carh.smarthealthmonitor

import android.app.Application
import mx.utng.carh.smarthealthmonitor.dataa.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this) // inicializar Room
    }
}
