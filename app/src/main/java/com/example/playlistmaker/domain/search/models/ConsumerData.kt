package com.example.playlistmaker.domain.search.models

sealed interface ConsumerData<T> {
    data class Success<T>(val data: T): ConsumerData<T>
    data class Error<T>(val message: String?): ConsumerData<T>
}
