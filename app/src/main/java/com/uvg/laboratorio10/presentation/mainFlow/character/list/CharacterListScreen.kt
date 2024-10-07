package com.uvg.laboratorio10.presentation.mainFlow.character.list

import android.content.res.Configuration
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
import com.uvg.laboratorio10.data.model.Character
import com.uvg.laboratorio10.presentation.ui.theme.Laboratorio10Theme

@Composable
fun CharacterListRoute(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterListScreen(
        state = state,
        onRetryClick = { viewModel.retryLoading() },
        onErrorClick = { viewModel.showError() },
        onCharacterClick = onCharacterClick,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun CharacterListScreen(
    state: CharacterListState,
    onRetryClick: () -> Unit,
    onErrorClick: () -> Unit,
    onCharacterClick: (Int) -> Unit,
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
                CharacterList(state.data, onCharacterClick)
            }
        }
    }
}

@Composable
private fun CharacterList(data: List<Character>, onCharacterClick: (Int) -> Unit) {
    LazyColumn {
        items(data) { item ->
            CharacterItem(character = item, onCharacterClick = onCharacterClick)
        }
    }
}

@Composable
private fun CharacterItem(character: Character, onCharacterClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .clickable { onCharacterClick(character.id) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = character.name)
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
        Text(text = "No hay personajes disponibles.")
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCharacterListScreen() {
    Laboratorio10Theme {
        Surface {
            // Simulación de datos
            val sampleCharacters = listOf(
                Character(1, "Personaje 1", "Vivo", "Especie 1", "Masculino", ""),
                Character(2, "Personaje 2", "Muerto", "Especie 2", "Femenino", "")
            )
            CharacterListScreen(
                state = CharacterListState(data = sampleCharacters),
                onRetryClick = {},
                onErrorClick = {},
                onCharacterClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
