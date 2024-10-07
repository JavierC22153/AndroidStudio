package com.uvg.laboratorio10.presentation.mainFlow.character.profile

import com.uvg.laboratorio10.data.model.Character

data class CharacterProfileState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val character: Character? = null
)

