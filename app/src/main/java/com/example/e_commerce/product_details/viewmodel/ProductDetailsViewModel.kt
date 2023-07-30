package com.example.e_commerce.product_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ProductDetailsViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _productDetailsState: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val productDetailsState: StateFlow<ApiState> get() = _productDetailsState

    private val _modifyCartDraftOrderState: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyCartDraftOrderState: StateFlow<ApiState> get() = _modifyCartDraftOrderState

    private val _modifyWishlistDraftOrderState: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyWishlistDraftOrderState: StateFlow<ApiState> get() = _modifyWishlistDraftOrderState

    private val _cartDraftOrdersState: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val cartDraftOrdersState: StateFlow<ApiState> get() = _cartDraftOrdersState

    private val _wishlistDraftOrdersState: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val wishlistDraftOrdersState: StateFlow<ApiState> get() = _wishlistDraftOrdersState


    fun getProductDetails(productID: Long) {
        viewModelScope.launch {
            try {
                repo.getProductById(productID)
                    .catch { _productDetailsState.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _productDetailsState.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (_: SocketTimeoutException) {
                getProductDetails(productID)
            }
        }
    }

    fun getCartDraftOrders(draftOrderId: Long) {
        viewModelScope.launch {
            try {
                repo.getDraftOrderByDraftId(draftOrderId)
                    .catch { _cartDraftOrdersState.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _cartDraftOrdersState.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (_: SocketTimeoutException) {
                getCartDraftOrders(draftOrderId)
            }
        }
    }

    fun getWishlistDraftOrders(draftOrderId: Long) {
        viewModelScope.launch {
            try {
                repo.getDraftOrderByDraftId(draftOrderId)
                    .catch { _wishlistDraftOrdersState.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _wishlistDraftOrdersState.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (_: SocketTimeoutException) {
                getWishlistDraftOrders(draftOrderId)
            }
        }
    }

    fun modifyCartDraftOrder(draftOrderId: Long, sendDraftRequest: SendDraftRequest) {
        viewModelScope.launch {
            try {
                repo.modifyDraftOrder(draftOrderId, sendDraftRequest)
                    .catch { _modifyCartDraftOrderState.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _modifyCartDraftOrderState.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (_: SocketTimeoutException) {
                modifyCartDraftOrder(draftOrderId, sendDraftRequest)
            }
        }
    }

    fun modifyWishlistDraftOrder(draftOrderId: Long, sendDraftRequest: SendDraftRequest) {
        viewModelScope.launch {
            try {
                repo.modifyDraftOrder(draftOrderId, sendDraftRequest)
                    .catch { _modifyWishlistDraftOrderState.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _modifyWishlistDraftOrderState.value = ApiState.Success(it.body()!!)
                        }
                    }
            }catch (_:SocketTimeoutException){
                modifyWishlistDraftOrder(draftOrderId, sendDraftRequest)
            }
        }
    }

    fun writeStringToSettingSP(key: String, value: String) {
        repo.writeStringToSettingSP(key, value)
    }

    fun readStringFromSettingSP(key: String): String {
        return repo.readStringFromSettingSP(key)
    }

    fun resetState() {
        _productDetailsState.value = ApiState.Loading
    }
}