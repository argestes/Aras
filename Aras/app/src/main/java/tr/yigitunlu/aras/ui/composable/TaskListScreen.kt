package tr.yigitunlu.aras.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tr.yigitunlu.aras.R
import tr.yigitunlu.aras.data.repository.TaskFilter
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.presentation.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.tasks_title)) },
                actions = {
                    FilterTasksDropdown(
                        currentFilter = currentFilter,
                        onFilterSelected = viewModel::setFilter
                    )
                    IconButton(onClick = { viewModel.onSettingsClicked() }) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(id = R.string.task_list_settings_icon_description))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddTaskClicked() }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.task_list_add_task_icon_description))
            }
        }
    ) { paddingValues ->
        if (tasks.isEmpty()) {
            EmptyState(currentFilter = currentFilter)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = { isCompleted ->
                            viewModel.setTaskCompleted(task.id, isCompleted)
                        },
                        onItemClick = { viewModel.onTaskClicked(task) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(currentFilter: TaskFilter) {
    val messageRes = if (currentFilter == TaskFilter.ALL) {
        R.string.task_list_empty_unfiltered
    } else {
        R.string.task_list_empty_filtered
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = stringResource(id = R.string.task_list_empty_icon_description),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = messageRes),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun FilterTasksDropdown(
    currentFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(
                text = stringResource(id = currentFilter.toStringRes()),
                color = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TaskFilter.values().forEach { filter ->
                DropdownMenuItem(onClick = {
                    onFilterSelected(filter)
                    expanded = false
                }) {
                    Text(text = stringResource(id = filter.toStringRes()))
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(16.dp))
        Text(text = task.title, style = androidx.compose.material.MaterialTheme.typography.h6)
    }
}

private fun TaskFilter.toStringRes() = when (this) {
    TaskFilter.ALL -> R.string.filter_all
    TaskFilter.ACTIVE -> R.string.filter_active
    TaskFilter.COMPLETED -> R.string.filter_completed
}
