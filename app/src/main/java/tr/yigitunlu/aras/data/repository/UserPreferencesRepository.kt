package tr.yigitunlu.aras.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserPreferences(
    val theme: AppTheme,
    val defaultFilter: TaskFilter
)

enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

enum class TaskFilter {
    ALL, COMPLETED, ACTIVE
}

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val APP_THEME = stringPreferencesKey("app_theme")
        val TASK_FILTER = stringPreferencesKey("task_filter")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            val theme = AppTheme.valueOf(
                preferences[PreferencesKeys.APP_THEME] ?: AppTheme.SYSTEM.name
            )
            val filter = TaskFilter.valueOf(
                preferences[PreferencesKeys.TASK_FILTER] ?: TaskFilter.ALL.name
            )
            UserPreferences(theme, filter)
        }

    suspend fun updateTheme(theme: AppTheme) {
        context.dataStore.edit {
            it[PreferencesKeys.APP_THEME] = theme.name
        }
    }

    suspend fun updateTaskFilter(filter: TaskFilter) {
        context.dataStore.edit {
            it[PreferencesKeys.TASK_FILTER] = filter.name
        }
    }
}
