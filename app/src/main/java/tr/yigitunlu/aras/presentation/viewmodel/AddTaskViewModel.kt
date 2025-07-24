package tr.yigitunlu.aras.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository
import tr.yigitunlu.aras.presentation.navigation.NavigationManager
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onKeyboardSaveClicked() {
        onSaveClicked()
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            if (_title.value.isNotBlank()) {
                repository.insert(Task(title = _title.value, description = _description.value))
                navigationManager.goBack()
            }
        }
    }
}
