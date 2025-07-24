package tr.yigitunlu.aras.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tr.yigitunlu.aras.R
import tr.yigitunlu.aras.data.repository.AppTheme
import tr.yigitunlu.aras.data.repository.TaskFilter
import tr.yigitunlu.aras.data.repository.UserPreferencesRepository
import tr.yigitunlu.aras.presentation.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = userPreferencesRepository.userPreferencesFlow.map {
        val filterOptions = TaskFilter.values().map {  filter ->
            FilterInfo(
                filter = filter,
                nameRes = filter.toStringRes(),
                isSelected = it.defaultFilter == filter
            )
        }
        val themeOptions = AppTheme.values().map { theme ->
            ThemeInfo(
                theme = theme,
                nameRes = theme.toStringRes(),
                isSelected = it.theme == theme
            )
        }
        SettingsUiState(filterOptions = filterOptions, themeOptions = themeOptions)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            userPreferencesRepository.updateTheme(theme)
        }
    }

    fun updateTaskFilter(filter: TaskFilter) {
        viewModelScope.launch {
            userPreferencesRepository.updateTaskFilter(filter)
        }
    }

    fun onBackClicked() {
        navigationManager.goBack()
    }
}

data class SettingsUiState(
    val filterOptions: List<FilterInfo> = emptyList(),
    val themeOptions: List<ThemeInfo> = emptyList()
)

data class FilterInfo(
    val filter: TaskFilter,
    @StringRes val nameRes: Int,
    val isSelected: Boolean
)

data class ThemeInfo(
    val theme: AppTheme,
    @StringRes val nameRes: Int,
    val isSelected: Boolean
)

private fun AppTheme.toStringRes() = when (this) {
    AppTheme.SYSTEM -> R.string.theme_system
    AppTheme.LIGHT -> R.string.theme_light
    AppTheme.DARK -> R.string.theme_dark
}

private fun TaskFilter.toStringRes() = when (this) {
    TaskFilter.ALL -> R.string.filter_all
    TaskFilter.ACTIVE -> R.string.filter_active
    TaskFilter.COMPLETED -> R.string.filter_completed
}
