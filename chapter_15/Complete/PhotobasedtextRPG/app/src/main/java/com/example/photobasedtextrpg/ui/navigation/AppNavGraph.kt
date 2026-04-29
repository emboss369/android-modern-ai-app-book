package com.example.photobasedtextrpg.ui.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.photobasedtextrpg.PhotoRpgApplication
import com.example.photobasedtextrpg.ui.ending.EndingScreen
import com.example.photobasedtextrpg.ui.ending.EndingUiState
import com.example.photobasedtextrpg.ui.ending.EndingViewModel
import com.example.photobasedtextrpg.ui.game.GameScreen
import com.example.photobasedtextrpg.ui.game.GameUiState
import com.example.photobasedtextrpg.ui.game.GameViewModel
import com.example.photobasedtextrpg.ui.history.HistoryScreen
import com.example.photobasedtextrpg.ui.history.HistoryUiState
import com.example.photobasedtextrpg.ui.history.HistoryViewModel
import com.example.photobasedtextrpg.ui.photopicker.PhotoPickerScreen
import com.example.photobasedtextrpg.ui.photopicker.PhotoPickerUiState
import com.example.photobasedtextrpg.ui.photopicker.PhotoPickerViewModel
import com.example.photobasedtextrpg.ui.start.StartScreen
import com.example.photobasedtextrpg.ui.worldgen.WorldGenScreen
import com.example.photobasedtextrpg.ui.worldgen.WorldGenUiState
import com.example.photobasedtextrpg.ui.worldgen.WorldGenViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// ── ルート定義 ────────────────────────────────────────────────────────────────

@Serializable
object StartRoute
@Serializable
object PhotoPickerRoute
@Serializable
data class WorldGenRoute(val imageUri: String)
@Serializable
data class GameRoute(val worldSettingId: String)
@Serializable
data class EndingRoute(val gameStateId: String)
@Serializable
object HistoryRoute

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
      val viewModel: PhotoPickerViewModel = viewModel()
      val uiState by viewModel.uiState.collectAsState()
      PhotoPickerScreen(
        uiState = uiState,
        onPhotoSelected = viewModel::onPhotoSelected,
        onStartAdventure = { uri ->
          navController.navigate(WorldGenRoute(uri.toString()))
        }
      )
    }

    // Phase 9: WorldGenScreen
    composable<WorldGenRoute> { backStackEntry ->
      val route: WorldGenRoute = backStackEntry.toRoute()
      val application =
        LocalContext.current.applicationContext as PhotoRpgApplication
      val viewModel: WorldGenViewModel = viewModel(
        factory = WorldGenViewModel.Factory(
          imageUri = route.imageUri,
          application = application,
          db = application.database
        )
      )
      val uiState by viewModel.uiState.collectAsState()
      WorldGenScreen(
        uiState = uiState,
        onGenreSelected = viewModel::onGenreSelected,
        onRetry = viewModel::onRetry,
        onNavigateToGame = { worldSettingId ->
          navController.navigate(GameRoute(worldSettingId)) {
            popUpTo(WorldGenRoute(route.imageUri)) {
              inclusive = true
            }
          }
        }
      )
    }

    // Phase 11: GameScreen
    composable<GameRoute> { backStackEntry ->
      val route: GameRoute = backStackEntry.toRoute()
      val application =
        LocalContext.current.applicationContext as PhotoRpgApplication
      val viewModel: GameViewModel = viewModel(
        factory = GameViewModel.Factory(
          worldSettingId = route.worldSettingId,
          application = application,
          db = application.database
        )
      )
      val uiState by viewModel.uiState.collectAsState()
      val scope = rememberCoroutineScope()
      LaunchedEffect(Unit) {
        scope.launch {
          viewModel.navigateToEnding.collect { gameStateId ->
            navController.navigate(EndingRoute(gameStateId)) {
              popUpTo(GameRoute(route.worldSettingId)) {
                inclusive = true
              }
            }
          }
        }
      }
      GameScreen(
        uiState = uiState,
        onChoiceSelected = viewModel::onChoiceSelected,
        onFreeInput = viewModel::onFreeInput
      )
    }

    // Phase 13: EndingScreen
    composable<EndingRoute> { backStackEntry ->
      val route: EndingRoute = backStackEntry.toRoute()
      val application =
        LocalContext.current.applicationContext as PhotoRpgApplication
      val context = LocalContext.current
      val viewModel: EndingViewModel = viewModel(
        factory = EndingViewModel.Factory(
          gameStateId = route.gameStateId,
          application = application,
          db = application.database
        )
      )
      val uiState by viewModel.uiState.collectAsState()
      val scope = rememberCoroutineScope()
      LaunchedEffect(Unit) {
        scope.launch {
          viewModel.shareEvent.collect { shareData ->
            val intent =
              Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                  Intent.EXTRA_TEXT,
                  shareData.text
                )
              }
            context.startActivity(
              Intent.createChooser(
                intent,
                "物語をシェア"
              )
            )
          }
        }
      }
      EndingScreen(
        uiState = uiState,
        onShare = viewModel::onShare,
        onPlayAgain = {
          navController.navigate(StartRoute) {
            popUpTo(0) { inclusive = true }
          }
        }
      )
    }

    // Phase 15: HistoryScreen
    composable<HistoryRoute> {
      val application =
        LocalContext.current.applicationContext as PhotoRpgApplication
      val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(db = application.database)
      )
      val uiState by viewModel.uiState.collectAsState()
      HistoryScreen(
        uiState = uiState,
        onGenreSelected = viewModel::onGenreSelected,
        onHistoryItemClick = { gameStateId ->
          navController.navigate(EndingRoute(gameStateId))
        },
        onBack = {
          navController.navigate(StartRoute) {
            popUpTo(0) { inclusive = true }
          }
        }
      )
    }
  }
}
