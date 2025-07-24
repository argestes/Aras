package tr.yigitunlu.aras.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tr.yigitunlu.aras.R
import tr.yigitunlu.aras.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClicked() }) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.settings_back_button_description
                            ))
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(stringResource(id = R.string.settings_theme_title),
                style = androidx.compose.material.MaterialTheme.typography.h6)
            uiState.themeOptions.forEach { themeInfo ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateTheme(themeInfo.theme) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = themeInfo.isSelected,
                        onClick = { viewModel.updateTheme(themeInfo.theme) })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = themeInfo.nameRes))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(id = R.string.settings_filter_title),
                style = androidx.compose.material.MaterialTheme.typography.h6
            )
            uiState.filterOptions.forEach { filterInfo ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateTaskFilter(filterInfo.filter) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = filterInfo.isSelected,
                        onClick = { viewModel.updateTaskFilter(filterInfo.filter) })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = filterInfo.nameRes))
                }
            }
        }
    }
}