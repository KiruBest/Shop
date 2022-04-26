package com.shop.domain.firebase

import com.shop.domain.models.CurrentUser
import com.shop.domain.models.ProductArray

interface BooleanCallback {
    fun onCallback(status: Boolean)
}

interface GetArrayProductCallback {
    fun onCallback(productArray: ProductArray)
}

interface GetCurrentUserCallback {
    fun onCallback(currentUser: CurrentUser)
}