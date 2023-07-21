package com.example.e_commerce.home.viewmodel

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
    private val _productsByIdMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val productsByIdStateFlow get() = _productsByIdMutableStateFlow
    var brandImage: Any? = null

    private val _pricesRulesMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val pricesRulesStateFlow: StateFlow<ApiState> get() = _pricesRulesMutableStateFlow

    fun getBrands() {
        viewModelScope.launch {
            repo.getBrands()
                .catch { _brandsMutableStateFlow.value = ApiState.Failure(it) }
                .collect {
                    if (it.isSuccessful) {
                        _brandsMutableStateFlow.value = ApiState.Success(it.body()!!)
                    }
                }
        }
    }

    fun getProductById(id: Long) {
        viewModelScope.launch {
            repo.getProductsById(id)
                .catch { _productsByIdMutableStateFlow.value = ApiState.Failure(it) }
                .collectLatest {
                    _productsByIdMutableStateFlow.value = ApiState.Success(it.body()!!)
                }
        }
    }

    fun getBrandImg(imageScr: Any) {
        brandImage = imageScr
    }

    fun getPriceRules() {
        viewModelScope.launch {
            repo.getAllPricesRules().catch {
                _pricesRulesMutableStateFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _pricesRulesMutableStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    _pricesRulesMutableStateFlow.value =
                        ApiState.Failure(Throwable(it.code().toString()))
                }
            }
        }
    }
}