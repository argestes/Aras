package tr.yigitunlu.aras.presentation.viewmodel

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
import tr.yigitunlu.aras.presentation.navigation.NavigationManager
import javax.inject.Inject

data class TaskDetailUiState(
    val isLoading: Boolean = true,
    val showExitConfirmation: Boolean = false,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val task: Task? = null
)

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val navigationManager: NavigationManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    private var initialTask: Task? = null

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    private val _hasUnsavedChanges = MutableStateFlow(false)
    val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges

    init {
        viewModelScope.launch {
            repository.getTaskById(taskId).onEach { task ->
                if (task != null) {
                    initialTask = task
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = task.title,
                            description = task.description ?: "",
                            isCompleted = task.isCompleted,
                            task = task
                        )
                    }
                    checkIfChanged()
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
        checkIfChanged()
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
        checkIfChanged()
    }

    fun onCompletionChange(isCompleted: Boolean) {
        _uiState.update { it.copy(isCompleted = isCompleted) }
        checkIfChanged()
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
                initialTask = taskToUpdate
                checkIfChanged()
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            _uiState.value.task?.let { repository.delete(it) }
            navigationManager.goBack()
        }
    }

    fun onBackClicked() {
        if (_hasUnsavedChanges.value) {
            _uiState.update { it.copy(showExitConfirmation = true) }
        } else {
            navigationManager.goBack()
        }
    }

    fun onConfirmationDismissed() {
        _uiState.update { it.copy(showExitConfirmation = false) }
    }

    fun onExitConfirmClicked() {
        onConfirmationDismissed()
        navigationManager.goBack()
    }

    fun onExitDismissClicked() {
        onConfirmationDismissed()
    }

    private fun checkIfChanged() {
        val currentState = _uiState.value
        val original = initialTask

        val changed = if (original == null) {
            // New task
            currentState.title.isNotEmpty() || currentState.description.isNotEmpty()
        } else {
            // Existing task
            original.title != currentState.title ||
                    original.description != currentState.description ||
                    original.isCompleted != currentState.isCompleted
        }

        _hasUnsavedChanges.value = changed
    }


}
