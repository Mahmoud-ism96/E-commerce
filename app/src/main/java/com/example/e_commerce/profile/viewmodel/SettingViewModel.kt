package com.example.e_commerce.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.services.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class SettingViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _addressesMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val addressesStateFlow: StateFlow<ApiState> get() = _addressesMutableStateFlow

    private val _currentCustomerMutableStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val currentCustomerStateFlow: StateFlow<ApiState> get() = _currentCustomerMutableStateFlow

    fun getAddressesForCustomer(customer_id: String) {
        try {
            viewModelScope.launch {
                repo.getAddressesForCustomer(customer_id).catch {
                    _addressesMutableStateFlow.value = ApiState.Failure(it)
                }.collect {
                    if (it.isSuccessful) {
                        _addressesMutableStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _addressesMutableStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            }
        } catch (_: SocketTimeoutException) {
            _addressesMutableStateFlow.value = ApiState.Failure(Throwable("Poor Connection"))
        }
    }

    fun getCurrentCustomer(email: String, name: String) {
        try {
            viewModelScope.launch {
                repo.getCustomerByEmailAndName(email, name).catch {
                    _currentCustomerMutableStateFlow.value = ApiState.Failure(it)
                }.collect {
                    if (it.isSuccessful) {
                        _currentCustomerMutableStateFlow.value = ApiState.Success(it.body()!!)
                    } else {
                        _currentCustomerMutableStateFlow.value =
                            ApiState.Failure(Throwable(it.code().toString()))
                    }
                }
            }
        } catch (_: SocketTimeoutException) {
            _currentCustomerMutableStateFlow.value = ApiState.Failure(Throwable("Poor Connection"))
        }
    }

    fun createAddressForCustomer(customer_id: String, sendAddress: SendAddressDTO) {
        try {
            viewModelScope.launch {
                repo.createAddressForCustomer(customer_id, sendAddress)
                getAddressesForCustomer(customer_id)
            }
        } catch (_: SocketTimeoutException) {

        }
    }

    fun makeAddressDefault(customer_id: String, address_id: String) {
        try {
            viewModelScope.launch {
                repo.makeAddressDefault(customer_id, address_id)
                getAddressesForCustomer(customer_id)

            }
        } catch (_: SocketTimeoutException) {

        }
    }

    fun deleteAddressForCustomer(customer_id: String, address_id: String) {
        try {
            viewModelScope.launch {
                repo.deleteAddressForCustomer(customer_id, address_id)
                getAddressesForCustomer(customer_id)
            }
        }catch (_: SocketTimeoutException){}
    }

    fun writeStringToSettingSP(key: String, value: String) {
        repo.writeStringToSettingSP(key, value)
    }

    fun readStringFromSettingSP(key: String): String {
        return repo.readStringFromSettingSP(key)
    }
}