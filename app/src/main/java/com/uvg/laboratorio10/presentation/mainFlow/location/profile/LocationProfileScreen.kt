package com.uvg.laboratorio10.presentation.mainFlow.location.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.laboratorio10.data.model.Location
import com.uvg.laboratorio10.presentation.ui.theme.Laboratorio10Theme

@Composable
fun LocationProfileRoute(
    onNavigateBack: () -> Unit,
    viewModel: LocationProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("LocationProfileRoute", "State: $state")

    LocationProfileScreen(
        state = state,
        onGetInfoClick = {
            viewModel.getLocationData()
        },
        onRetryClick = {
            viewModel.retryLoading()
        },
        onErrorClick = {
            viewModel.showError()
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationProfileScreen(
    state: LocationProfileState,
    onGetInfoClick: () -> Unit,
    onRetryClick: () -> Unit,
    onErrorClick: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Text("Character Detail")
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.initial -> {
                    EmptyContent(onGetInfoClick = onGetInfoClick)
                }
                state.loading -> {
                    LoadingIndicator(onErrorClick = onErrorClick)
                }
                state.hasError -> {
                    ErrorScreen(onRetryClick = onRetryClick)
                }
                state.data == null -> {
                    EmptyContent(onGetInfoClick = onGetInfoClick)
                }
                else -> {
                    LocationProfileDetails(location = state.data)
                }
            }
        }
    }
}


@Composable
private fun LoadingIndicator(onErrorClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onErrorClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Cargando")
        }
    }
}



@Composable
private fun ErrorScreen(onRetryClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onRetryClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ocurrió un error al obtener la información.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onRetryClick() }) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyContent(onGetInfoClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onGetInfoClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Obtener información")
        }
    }
}

@Composable
private fun LocationProfileDetails(location: Location) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = location.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        LocationProfilePropItem(
            title = "ID:",
            value = location.id.toString(),
            modifier = Modifier.fillMaxWidth()
        )
        LocationProfilePropItem(
            title = "Type:",
            value = location.type,
            modifier = Modifier.fillMaxWidth()
        )
        LocationProfilePropItem(
            title = "Dimensions:",
            value = location.dimension,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LocationProfilePropItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Text(text = value)
    }
}


// Previews
@Preview
@Composable
private fun PreviewLocationProfileScreen() {
    Laboratorio10Theme {
        Surface {
            LocationProfileScreen(
                state = LocationProfileState(
                    loading = false,
                    data = Location(1, "Earth (C-137)", "Planet", "Dimension C-137")
                ),
                onNavigateBack = { },
                onGetInfoClick = { },
                onRetryClick = { },
                onErrorClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLoadingLocationProfileScreen() {
    Laboratorio10Theme {
        Surface {
            LocationProfileScreen(
                state = LocationProfileState(
                    loading = true,
                    data = null
                ),
                onNavigateBack = { },
                onGetInfoClick = { },
                onRetryClick = { },
                onErrorClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmptyLocationProfileScreen() {
    Laboratorio10Theme {
        Surface {
            LocationProfileScreen(
                state = LocationProfileState(
                    loading = false,
                    data = null
                ),
                onNavigateBack = { },
                onGetInfoClick = { },
                onRetryClick = { },
                onErrorClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
