package com.shop

import android.app.Application
import com.shop.presentation.di.appModule
import com.shop.presentation.di.dataModule
import com.shop.presentation.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, appModule))
        }
    }
}