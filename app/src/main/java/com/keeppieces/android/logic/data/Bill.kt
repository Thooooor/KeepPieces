package com.keeppieces.android.logic.data

import androidx.room.*

@Entity(
    tableName = "bill",
    foreignKeys = [
        ForeignKey(entity = Account::class, parentColumns = ["account_name"], childColumns = ["account"]),
        ForeignKey(entity = Member::class, parentColumns = ["member_name"], childColumns = ["member"]),
        ForeignKey(entity = PrimaryCategory::class, parentColumns = ["primary_name"], childColumns = ["primary_category"]),
        ForeignKey(entity = SecondaryCategory::class, parentColumns = ["secondary_name"], childColumns = ["secondary_category"]),
        ForeignKey(entity = Type::class, parentColumns = ["type_name"], childColumns = ["type"])
    ],
    indices = [
        Index("account"),
        Index("member"),
        Index("primary_category"),
        Index("secondary_category"),
        Index("type")
    ]
)
data class Bill(
    val date: String,
    val amount: Double,
    @ColumnInfo(name = "account") val account: String,
    @ColumnInfo(name = "member") val member: String,
    @ColumnInfo(name = "primary_category") val primaryCategory: String,
    @ColumnInfo(name = "secondary_category") val secondaryCategory: String,
    @ColumnInfo(name = "type") val type: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_id")
    var billId: Long = 0
}