package tr.yigitunlu.aras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tr.yigitunlu.aras.presentation.AddTaskSheet
import tr.yigitunlu.aras.presentation.TaskDetailScreen
import tr.yigitunlu.aras.presentation.TaskListScreen
import tr.yigitunlu.aras.ui.theme.ArasTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//                        .consumeWindowInsets(WindowInsets.safeContent)
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