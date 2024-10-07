package com.uvg.laboratorio10.presentation.mainFlow.location.list

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
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
import com.uvg.laboratorio10.data.source.LocationDb
import com.uvg.laboratorio10.presentation.ui.theme.Laboratorio10Theme

@Composable
fun LocationListRoute(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LocationListScreen(
        state = state,
        onRetryClick = { viewModel.retryLoading() },
        onErrorClick = { viewModel.showError() },
        onLocationClick = onLocationClick,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun LocationListScreen(
    state: LocationListState,
    onRetryClick: () -> Unit,
    onErrorClick: () -> Unit,
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                LoadingIndicator(onErrorClick)
            }
            state.hasError -> {
                ErrorScreen(onRetryClick)
            }
            state.data.isEmpty() -> {
                EmptyContent()
            }
            else -> {
                LocationList(state.data, onLocationClick)
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
private fun EmptyContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No hay ubicaciones disponibles.")
    }
}

@Composable
private fun LocationList(data: List<Location>, onLocationClick: (Int) -> Unit) {
    LazyColumn {
        items(data) { item ->
            LocationItem(location = item, onLocationClick = onLocationClick)
        }
    }
}

@Composable
private fun LocationItem(location: Location, onLocationClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable { onLocationClick(location.id) }
    ) {
        Text(text = location.name)
        Text(text = location.type, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLocationListScreen() {
    Laboratorio10Theme {
        Surface {
            val db = LocationDb()
            LocationListScreen(
                state = LocationListState(
                    isLoading = false,
                    data = db.getAllLocations().take(6),
                    hasError = false
                ),
                onLocationClick = {},
                onRetryClick = {},
                onErrorClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}



