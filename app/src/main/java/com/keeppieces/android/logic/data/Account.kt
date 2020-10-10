package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "account"
)
data class Account(
    @ColumnInfo(name = "account_name") val name: String,
    val amount: Double = 0.0,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    var accountId: Long = 0
}