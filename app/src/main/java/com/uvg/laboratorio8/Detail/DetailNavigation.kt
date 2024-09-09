package com.uvg.laboratorio8.Detail

import CharacterDb
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetailDestination(val characterId: Int)

fun NavController.navigateToDetailScreen(characterId: Int) {
    val destination = CharacterDetailDestination(characterId)
    this.navigate("detail_screen/${destination.characterId}")
}

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit,
    characterDb: CharacterDb
) {
    composable("detail_screen/{characterId}") { backStackEntry ->
        val characterId = backStackEntry.arguments?.getString("characterId")?.toInt() ?: return@composable
        val character = characterDb.getCharacterById(characterId)
        DetailScreen(
            character = character,
            onBackClick = onNavigateBack
        )
    }
}