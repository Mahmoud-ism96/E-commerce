package com.example.e_commerce.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _productList: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val productList: StateFlow<ApiState> get() = _productList

    private val _brandHint: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val brandHint: StateFlow<ApiState> get() = _brandHint

    fun getAllProducts() {
        viewModelScope.launch {
            repo.getAllProducts()
                .catch { _productList.value = ApiState.Failure(it) }
                .collect {
                    if (it.isSuccessful) {
                        _productList.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

    fun getAllBrands() {
        viewModelScope.launch {
            repo.getBrands()
                .catch { _brandHint.value = ApiState.Failure(it) }
                .collect {
                    if (it.isSuccessful) {
                        _brandHint.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }


}