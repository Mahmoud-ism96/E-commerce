package com.example.e_commerce.wishlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.e_commerce.model.repo.RepoInterface

class WishListViewModelFactory(private val repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(WishListViewModel::class.java)) {
            return WishListViewModel(repo) as T
        }
        return super.create(modelClass, extras)
    }
}