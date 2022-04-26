package com.shop.presentation.ui.register.viewmodel

import androidx.lifecycle.ViewModel
import com.shop.domain.firebase.BooleanCallback
import com.shop.domain.usecase.SignInUseCase

class AuthFragmentViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    fun auth(email: String, pwd: String, callback: BooleanCallback) {
        signInUseCase.execute(email = email, pwd = pwd, callback = callback)
    }

}