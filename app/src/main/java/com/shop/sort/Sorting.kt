package com.shop.sort

import com.shop.models.BasketProduct
import com.shop.models.FavoriteProduct
import com.shop.models.Product

class Sorting {
    fun sortPrice(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareBy(Product::price))
    }

    @JvmName("sortPrice1")
    fun sortPrice(products: MutableList<BasketProduct>): List<BasketProduct> {
        return products.sortedWith(compareBy(BasketProduct::price))
    }

    @JvmName("sortPrice2")
    fun sortPrice(products: MutableList<FavoriteProduct>): List<FavoriteProduct> {
        return products.sortedWith(compareBy(FavoriteProduct::price))
    }

    @JvmName("sorPriceDec1")
    fun sortPriceDec(products: MutableList<BasketProduct>): List<BasketProduct> {
        return products.sortedWith(compareByDescending(BasketProduct::price))
    }

    @JvmName("sorPriceDec2")
    fun sortPriceDec(products: MutableList<FavoriteProduct>): List<FavoriteProduct> {
        return products.sortedWith(compareByDescending(FavoriteProduct::price))
    }

    fun sortPriceDec(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareByDescending(Product::price))
    }

    fun sortName(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareBy(Product::title))
    }

    @JvmName("sortName1")
    fun sortName(products: MutableList<BasketProduct>): List<BasketProduct> {
        return products.sortedWith(compareBy(BasketProduct::title))
    }

    @JvmName("sortName2")
    fun sortName(products: MutableList<FavoriteProduct>): List<FavoriteProduct> {
        return products.sortedWith(compareBy(FavoriteProduct::title))
    }

    fun sortNameDec(products: MutableList<Product>): List<Product> {
        return products.sortedWith(compareByDescending(Product::title))
    }

    @JvmName("sortNameDec1")
    fun sortNameDec(products: MutableList<BasketProduct>): List<BasketProduct> {
        return products.sortedWith(compareByDescending(BasketProduct::title))
    }

    @JvmName("sortNameDec2")
    fun sortNameDec(products: MutableList<FavoriteProduct>): List<FavoriteProduct> {
        return products.sortedWith(compareByDescending(FavoriteProduct::title))
    }
}
