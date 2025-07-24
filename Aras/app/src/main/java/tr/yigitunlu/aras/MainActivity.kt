package tr.yigitunlu.aras

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tr.yigitunlu.aras.presentation.AddTaskSheet
import tr.yigitunlu.aras.presentation.SettingsScreen
import tr.yigitunlu.aras.presentation.TaskDetailScreen
import tr.yigitunlu.aras.presentation.TaskListScreen
import tr.yigitunlu.aras.ui.theme.ArasTheme

fun Window.setStatusBarBackgroundColor(@ColorInt color: Int) {
    ValueAnimator.ofObject(
        ArgbEvaluator(),
        statusBarColor,
        color
    ).apply {
        addUpdateListener {
            statusBarColor = it.animatedValue as Int
        }
    }.start()
}

fun Window.setNavigationBarBackgroundColor(@ColorInt color: Int) {
    ValueAnimator.ofObject(
        ArgbEvaluator(),
        navigationBarColor,
        color
    ).apply {
        addUpdateListener {
            navigationBarColor = it.animatedValue as Int
        }
    }.start()
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            ArasTheme {
                val bottomSheetNavigator = BottomSheetNavigator(
                    sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                )
                val navController = rememberNavController(bottomSheetNavigator)

                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.safeDrawing.asPaddingValues())
                ) {
                    NavHost(navController, startDestination = "taskList") {
                        composable("taskList") {
                            TaskListScreen(navController = navController)
                        }
                        composable(
                            route = "taskDetail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            TaskDetailScreen(navController = navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }
                        bottomSheet("addTask") {
                            AddTaskSheet {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBarProtection(
    color: Color = MaterialTheme.colors.primary,
    heightProvider: () -> Float = calculateGradientHeight(),
) {
    androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
        val calculatedHeight = heightProvider()
        val gradient = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 1f),
                color.copy(alpha = .8f),
                Color.Transparent
            ),
            startY = 0f,
            endY = calculatedHeight
        )
        drawRect(
            brush = gradient,
            size = Size(size.width, calculatedHeight),
        )
    }
}

@Composable
fun calculateGradientHeight(): () -> Float {
    val statusBars = WindowInsets.statusBars
    val density = LocalDensity.current
    return { statusBars.getTop(density).times(1.2f) }
}