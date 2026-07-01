package com.example.mendacium.ui.service

interface ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>
    data class Error(val message: String) : ApiResult<Nothing>
}
