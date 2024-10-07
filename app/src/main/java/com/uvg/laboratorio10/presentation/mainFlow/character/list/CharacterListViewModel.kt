package com.uvg.laboratorio10.presentation.mainFlow.character.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.laboratorio10.data.model.Character
import com.uvg.laboratorio10.data.source.CharacterDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterListState())
    val uiState: StateFlow<CharacterListState> = _uiState.asStateFlow()

    init {
        getCharacters()
    }

    fun getCharacters() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            Log.d("CharacterListViewModel", "Loading characters...")
            try {
                delay(4000)
                val characters = CharacterDb().getAllCharacters()
                Log.d("CharacterListViewModel", "Characters loaded successfully.")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = characters,
                    hasError = false
                )
            } catch (e: Exception) {
                Log.e("CharacterListViewModel", "Error loading characters: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun showError() {
        Log.d("CharacterListViewModel", "Showing error screen...")
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            hasError = true
        )
    }

    fun retryLoading() {
        Log.d("CharacterListViewModel", "Retry loading characters...")
        _uiState.value = _uiState.value.copy(hasError = false)
        getCharacters()
    }
}
