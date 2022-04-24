package com.shop.sort

import com.shop.models.Product

class Sorting {
    fun sortPrice(): List<Product>? {
        return Product.products?.sortedWith(compareBy { it.price })
    }

    fun sorPriceDec() : List<Product>?{
        return Product.products?.sortedWith(compareByDescending(Product::price))
    }

    fun sortName() : List<Product>?{
        return Product.products?.sortedWith(compareBy(Product::title))
    }

    fun sortNameDec() : List<Product>?{
        return Product.products?.sortedWith(compareByDescending(Product::title))
    }
}
