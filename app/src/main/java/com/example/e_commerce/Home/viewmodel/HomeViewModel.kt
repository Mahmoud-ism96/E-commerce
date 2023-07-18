package com.example.e_commerce.Home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _brandsMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val brandsStateFlow: StateFlow<ApiState> get() = _brandsMutableStateFlow
    private val _productsByBrandMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val productsByBrandStateFlow get() = _productsByBrandMutableStateFlow
    var brandImage:String=""


    fun getBrands() {
        viewModelScope.launch {
            repo.getBrands()
                .catch { _brandsMutableStateFlow.value = ApiState.Failure(it.message!!) }
                .collect {
                    if (it.isSuccessful) {
                        _brandsMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

    fun getProductByBrand(brandId: Long) {
        viewModelScope.launch {
            repo.getProductsByBrand(brandId)
                .catch { _productsByBrandMutableStateFlow.value = ApiState.Failure(it.message!!) }
                .collectLatest {
                    _productsByBrandMutableStateFlow.value=ApiState.Success(it.body()!!)
                }
        }
    }

    fun getBrandImg(imageScr:String){
        brandImage=imageScr
    }
}