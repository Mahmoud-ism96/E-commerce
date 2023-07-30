package com.example.e_commerce.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class AuthViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _emailCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val emailCustomerMutableStateFlow: StateFlow<ApiState> get() = _emailCustomerMutableStateFlow

    private val _gmailCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val gmailCustomerMutableStateFlow: StateFlow<ApiState> get() = _gmailCustomerMutableStateFlow

    private val _createDraftStatusMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val createDraftStatusStateFlow: StateFlow<ApiState> get() = _createDraftStatusMutableStateFlow

    private val _createGoogleDraftStatusMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val createGoogleDraftStatusStateFlow: StateFlow<ApiState> get() = _createGoogleDraftStatusMutableStateFlow

    private val _modifyCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyCustomerMutableStateFlow: StateFlow<ApiState> get() = _modifyCustomerMutableStateFlow

    private val _modifyGoogleCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val modifyGoogleCustomerMutableStateFlow: StateFlow<ApiState> get() = _modifyGoogleCustomerMutableStateFlow

    private val _loggedCustomerStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val loggedCustomerStateFlow: StateFlow<ApiState> get() = _loggedCustomerStateFlow

    fun createNewEmailCustomer(customerData: CustomerData) {
        viewModelScope.launch {
            try {
                repo.createCustomer(customerData)
                    .catch { _emailCustomerMutableStateFlow.value = ApiState.Failure(it) }.collect {
                        if (it.isSuccessful) {
                            _emailCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                        }
                    }
            } catch (_: SocketTimeoutException) {
                _emailCustomerMutableStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                createNewEmailCustomer(customerData)
            }
        }
    }

    fun modifyGoogleCustomer(customerId: Long, customerData: CustomerData) {
        viewModelScope.launch {
            try {
                repo.modifyCustomer(customerId, customerData).catch {
                    _modifyGoogleCustomerMutableStateFlow.value = ApiState.Failure(it)
                }.collect {
                    if (it.isSuccessful) {
                        _modifyGoogleCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _modifyGoogleCustomerMutableStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            } catch (_: SocketTimeoutException) {
                _modifyGoogleCustomerMutableStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                modifyGoogleCustomer(customerId, customerData)
            }
        }
    }

    fun modifyCustomer(customerId: Long, customerData: CustomerData) {
        viewModelScope.launch {
            try {
                repo.modifyCustomer(customerId, customerData)
                    .catch { _modifyCustomerMutableStateFlow.value = ApiState.Failure(it) }
                    .collect {
                        if (it.isSuccessful) {
                            _modifyCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                            Log.i("TAG", "firebaseAuthWithGoogle: Success")
                        } else {
                            _modifyCustomerMutableStateFlow.value =
                                ApiState.Failure(Throwable(it.code().toString()))
                        }
                    }
            } catch (_: SocketTimeoutException) {
                _modifyCustomerMutableStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                modifyCustomer(customerId, customerData)
            }
        }
    }

    fun createGmailEmailCustomer(customerData: CustomerData) {
        viewModelScope.launch {
            try {
                repo.createCustomer(customerData).catch {
                    _gmailCustomerMutableStateFlow.value = ApiState.Failure(it)
                }.collect {
                    if (it.isSuccessful) {
                        _gmailCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _gmailCustomerMutableStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            } catch (_: SocketTimeoutException) {
                _gmailCustomerMutableStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                createGmailEmailCustomer(customerData)
            }
        }
    }

    fun createDraftOrder(draft_order: SendDraftRequest) {
        viewModelScope.launch {
            try {
                repo.createDraftOrder(draft_order).collectLatest {
                    if (it.isSuccessful) {
                        _createDraftStatusMutableStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _createDraftStatusMutableStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            } catch (_: SocketTimeoutException) {
                _createDraftStatusMutableStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                createDraftOrder(draft_order)
            }
        }
    }

    fun createGoogleDraftOrder(draft_order: SendDraftRequest) {
        viewModelScope.launch {
            try {
                repo.createDraftOrder(draft_order).collectLatest {
                    if (it.isSuccessful) {
                        _createGoogleDraftStatusMutableStateFlow.value =
                            ApiState.Success(it.body()!!)
                    } else {
                        _createGoogleDraftStatusMutableStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            } catch (_: SocketTimeoutException) {
                _createGoogleDraftStatusMutableStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                createGoogleDraftOrder(draft_order)
            }
        }
    }

    fun getCustomerData(customerEmail: String, customerName: String) {
        viewModelScope.launch {
            try {
                repo.getCustomerByEmailAndName(customerEmail, customerName).collectLatest {
                    if (it.isSuccessful) {
                        _loggedCustomerStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _loggedCustomerStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            }catch (_:SocketTimeoutException){
                _loggedCustomerStateFlow.value =
                    ApiState.Failure(Throwable("poor connection"))
                getCustomerData(customerEmail, customerName)
            }
        }
    }

    fun writeStringToSettingSP(key: String, value: String) {
        repo.writeStringToSettingSP(key, value)
    }

    fun readStringFromSettingSP(key: String): String {
        return repo.readStringFromSettingSP(key)
    }

}