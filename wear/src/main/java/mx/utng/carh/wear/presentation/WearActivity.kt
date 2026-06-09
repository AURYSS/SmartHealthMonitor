package mx.utng.carh.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.utng.carh.wear.presentation.theme.HealthDataService

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Registrar el listener de Health Services
        lifecycleScope.launch {
            HealthDataService.registrar(applicationContext)
        }
    }
}