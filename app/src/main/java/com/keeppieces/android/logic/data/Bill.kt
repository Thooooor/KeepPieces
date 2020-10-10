package com.keeppieces.android.logic.data

import androidx.room.*

@Entity(
    tableName = "bill",
    foreignKeys = [
        ForeignKey(entity = Account::class, parentColumns = ["account_name"], childColumns = ["account"]),
        ForeignKey(entity = Member::class, parentColumns = ["member_name"], childColumns = ["member"]),
        ForeignKey(entity = PrimaryCategory::class, parentColumns = ["primary_name"], childColumns = ["primary_category"])
    ],
)
data class Bill(
    val date: String,
    val amount: Double,
    @ColumnInfo(name = "account") val account: String,
    @ColumnInfo(name = "member") val member: String,
    @ColumnInfo(name = "primary_category") val primaryCategory: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_id")
    var billId: Long = 0
}