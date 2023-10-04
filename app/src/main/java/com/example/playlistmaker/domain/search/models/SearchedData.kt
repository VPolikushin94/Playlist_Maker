package com.example.playlistmaker.domain.search.models

sealed interface SearchedData<T> {
    data class Success<T>(val data: T): SearchedData<T>
    data class Error<T>(val message: String?): SearchedData<T>
}
