package com.keeppieces.android.logic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Bill(
    val date: Date,
    val amount: Double,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}