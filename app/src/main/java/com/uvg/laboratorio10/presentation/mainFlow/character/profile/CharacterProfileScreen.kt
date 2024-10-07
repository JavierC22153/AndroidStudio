package com.uvg.laboratorio10.presentation.mainFlow.character.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.uvg.laboratorio10.data.model.Character
import com.uvg.laboratorio10.presentation.ui.theme.Laboratorio10Theme

@Composable
fun CharacterProfileRoute(
    id: Int,
    onNavigateBack: () -> Unit,
    viewModel: CharacterProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterProfileScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        viewModel = viewModel,
        id = id,
        modifier = Modifier.fillMaxSize()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterProfileScreen(
    state: CharacterProfileState,
    onNavigateBack: () -> Unit,
    viewModel: CharacterProfileViewModel = viewModel(),
    id: Int,
    modifier: Modifier = Modifier
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
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )

        when {
            state.isLoading -> {
                LoadingIndicator { viewModel.showError() }
            }
            state.hasError -> {
                ErrorScreen { viewModel.retryLoading() }
            }
            state.character != null -> {
                CharacterContent(character = state.character)
            }
            else -> {
                EmptyContent {
                    viewModel.fetchCharacterInfo(id)
                }
            }
        }
    }
}


@Composable
private fun CharacterContent(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = character.image,
            contentDescription = "Image of ${character.name}",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape) // Adjust the shape to a circle
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = character.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        CharacterProfilePropItem(
            title = "Species:",
            value = character.species,
            modifier = Modifier.fillMaxWidth()
        )
        CharacterProfilePropItem(
            title = "Status:",
            value = character.status,
            modifier = Modifier.fillMaxWidth()
        )
        CharacterProfilePropItem(
            title = "Gender:",
            value = character.gender,
            modifier = Modifier.fillMaxWidth()
        )
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
            Text(text = "Loading...")
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
        Text(text = "No character information available.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onGetInfoClick() }) {
            Text("Fetch Information")
        }
    }
}

@Composable
private fun CharacterProfilePropItem(
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCharacterProfileScreen() {
    Laboratorio10Theme {
        Surface {
            CharacterProfileScreen(
                state = CharacterProfileState(character = Character(
                    id = 2565,
                    name = "Rick",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    image = ""
                )),
                onNavigateBack = { },
                id = 2565,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
