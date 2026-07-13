package mx.utng.carh.wear.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import mx.utng.carh.wear.presentation.components.WearFCCard

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit = {},
    onHistorialClick: () -> Unit = {},
    viewModel: WearViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 32.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 32.dp
            )
        ) {
            item {
                WearFCCard(
                    fc = fc,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(
                    label = { Text("Ver Historial", modifier = Modifier.fillMaxWidth()) },
                    onClick = onHistorialClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(
                    label = { Text("Enviar Alerta", modifier = Modifier.fillMaxWidth()) },
                    onClick = onAlertClick,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colors.error,
                        contentColor = MaterialTheme.colors.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
