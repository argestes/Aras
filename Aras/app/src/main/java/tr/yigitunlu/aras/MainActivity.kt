package tr.yigitunlu.aras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tr.yigitunlu.aras.presentation.composable.AddTaskSheet
import tr.yigitunlu.aras.presentation.composable.SettingsScreen
import tr.yigitunlu.aras.presentation.composable.TaskDetailScreen
import tr.yigitunlu.aras.presentation.composable.TaskListScreen
import tr.yigitunlu.aras.presentation.navigation.NavigationCommand
import tr.yigitunlu.aras.presentation.navigation.NavigationManager
import tr.yigitunlu.aras.ui.theme.ArasTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ArasTheme {
                val bottomSheetNavigator = BottomSheetNavigator(
                    sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                )
                val navController = rememberNavController(bottomSheetNavigator)

                LaunchedEffect(Unit) {
                    navigationManager.commands.collect { command ->
                        when (command) {
                            is NavigationCommand.Navigate -> navController.navigate(command.destination)
                            is NavigationCommand.GoBack -> navController.popBackStack()
                        }
                    }
                }

                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.safeDrawing.asPaddingValues())
                ) {
                    NavHost(
                        navController,
                        startDestination = "taskList",
                        enterTransition = { slideInHorizontally() + fadeIn() },
                        popEnterTransition = { fadeIn() },
                        exitTransition = { fadeOut()  },
                        popExitTransition = {  fadeOut()  + slideOutHorizontally()},
                    ) {
                        composable("taskList") {
                            TaskListScreen()
                        }
                        composable(
                            route = "taskDetail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            TaskDetailScreen()
                        }
                        composable("settings") {
                            SettingsScreen()
                        }
                        bottomSheet("addTask") {
                            AddTaskSheet()
                        }
                    }
                }
            }
        }
    }
}

