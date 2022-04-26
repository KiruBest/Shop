package com.shop.presentation.di

import com.shop.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory<SignInUseCase> {
        SignInUseCase(userDatabase = get())
    }

    factory<GetCurrentUserUseCase> {
        GetCurrentUserUseCase(userDatabase = get())
    }

    factory<AddUserUseCase> {
        AddUserUseCase(userDatabase = get())
    }

    factory<SignUpUseCase> {
        SignUpUseCase(userDatabase = get())
    }

    factory<GetProductUseCase> {
        GetProductUseCase(productDatabase = get())
    }

    factory<SignOutUseCase> {
        SignOutUseCase(userDatabase = get())
    }

    factory<SortByNameUseCase> {
        SortByNameUseCase()
    }

    factory <SortByPriceUseCase> {
        SortByPriceUseCase()
    }
}