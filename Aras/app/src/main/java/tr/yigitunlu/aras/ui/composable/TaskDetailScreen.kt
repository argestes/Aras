package tr.yigitunlu.aras.ui.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tr.yigitunlu.aras.R
import tr.yigitunlu.aras.presentation.viewmodel.TaskDetailViewModel

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsState()

    if (uiState.showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.onConfirmationDismissed() },
            title = { Text(stringResource(id = R.string.unsaved_changes_dialog_title)) },
            text = { Text(stringResource(id = R.string.unsaved_changes_dialog_message)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onExitConfirmClicked() }) {
                    Text(stringResource(id = R.string.unsaved_changes_dialog_continue))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onExitDismissClicked() }) {
                    Text(stringResource(id = R.string.unsaved_changes_dialog_discard))
                }
            }
        )
    }

    BackHandler(enabled = true) {
        viewModel.onBackClicked()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.task_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClicked() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.settings_back_button_description))
                    }
                },
                actions = {
                    if (hasUnsavedChanges) {
                        IconButton(onClick = { viewModel.saveTask() }) {
                            Icon(Icons.Default.Done, contentDescription = stringResource(id = R.string.task_detail_save_task_icon_description))
                        }
                    }
                    var showMenu by remember { mutableStateOf(false) }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.deleteTask()
                            showMenu = false
                        }) {
                            Text(stringResource(id = R.string.task_detail_delete_menu_item))
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    label = { Text(stringResource(id = R.string.task_title_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = { Text(stringResource(id = R.string.task_description_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.isCompleted,
                        onCheckedChange = viewModel::onCompletionChange
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = R.string.task_completed_label))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
