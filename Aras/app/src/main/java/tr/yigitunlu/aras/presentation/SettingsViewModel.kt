package tr.yigitunlu.aras.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tr.yigitunlu.aras.data.repository.AppTheme
import tr.yigitunlu.aras.data.repository.TaskFilter
import tr.yigitunlu.aras.data.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userPreferences = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
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
}
