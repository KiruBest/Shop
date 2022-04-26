package com.shop.domain.usecase

import com.shop.domain.firebase.BooleanCallback
import com.shop.domain.firebase.IUserDatabase

class SignUpUseCase(private val userDatabase: IUserDatabase) {
    fun execute(email: String, pwd: String, callback: BooleanCallback) : Boolean {
        return userDatabase.createAccount(email, pwd, callback)
    }
}