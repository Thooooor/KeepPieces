package com.keeppieces.android.logic.model

data class AccountResponse(val accounts: List<Account>)

data class Account (val name: String, val amount: Float, val user: User, val bills: List<Bill>)