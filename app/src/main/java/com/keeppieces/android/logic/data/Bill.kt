package com.keeppieces.android.logic.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bill(
    val date: String,
    val amount: Double,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}