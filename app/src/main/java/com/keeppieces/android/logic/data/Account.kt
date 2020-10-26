package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "account",
    indices = (arrayOf(Index(value = ["account_name"], unique = true)))
)
data class Account(
        @ColumnInfo(name = "account_name") val name: String,
        val amount: Double,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    var accountId: Long = 0
}