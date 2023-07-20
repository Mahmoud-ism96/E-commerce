package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repo private constructor(private val remoteSource: RemoteSource) : RepoInterface {

    companion object {
        private var instance: Repo? = null

        fun getInstance(remoteSource: RemoteSource): Repo {
            return instance ?: synchronized(this) {
                instance ?: Repo(remoteSource).also { instance = it }
            }
        }
    }

    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        return remoteSource.getAllProducts()
    }

    override suspend fun getBrands(): Flow<Response<BrandsResponse>> {
        return remoteSource.getBrand()
    }

    override suspend fun getProductsById(id: Long): Flow<Response<ProductsResponse>> {
        return remoteSource.getProductsById(id)
    }

    override suspend fun getProductsByTitle(title: String): Flow<Response<ProductsResponse>> {
        return remoteSource.getProductsByTitle(title)
    }

    override suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>> {
        return remoteSource.createCustomer(customerData)
    }

    override suspend fun getCustomerByEmailAndName(
        email: String, name: String
    ): Flow<Response<CustomerResponse>> {
        return remoteSource.getCustomerByEmailAndName(email, name)
    }
}