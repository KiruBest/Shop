package com.shop.domain.usecase

import com.shop.domain.firebase.GetCurrentUserCallback
import com.shop.domain.firebase.IUserDatabase
import com.shop.domain.models.CurrentUser

class GetCurrentUserUseCase(private val userDatabase: IUserDatabase) {
    private var currentUser: CurrentUser? = null

    fun execute(uid: String, callback: GetCurrentUserCallback): CurrentUser {
        if (currentUser == null) currentUser = userDatabase.readCurrentUser(uid, callback)
        return currentUser!!
    }
}