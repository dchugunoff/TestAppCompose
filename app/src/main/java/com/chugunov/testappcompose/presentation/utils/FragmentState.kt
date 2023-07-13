package com.chugunov.testappcompose.presentation.utils

/**
 * Sealed класс для состояния фрагментов
 */
sealed class FragmentState() {
    object MainFragmentState : FragmentState()
    object SecondFragmentState : FragmentState()
}