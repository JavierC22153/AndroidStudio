package com.uvg.laboratorio8.Login

import CharacterDb
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.uvg.laboratorio8.Characters.CharactersScreen
import kotlinx.serialization.Serializable

@Serializable
data class LoginDestination(val userId: Int? = null)

fun NavController.navigateToCharactersScreen() {
    this.navigate("characters_screen")
}

fun NavGraphBuilder.loginScreen(
    onNavigateToCharacters: () -> Unit
) {
    composable("login_screen") {
        LoginScreen(onLoginClick = onNavigateToCharacters)
    }
}