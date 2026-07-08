package mx.utng.carh.tv

import android.app.Application
import mx.utng.carh.smarthealthmonitor.dataa.SmartHealthRepository

class SmartHealthTvApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
    }
}
