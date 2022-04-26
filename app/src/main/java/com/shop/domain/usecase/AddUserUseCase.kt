package com.shop.domain.usecase

import com.shop.domain.firebase.IUserDatabase
import com.shop.domain.models.CurrentUser

class AddUserUseCase(private val userDatabase: IUserDatabase) {
    fun execute(uid: String, currentUser: CurrentUser) : Boolean {
        return userDatabase.writeUser(uid, currentUser)
    }
}