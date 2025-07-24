package tr.yigitunlu.aras.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tr.yigitunlu.aras.data.repository.AppTheme
import tr.yigitunlu.aras.data.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val theme: StateFlow<AppTheme?> = userPreferencesRepository
        .userPreferencesFlow
        .map {
            it.theme
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
