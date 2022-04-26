package com.shop.domain.usecase

import com.shop.domain.firebase.BooleanCallback
import com.shop.domain.firebase.IUserDatabase

class SignInUseCase(private val userDatabase: IUserDatabase) {
    fun execute(email: String, pwd: String, callback: BooleanCallback) : Boolean {
        return userDatabase.signIn(email = email, password = pwd, callback = callback)
    }
}