package com.uvg.laboratorio10.presentation.mainFlow.location.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.uvg.laboratorio10.data.source.LocationDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationProfileViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val locationDb = LocationDb()
    private val locationProfile = savedStateHandle.toRoute<LocationProfileDestination>()
    private val _uiState: MutableStateFlow<LocationProfileState> = MutableStateFlow(
        LocationProfileState(loading = false, hasError = false, data = null)
    )
    val uiState = _uiState.asStateFlow()

    init {
        getLocationData()
    }

    fun getLocationData() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(initial = false, loading = true, hasError = false) }

            delay(2000)

            val location = locationDb.getLocationById(locationProfile.locationId)

            if (location == null) {
                showError()
            } else {
                _uiState.update { state ->
                    state.copy(data = location, loading = false, hasError = false)
                }
            }
        }
    }


    fun showError() {
        Log.d("LocationProfileViewModel", "Showing error screen...")
        _uiState.value = _uiState.value.copy(
            loading = false,
            hasError = true
        )
    }

    fun retryLoading() {
        Log.d("LocationProfileViewModel", "Retry loading location data...")
        _uiState.value = _uiState.value.copy(hasError = false)
        getLocationData()
    }
}

