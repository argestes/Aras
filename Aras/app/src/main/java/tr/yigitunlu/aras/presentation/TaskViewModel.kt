package tr.yigitunlu.aras.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tr.yigitunlu.aras.data.repository.TaskFilter
import tr.yigitunlu.aras.data.repository.UserPreferencesRepository
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()

    init {
        userPreferencesRepository.userPreferencesFlow.onEach { preferences ->
            _currentFilter.value = preferences.defaultFilter
            val isCompleted = when (preferences.defaultFilter) {
                TaskFilter.ALL -> null
                TaskFilter.COMPLETED -> true
                TaskFilter.ACTIVE -> false
            }
            getTasks(isCompleted)
        }.launchIn(viewModelScope)
    }

    private fun getTasks(isCompleted: Boolean?) {
        val taskFlow = when (isCompleted) {
            true -> repository.getTasksByCompletion(true)
            false -> repository.getTasksByCompletion(false)
            null -> repository.getAllTasks()
        }

        taskFlow.onEach { taskList ->
            _tasks.value = taskList
        }.launchIn(viewModelScope)
    }


    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }


    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
        val isCompleted = when (filter) {
            TaskFilter.ALL -> null
            TaskFilter.COMPLETED -> true
            TaskFilter.ACTIVE -> false
        }
        getTasks(isCompleted)
    }
}
