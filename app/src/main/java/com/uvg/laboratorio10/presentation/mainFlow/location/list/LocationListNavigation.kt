package com.uvg.laboratorio10.presentation.mainFlow.location.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LocationListDestination

fun NavGraphBuilder.locationListScreen(
    onLocationClick: (Int) -> Unit
) {
    composable<LocationListDestination> {
        LocationListRoute(onLocationClick = onLocationClick)
    }
}