package tr.yigitunlu.aras.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import tr.yigitunlu.aras.R
import tr.yigitunlu.aras.presentation.viewmodel.AddTaskViewModel
import tr.yigitunlu.aras.ui.theme.Padding

@Composable
fun AddTaskSheet(
    viewModel: AddTaskViewModel = hiltViewModel()
) {
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.Medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = viewModel::onTitleChange,
            label = { Text(stringResource(id = R.string.add_task_title_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text(stringResource(id = R.string.task_description_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding.Small),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    viewModel.onKeyboardSaveClicked()
                }
            )
        )

        Button(
            onClick = { viewModel.onSaveClicked() },
            modifier = Modifier
                .padding(top = Padding.Medium)
                .align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.add_task_save_button))
        }
    }
}
