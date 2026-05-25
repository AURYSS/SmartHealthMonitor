package mx.utng.carh.smarthealthmonitor

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.platform.LocalConfiguration


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartHealthMonitorTheme {

                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        onLoginSuccess = {
                            // TODO sesión 5: navegar al Dashboard
                            Log.d("SmartHealth", "Login exitoso")
                        }
                    )
                }
            }
        }
    }

@Composable
fun SmartHealthScreen(
    isLandscape: Boolean,
    nombre: String = "Juan García",
    frecuenciaCardiaca: Int = 78,
    pasos: Int = 4250
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            if (isLandscape) {

                // LANDSCAPE
                LandscapeLayout(
                    nombre = nombre,
                    frecuenciaCardiaca = frecuenciaCardiaca,
                    pasos = pasos
                )

            } else {

                // PORTRAIT
                PortraitLayout(
                    nombre = nombre,
                    frecuenciaCardiaca = frecuenciaCardiaca,
                    pasos = pasos
                )
            }
        }
    }
}

@Composable
fun PortraitLayout(
    nombre: String,
    frecuenciaCardiaca: Int,
    pasos: Int
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // Parte superior
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // Icono perfil
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre
            Text(
                text = nombre,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Frecuencia cardíaca
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Frecuencia cardíaca",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$frecuenciaCardiaca bpm",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pasos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.DirectionsWalk,
                    contentDescription = "Pasos",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "%,d pasos".format(pasos),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Botón alerta
        Button(
            onClick = {

                // Acción alerta

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Alerta",
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "ENVIAR ALERTA",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LandscapeLayout(
    nombre: String,
    frecuenciaCardiaca: Int,
    pasos: Int
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // Parte superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),

            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Perfil
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Datos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Frecuencia",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "$frecuenciaCardiaca bpm",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.DirectionsWalk,
                        contentDescription = "Pasos",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "%,d pasos".format(pasos),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Botón alerta
        Button(
            onClick = {

                // Acción alerta

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Alerta",
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "ENVIAR ALERTA",
                fontSize = 14.sp
            )
        }
    }
}

@Preview(
    name = "Portrait",
    showBackground = true,
    device = "id:pixel_6"
)
@Composable
fun PreviewPortrait() {

    SmartHealthMonitorTheme {
        SmartHealthScreen(isLandscape = false)
    }
}

@Preview(
    name = "Landscape",
    showBackground = true,
    device = "id:pixel_5"
)
@Composable
fun PreviewLandscape() {

    SmartHealthMonitorTheme {
        SmartHealthScreen(isLandscape = true)
    }
}

    @Preview(name = "Login - Light", showBackground = true,
        showSystemUi = true, device = "id:pixel_6")
    @Preview(name = "Login - Dark", showBackground = true,
        uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
    @Preview(name = "Login - Big Font", showBackground = true,
        fontScale = 1.5f)
    @Composable
    private fun LoginScreenPreview() {
        SmartHealthMonitorTheme {
            LoginScreen()
        }
    }

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ThemePreview() {
    SmartHealthMonitorTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "SmartHelth Monitor",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}
}
