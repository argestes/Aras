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
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        getTasks(isCompleted = null) // Load all tasks initially
    }

    fun getTasks(isCompleted: Boolean?) {
        val taskFlow = when (isCompleted) {
            true -> repository.getTasksByCompletion(true)
            false -> repository.getTasksByCompletion(false)
            null -> repository.getAllTasks()
        }

        taskFlow.onEach { taskList ->
            _tasks.value = taskList
        }.launchIn(viewModelScope)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}
