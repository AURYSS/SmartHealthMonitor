package mx.utng.carh.tv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel()
) {
    val lecturas by viewModel.historial.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B4A))
            .padding(48.dp)
    ) {
        Text(
            text = "SmartHealth Monitor",
            style = MaterialTheme.typography.displaySmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Fila 1: Estado Actual (Última lectura de Room)
        Text("Estado Actual", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val ultimaLectura = lecturas.firstOrNull()
            item {
                Surface(
                    onClick = { ultimaLectura?.let { onCardClick(it.id) } },
                    modifier = Modifier.size(240.dp, 140.dp),
                    colors = ClickableSurfaceDefaults.colors(
                        containerColor = if (ultimaLectura?.esNormal == false) Color(0xFFB3261E) else Color(0xFF1B4F8A)
                    )
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                        Text(
                            text = if (ultimaLectura != null) "${ultimaLectura.valorBpm} bpm" else "-- bpm",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Text(
                            text = if (ultimaLectura != null) "Última: ${ultimaLectura.hora}" else "Sin datos",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Fila 2: Historial
        Text("Historial FC", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(lecturas) { lectura ->
                Surface(
                    onClick = { onCardClick(lectura.id) },
                    modifier = Modifier.size(200.dp, 120.dp),
                    colors = ClickableSurfaceDefaults.colors(
                        containerColor = if (lectura.esNormal) Color(0xFF1B4F8A) else Color(0xFFB3261E)
                    )
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                        Text("${lectura.valorBpm} bpm", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Text(lectura.hora, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.7f))
                    }
                }
            }
        }
    }
}
