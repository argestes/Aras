package tr.yigitunlu.aras.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class NavigationCommand {
    data class Navigate(val destination: String) : NavigationCommand()
    object GoBack : NavigationCommand()
}

@Singleton
class NavigationManager @Inject constructor() {
    private val _commands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)
    val commands = _commands.asSharedFlow()

    fun navigate(destination: String) {
        _commands.tryEmit(NavigationCommand.Navigate(destination))
    }

    fun goBack() {
        _commands.tryEmit(NavigationCommand.GoBack)
    }
}
