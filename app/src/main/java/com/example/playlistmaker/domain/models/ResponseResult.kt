package com.example.playlistmaker.domain.models

sealed class ResponseResult<T>() {
    class NothingFound<T>: ResponseResult<T>()
    class Success<T>(val data: T): ResponseResult<T>()
    class Error<T>(): ResponseResult<T>()
}