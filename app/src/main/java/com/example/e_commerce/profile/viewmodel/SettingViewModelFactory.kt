package com.example.e_commerce.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.model.repo.RepoInterface
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel

class SettingViewModelFactory (private val repo: RepoInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(repo) as T
        }
        return super.create(modelClass, extras)
    }
}