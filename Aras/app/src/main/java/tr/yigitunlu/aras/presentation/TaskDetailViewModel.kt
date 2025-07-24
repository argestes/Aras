package tr.yigitunlu.aras.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository
import javax.inject.Inject

data class TaskDetailUiState(
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val task: Task? = null
)

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        repository.getTaskById(taskId).onEach { task ->
            _uiState.update {
                it.copy(
                    title = task?.title ?: "",
                    description = task?.description ?: "",
                    isCompleted = task?.isCompleted ?: false,
                    task = task
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onCompletionChange(isCompleted: Boolean) {
        _uiState.update { it.copy(isCompleted = isCompleted) }
    }

    fun saveTask() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val taskToUpdate = currentState.task?.copy(
                title = currentState.title,
                description = currentState.description,
                isCompleted = currentState.isCompleted
            )
            if (taskToUpdate != null) {
                repository.update(taskToUpdate)
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            _uiState.value.task?.let { repository.delete(it) }
        }
    }
}
