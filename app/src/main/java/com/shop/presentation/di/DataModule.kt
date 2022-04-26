package com.shop.presentation.di

import com.google.firebase.auth.FirebaseAuth
import com.shop.data.firebase.ProductDatabase
import com.shop.data.firebase.UserDatabase
import com.shop.domain.firebase.IProductDatabase
import com.shop.domain.firebase.IUserDatabase
import org.koin.dsl.module

val dataModule = module {
    single<IUserDatabase> {
        UserDatabase(auth = FirebaseAuth.getInstance())
    }

    single<IProductDatabase> {
        ProductDatabase()
    }
}