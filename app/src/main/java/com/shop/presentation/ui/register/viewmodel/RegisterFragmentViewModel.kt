package com.shop.presentation.ui.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shop.domain.firebase.BooleanCallback
import com.shop.domain.usecase.SignUpUseCase

class RegisterFragmentViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val mutableResult = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = mutableResult

    fun createAccount(email: String, pwd: String) {
        mutableResult.value = signUpUseCase.execute(email, pwd, object : BooleanCallback {
            override fun onCallback(status: Boolean) {
                mutableResult.value = status
            }
        })
    }
}