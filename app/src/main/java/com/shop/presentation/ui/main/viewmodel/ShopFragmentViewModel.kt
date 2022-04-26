package com.shop.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shop.domain.models.ProductArray
import com.shop.domain.usecase.GetProductUseCase

class ShopFragmentViewModel(
    private val getProductUseCase: GetProductUseCase,
) : ViewModel() {

    private val productsMutableLiveData = MutableLiveData<ProductArray>()
    val productsLiveData: LiveData<ProductArray> = productsMutableLiveData

    fun getProducts() {

    }

}