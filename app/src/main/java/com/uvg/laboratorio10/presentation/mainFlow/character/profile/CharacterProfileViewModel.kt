package com.uvg.laboratorio10.presentation.mainFlow.character.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.laboratorio10.data.source.CharacterDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterProfileViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val characterDb = CharacterDb()
    private val characterId = savedStateHandle.get<Int>("characterId")
    private val _uiState: MutableStateFlow<CharacterProfileState> = MutableStateFlow(
        CharacterProfileState(isLoading = false, hasError = false, character = null)
    )
    val uiState = _uiState.asStateFlow()

    init {
        characterId?.let { fetchCharacterInfo(it) }
    }

    fun fetchCharacterInfo(characterId: Int) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true, hasError = false) }

            delay(2000)

            val character = characterDb.getCharacterById(characterId)

            if (character == null) {
                showError()
            } else {
                _uiState.update { state ->
                    state.copy(character = character, isLoading = false, hasError = false)
                }
            }
        }
    }

    fun showError() {
        Log.d("CharacterProfileViewModel", "Showing error screen...")
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            hasError = true
        )
    }

    fun retryLoading() {
        Log.d("CharacterProfileViewModel", "Retry loading character data...")
        _uiState.value = _uiState.value.copy(hasError = false)
        characterId?.let { fetchCharacterInfo(it) }
    }
}
