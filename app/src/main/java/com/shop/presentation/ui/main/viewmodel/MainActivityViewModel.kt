package com.shop.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.domain.firebase.GetArrayProductCallback
import com.shop.domain.firebase.GetCurrentUserCallback
import com.shop.domain.models.CurrentUser
import com.shop.domain.models.Product
import com.shop.domain.models.ProductArray
import com.shop.domain.usecase.*

class MainActivityViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val sortByNameUseCase: SortByNameUseCase,
    private val sortByPriceUseCase: SortByPriceUseCase
) : ViewModel() {
    private var booleanMutableLiveData = MutableLiveData<Boolean>()
    val booleanLiveData: LiveData<Boolean> = booleanMutableLiveData

    private var currentUserMutableLiveData = MutableLiveData<CurrentUser>()
    val currentUserLiveData: LiveData<CurrentUser> = currentUserMutableLiveData

    private val productsMutableLiveData = MutableLiveData<ProductArray>()
    val productsLiveData: LiveData<ProductArray> = productsMutableLiveData

    fun getCurrentUser() {
        currentUserMutableLiveData.value =
            getCurrentUserUseCase.execute(Firebase.auth.currentUser?.uid.toString(),
                object : GetCurrentUserCallback {
                    override fun onCallback(currentUser: CurrentUser) {
                        currentUserMutableLiveData.value = currentUser
                    }
                })
    }

    fun signOut() {
        booleanMutableLiveData.value = signOutUseCase.execute()
    }

    fun getProducts() {
        productsMutableLiveData.value = getProductUseCase.execute(object : GetArrayProductCallback {
            override fun onCallback(productArray: ProductArray) {
                productsMutableLiveData.value = productArray
            }
        })
    }

    fun sortByName() {
        val sortProductArray = ProductArray(mutableListOf<Product>())
        sortProductArray.products.addAll(sortByNameUseCase.sortName(productsMutableLiveData.value!!))
        productsMutableLiveData.value = sortProductArray
    }

    fun sortByNameDec() {
        val sortProductArray = ProductArray(mutableListOf<Product>())
        sortProductArray.products.addAll(sortByNameUseCase.sortNameDec(productsMutableLiveData.value!!))
        productsMutableLiveData.value = sortProductArray
    }

    fun sortPrice() {
        val sortProductArray = ProductArray(mutableListOf<Product>())
        sortProductArray.products.addAll(sortByPriceUseCase.sortPrice(productsMutableLiveData.value!!))
        productsMutableLiveData.value = sortProductArray
    }

    fun sortByPriceDec() {
        val sortProductArray = ProductArray(mutableListOf<Product>())
        sortProductArray.products.addAll(sortByPriceUseCase.sorPriceDec(productsMutableLiveData.value!!))
        productsMutableLiveData.value = sortProductArray
    }
}