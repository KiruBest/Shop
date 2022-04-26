package com.shop.presentation.di

import com.shop.presentation.ui.main.viewmodel.MainActivityViewModel
import com.shop.presentation.ui.main.viewmodel.ShopFragmentViewModel
import com.shop.presentation.ui.register.viewmodel.AuthFragmentViewModel
import com.shop.presentation.ui.register.viewmodel.RegisterFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<AuthFragmentViewModel> {
        AuthFragmentViewModel(signInUseCase = get())
    }

    viewModel<RegisterFragmentViewModel> {
        RegisterFragmentViewModel(signUpUseCase = get())
    }

    viewModel<ShopFragmentViewModel> {
        ShopFragmentViewModel(getProductUseCase = get())
    }

    viewModel<MainActivityViewModel>() {
        MainActivityViewModel(getCurrentUserUseCase = get(),
            signOutUseCase = get(),
            getProductUseCase = get(),
            sortByNameUseCase = get(),
            sortByPriceUseCase = get()
        )
    }

}