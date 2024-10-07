package com.uvg.laboratorio10.presentation.mainFlow.location.list

import com.uvg.laboratorio10.data.model.Location

data class LocationListState(
    val data: List<Location> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)

