package com.example.photobasedtextrpg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photobasedtextrpg.ui.ending.EndingScreen
import com.example.photobasedtextrpg.ui.ending.EndingUiState
import com.example.photobasedtextrpg.ui.game.GameScreen
import com.example.photobasedtextrpg.ui.game.GameUiState
import com.example.photobasedtextrpg.ui.history.HistoryScreen
import com.example.photobasedtextrpg.ui.history.HistoryUiState
import com.example.photobasedtextrpg.ui.photopicker.PhotoPickerScreen
import com.example.photobasedtextrpg.ui.photopicker.PhotoPickerUiState
import com.example.photobasedtextrpg.ui.start.StartScreen
import com.example.photobasedtextrpg.ui.worldgen.WorldGenScreen
import com.example.photobasedtextrpg.ui.worldgen.WorldGenUiState
import kotlinx.serialization.Serializable

// ── ルート定義 ────────────────────────────────────────────────────────────────

@Serializable object StartRoute
@Serializable object PhotoPickerRoute
@Serializable data class WorldGenRoute(val imageUri: String)
@Serializable data class GameRoute(val worldSettingId: String)
@Serializable data class EndingRoute(val gameStateId: String)
@Serializable object HistoryRoute

// ── NavGraph ──────────────────────────────────────────────────────────────────

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = StartRoute,
        modifier = modifier
    ) {
        // Phase 5: StartScreen
        composable<StartRoute> {
            StartScreen(
                onCameraResult = { uri ->
                    navController.navigate(WorldGenRoute(uri.toString()))
                },
                onSelectPhoto = {
                    navController.navigate(PhotoPickerRoute)
                },
                onHistory = {
                    navController.navigate(HistoryRoute)
                }
            )
        }

        // Phase 7: PhotoPickerScreen
        composable<PhotoPickerRoute> {
            PhotoPickerScreen(
                uiState = PhotoPickerUiState.Idle,
                onPhotoSelected = { uri ->
                    navController.navigate(WorldGenRoute(uri.toString()))
                },
                onStartAdventure = { uri ->
                    navController.navigate(WorldGenRoute(uri.toString()))
                }
            )
        }

        // Phase 9: WorldGenScreen
        composable<WorldGenRoute> { backStackEntry ->
            WorldGenScreen(
                uiState = WorldGenUiState.Idle(selectedGenre = null) ,
                onGenreSelected = { genre ->
                    navController.navigate(GameRoute(genre.toString()))
                },
                onRetry = {},
                onNavigateToGame = { worldSettingId ->
                    navController.navigate(GameRoute(worldSettingId))
                }
            )
        }

        // Phase 11: GameScreen
        composable<GameRoute> { backStackEntry ->
            GameScreen(
                uiState = GameUiState.Loading ,
                onChoiceSelected = {},
                onFreeInput = {}
            )
        }

        // Phase 13: EndingScreen
        composable<EndingRoute> { backStackEntry ->
            EndingScreen(
                uiState = EndingUiState.Loading,
                onShare = {},
                onPlayAgain = {}
            )
        }

        // Phase 15: HistoryScreen
        composable<HistoryRoute> {
            HistoryScreen(
                uiState = HistoryUiState.Loading,
                onGenreSelected = {},
                onHistoryItemClick = {},
                onBack = {}
            )
        }
    }
}
