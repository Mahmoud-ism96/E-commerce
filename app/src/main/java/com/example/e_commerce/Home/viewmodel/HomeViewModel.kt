package com.example.e_commerce.Home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.currency.ApiCurrencyClient
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.utility.Functions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

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

    private val _usdAmountMutableStateFlow : MutableStateFlow<Double> = MutableStateFlow(0.0)
    val usdAmountStateFlow:StateFlow<Double> get() = _usdAmountMutableStateFlow

    init {
        convertCurrency()
    }

    fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getBrands()
                    .catch { _brandsMutableStateFlow.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _brandsMutableStateFlow.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (e: SocketTimeoutException) {
                _brandsMutableStateFlow.value = ApiState.Failure(Throwable("Poor Connection"))
            }
        }
    }

    fun getProductById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
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
        try {
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
        } catch (e: SocketTimeoutException) {
            _brandsMutableStateFlow.value = ApiState.Failure(Throwable("Poor Connection"))
        }
    }

    fun readFromSP(key: String): String {
        return repo.readStringFromSettingSP(key)
    }

    fun writeToSP(key: String, value: String) {
        repo.writeStringToSettingSP(key, value)
    }

    fun convertCurrency() {
        viewModelScope.launch {
             _usdAmountMutableStateFlow.value=ApiCurrencyClient.convertCurrency("1", "USD")
        }
    }
}