package com.shop.domain.usecase

import com.shop.domain.models.Product
import com.shop.domain.models.ProductArray

class SortByPriceUseCase {
    fun sortPrice(productArray: ProductArray): List<Product> {
        return productArray.products.sortedWith(compareBy { it.price })
    }

    fun sorPriceDec(productArray: ProductArray): List<Product> {
        return productArray.products.sortedWith(compareByDescending(Product::price))
    }
}