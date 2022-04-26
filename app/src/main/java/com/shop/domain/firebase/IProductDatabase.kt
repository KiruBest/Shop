package com.shop.domain.firebase

import com.shop.domain.models.Product
import com.shop.domain.models.ProductArray

interface IProductDatabase {
    fun readProduct(callback: GetArrayProductCallback): ProductArray
    fun writeProduct(product: Product)
}