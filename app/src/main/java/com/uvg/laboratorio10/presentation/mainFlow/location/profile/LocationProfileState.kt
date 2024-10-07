package com.uvg.laboratorio10.presentation.mainFlow.location.profile

import com.uvg.laboratorio10.data.model.Location

data class LocationProfileState(
    val data: Location? = null,
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val initial: Boolean = true
)
