package com.pdm.vaultpay

import android.app.Application
import com.pdm.vaultpay.di.networkModule
import com.pdm.vaultpay.di.repositoryModule
import com.pdm.vaultpay.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class VaultPayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VaultPayApp)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}