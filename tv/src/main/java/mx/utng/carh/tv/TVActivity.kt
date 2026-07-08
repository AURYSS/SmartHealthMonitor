package mx.utng.carh.tv

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class TVActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthTvTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "catalog") {
                    composable("catalog") {
                        TvCatalogScreen(onCardClick = { lecturaId ->
                            navController.navigate("detail/$lecturaId")
                        })
                    }
                    composable(
                        route = "detail/{lecturaId}",
                        arguments = listOf(navArgument("lecturaId") { type = NavType.IntType })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("lecturaId") ?: return@composable
                        TvDetailScreen(lecturaId = id, navController = navController)
                    }
                    composable("playback") {
                        TvPlaybackScreen(navController = navController)
                    }
                }
            }
        }
    }
