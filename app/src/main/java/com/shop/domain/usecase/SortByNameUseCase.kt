package com.shop.domain.usecase

import com.shop.domain.models.Product
import com.shop.domain.models.ProductArray

class SortByNameUseCase {
    fun sortName(productArray: ProductArray): List<Product> {
        return productArray.products.sortedWith(compareBy(Product::title))
    }

    fun sortNameDec(productArray: ProductArray): List<Product> {
        return productArray.products.sortedWith(compareByDescending(Product::title))
    }
}