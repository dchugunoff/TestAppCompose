package com.chugunov.testappcompose.presentation.utils

sealed class LoadingState {
    object Loading: LoadingState()
    object Loaded: LoadingState()
}