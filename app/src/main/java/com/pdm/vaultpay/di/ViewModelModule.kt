package com.pdm.vaultpay.di

import com.pdm.vaultpay.ui.forgot_password.ForgotPasswordViewModel
import com.pdm.vaultpay.ui.login.LoginViewModel
import com.pdm.vaultpay.ui.mfa.MfaViewModel
import com.pdm.vaultpay.ui.profile.ProfileViewModel
import com.pdm.vaultpay.ui.signup.SignupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), androidContext()) }
    viewModel { SignupViewModel(get()) }
    viewModel { MfaViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
}