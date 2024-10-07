package com.uvg.laboratorio10.presentation.mainFlow.location.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.laboratorio10.data.source.LocationDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LocationListState())
    val uiState: StateFlow<LocationListState> = _uiState.asStateFlow()

    init {
        getLocations()
    }

    fun getLocations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            Log.d("LocationListViewModel", "Loading locations...")
            try {
                delay(2000)
                val locations = LocationDb().getAllLocations()
                Log.d("LocationListViewModel", "Locations loaded successfully.")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = locations,
                    hasError = false
                )
            } catch (e: Exception) {
                Log.e("LocationListViewModel", "Error loading locations: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun showError() {
        Log.d("LocationListViewModel", "Showing error screen...")
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            hasError = true
        )
    }

    fun retryLoading() {
        Log.d("LocationListViewModel", "Retry loading locations...")
        _uiState.value = _uiState.value.copy(hasError = false)
        getLocations()
    }
}


