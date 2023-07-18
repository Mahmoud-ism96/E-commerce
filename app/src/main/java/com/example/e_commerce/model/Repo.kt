package com.example.e_commerce.model

import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repo private constructor(private val remoteSource: RemoteSource): RepoInterface {

    companion object{
        private var instance: Repo? = null

        fun getInstance(remoteSource: RemoteSource): Repo{
            return instance?: synchronized(this){
                instance?: Repo(remoteSource).also { instance = it }
            }
        }
    }

    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        return remoteSource.getAllProducts()
    }
}