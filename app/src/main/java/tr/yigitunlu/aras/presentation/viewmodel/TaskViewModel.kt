package tr.yigitunlu.aras.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tr.yigitunlu.aras.data.repository.TaskFilter
import tr.yigitunlu.aras.data.repository.UserPreferencesRepository
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository
import tr.yigitunlu.aras.presentation.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {


    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()

    val tasks = currentFilter.flatMapLatest {
        when (it) {
            TaskFilter.ALL -> repository.getAllTasks()
            TaskFilter.COMPLETED -> repository.getTasksByCompletion(true)
            TaskFilter.ACTIVE -> repository.getTasksByCompletion(false)
        }
    }        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())



    init {
        userPreferencesRepository.userPreferencesFlow.onEach { preferences ->
            _currentFilter.value = preferences.defaultFilter
        }.launchIn(viewModelScope)
    }


    fun setTaskCompleted(id: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.getTaskById(id).first()?.let { task ->
                repository.update(task.copy(isCompleted = isCompleted))
            }
        }
    }


    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
    }

    fun onSettingsClicked() {
        navigationManager.navigate("settings")
    }

    fun onAddTaskClicked() {
        navigationManager.navigate("addTask")
    }

    fun onTaskClicked(task: Task) {
        navigationManager.navigate("taskDetail/${task.id}")
    }
}
