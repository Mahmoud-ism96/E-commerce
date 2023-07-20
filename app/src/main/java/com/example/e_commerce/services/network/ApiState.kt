package com.example.e_commerce.services.network

sealed class ApiState{
    class Success(val data: Any): ApiState()
    class Failure(val throwable: Throwable): ApiState()
    object Loading: ApiState()
}
