package com.example.e_commerce.authentication.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.model.repo.RepoInterface

class SignUpViewModelFactory (private val repo: RepoInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(repo) as T
        }
        return super.create(modelClass, extras)
    }
}