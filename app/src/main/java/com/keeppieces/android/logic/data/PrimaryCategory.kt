package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "primary_category"
)
data class PrimaryCategory(
    @ColumnInfo(name = "primary_name") val name: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_id")
    val primaryId: Long = 0
}