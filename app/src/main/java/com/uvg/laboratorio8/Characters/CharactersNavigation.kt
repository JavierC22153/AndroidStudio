package com.uvg.laboratorio8.Characters

import CharacterDb
import Character
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.uvg.laboratorio8.Detail.DetailScreen
import kotlinx.serialization.Serializable


@Serializable
data class CharacterDetailDestination(val characterId: Int)

fun NavController.navigateToDetailScreen(character: Character) {
    this.navigate("detail_screen/${character.id}")
}

fun NavGraphBuilder.charactersScreen(
    characterDb: CharacterDb,
    onCharacterClick: (Character) -> Unit
) {
    composable("characters_screen") {
        CharactersScreen(
            characterDb = characterDb,
            onCharacterClick = onCharacterClick
        )
    }

    composable("detail_screen/{characterId}") { backStackEntry ->
        val characterId = backStackEntry.arguments?.getString("characterId")?.toIntOrNull() ?: return@composable
        val character = characterDb.getCharacterById(characterId)
        DetailScreen(
            character = character,
            onBackClick = { /* Handle back navigation */ }
        )
    }
}