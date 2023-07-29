package com.example.e_commerce.wishlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WishListViewModel (private val repo: RepoInterface) : ViewModel() {
    private val _wishlistDraftOrderFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val wishlistDraftOrderFlow: StateFlow<ApiState> get() = _wishlistDraftOrderFlow

    private val _modifyDraftOrderFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyDraftOrderFlow: StateFlow<ApiState> get() = _modifyDraftOrderFlow

    fun getDraftOrderByDraftId(draftId: Long) {
        viewModelScope.launch {
            repo.getDraftOrderByDraftId(draftId).catch {
                _wishlistDraftOrderFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _wishlistDraftOrderFlow.value = ApiState.Success(it.body()!!)
                }else{
                    _wishlistDraftOrderFlow.value = ApiState.Failure(Throwable(it.code().toString()))
                }
            }
        }
    }

    fun removeLineItem(draftOrderID : Long, sendDraftRequest: SendDraftRequest){
        viewModelScope.launch {
            repo.modifyDraftOrder(draftOrderID,sendDraftRequest).catch {
                _wishlistDraftOrderFlow.value = ApiState.Failure(it)
            }.collect {
                if (it.isSuccessful) {
                    _modifyDraftOrderFlow.value = ApiState.Success(it.body()!!)
                }else{
                    _modifyDraftOrderFlow.value = ApiState.Failure(Throwable(it.code().toString()))
                }
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
        _wishlistDraftOrderFlow.value = ApiState.Loading
    }
}