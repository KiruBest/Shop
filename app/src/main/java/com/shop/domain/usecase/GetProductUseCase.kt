package com.shop.domain.usecase

import com.shop.domain.firebase.GetArrayProductCallback
import com.shop.domain.firebase.IProductDatabase
import com.shop.domain.models.ProductArray

class GetProductUseCase(private val productDatabase: IProductDatabase) {
    var productArray: ProductArray? = null

    fun execute(callback: GetArrayProductCallback) : ProductArray {
        if(productArray == null) productArray = productDatabase.readProduct(callback)
        return productArray!!
    }
}