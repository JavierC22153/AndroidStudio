package com.uvg.laboratorio10.presentation.mainFlow.character.list

import com.uvg.laboratorio10.data.model.Character

data class CharacterListState(
    val data: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)
