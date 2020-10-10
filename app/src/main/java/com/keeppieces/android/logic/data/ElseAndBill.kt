package com.keeppieces.android.logic.data

import androidx.room.Embedded
import androidx.room.Relation

data class AccountAndBill (
    @Embedded
    val account: Account,

    @Relation(parentColumn = "account_name", entityColumn = "account")
    val accountListFromBill: List<Account> = emptyList()
)

data class MemberAndBill (
    @Embedded
    val member: Member,

    @Relation(parentColumn = "member_name", entityColumn = "member")
    val memberListFromBill: List<Member> = emptyList()
)

data class PrimaryCategoryAndBill (
    @Embedded
    val primaryCategory: PrimaryCategory,

    @Relation(parentColumn = "primary_name", entityColumn = "primary_category")
    val primaryListFromBill: List<PrimaryCategory> = emptyList()
)

data class SecondCategoryAndBill (
    @Embedded
    val secondaryCategory: SecondaryCategory,

    @Relation(parentColumn = "secondary_name", entityColumn = "secondary_category")
    val secondaryListFromBill: List<SecondaryCategory> = emptyList()
)

data class TypeAndBill (
    @Embedded
    val type: Type,

    @Relation(parentColumn = "type_name", entityColumn = "type")
    val typeListFromBill: List<Type> = emptyList()
)