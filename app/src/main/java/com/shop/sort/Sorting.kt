package com.shop.sort

import com.shop.models.Product

class Sorting {
    fun sortPrice(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareBy(Product::price))
    }

    fun sortPriceDec(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareByDescending(Product::price))
    }

    fun sortName(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareBy(Product::title))
    }

    fun sortNameDec(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareByDescending(Product::title))
    }
}
