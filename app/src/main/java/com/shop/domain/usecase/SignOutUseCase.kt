package com.shop.domain.usecase

import com.shop.domain.firebase.IUserDatabase

class SignOutUseCase(private val userDatabase: IUserDatabase) {
    fun execute() : Boolean {
        return userDatabase.signOut().email == ""
    }
}