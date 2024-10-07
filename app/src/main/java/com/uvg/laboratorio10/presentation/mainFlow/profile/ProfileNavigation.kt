package com.uvg.laboratorio10.presentation.mainFlow.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ProfileDestination

fun NavGraphBuilder.profileScreen(
    onLogOutClick: () -> Unit
) {
    composable<ProfileDestination> {
        ProfileRoute(onLogOutClick = onLogOutClick)
    }
}