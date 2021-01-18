package com.example.inmobilestask.data.models

sealed class NetworkState {
    data class Success(val data: ServerResponse) : NetworkState()
    data class Error(val error: String) : NetworkState()
    object Loading : NetworkState()
}


