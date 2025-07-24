package tr.yigitunlu.aras.ui.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import tr.yigitunlu.aras.data.repository.AppTheme
import tr.yigitunlu.aras.presentation.viewmodel.ThemeViewModel

private val DarkColorPalette = darkColors(
    primary = Purple80,
    primaryVariant = PurpleGrey80,
    secondary = Pink80,
    onPrimary = Color.White
)

private val LightColorPalette = lightColors(
    primary = Purple40,
    primaryVariant = PurpleGrey40,
    secondary = Pink40

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ArasTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val theme by themeViewModel.theme.collectAsState()
    val useDarkTheme = when (theme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        else -> isSystemInDarkTheme()
    }

    val view = LocalView.current


    val colors = if (useDarkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }


    if (!view.isInEditMode) {
        SideEffect {


            val activity = view.context as? ComponentActivity
            val window = activity?.window
            val color =
                if (useDarkTheme) 0x33000000 else android.graphics.Color.WHITE



            val systemBarStyle = if (useDarkTheme) {
                SystemBarStyle.dark(DarkColorPalette.background.toArgb())
            } else {
                SystemBarStyle.light(
                    LightColorPalette.primary.toArgb(),
                    LightColorPalette.primary.toArgb()
                )
            }

            activity?.enableEdgeToEdge(
                statusBarStyle = systemBarStyle,
                navigationBarStyle = systemBarStyle
            )


//            window?.setStatusBarBackgroundColor(
//                color
//            )
//
//            window?.setNavigationBarBackgroundColor(
//                color
//            )


//            window?.setBackgroundDrawable(
//                ColorDrawable(
//                    if (useDarkTheme) android.graphics.Color.BLACK else android.graphics.Color.WHITE
//                )
//            )

//            WindowInsetsControllerCompat(window, window?.decorView).isAppearanceLightStatusBars = false

        }
    }

    Surface(
        color = colors.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .displayCutoutPadding()
                .navigationBarsPadding()
        ) {
            MaterialTheme(
                colors = colors,
                typography = Typography,
                shapes = Shapes,
                content = content
            )
        }
    }


}