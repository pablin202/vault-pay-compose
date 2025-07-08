package com.pdm.vaultpay.di

import com.pdm.vaultpay.data.repository.AuthRepository
import com.pdm.vaultpay.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository(get()) }
    single { UserRepository(get()) }
}