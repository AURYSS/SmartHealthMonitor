package mx.utng.carh.wear.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import kotlinx.coroutines.launch
import mx.utng.carh.wear.data.SmartHealthRepository
import mx.utng.carh.wear.presentation.theme.HealthDataService
import mx.utng.carh.wear.presentation.theme.WearDataSender

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private lateinit var wearDataSender: WearDataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        wearDataSender = WearDataSender(this)

        // Registrar Health Services para segundo plano
        lifecycleScope.launch {
            try {
                HealthDataService.registrar(this@MainActivity)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error registrando HealthDataService", e)
            }
        }

        // Solicitar permisos al iniciar para que el sensor funcione
        checkAndRequestPermissions()

        setContent {
            MaterialTheme {
                SmartHealthWearNavGraph()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            registerSensor()
        } else {
            requestPermissions(permissions, 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            registerSensor()
        }
    }

    private fun registerSensor() {
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("MainActivity", "Sensor de ritmo cardiaco registrado")
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
            val bpm = event.values[0].toInt()
            if (bpm > 0) {
                // Actualizamos el repositorio local que observa el ViewModel
                SmartHealthRepository.actualizarFC(bpm)
                // Enviamos los datos al celular
                lifecycleScope.launch {
                    wearDataSender.enviarFC(bpm)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
