package tr.yigitunlu.aras

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.Window
import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

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

@Composable
fun calculateGradientHeight(): () -> Float {
    val statusBars = WindowInsets.statusBars
    val density = LocalDensity.current
    return { statusBars.getTop(density).times(1.2f) }
}