package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "primaryCategory"
)
data class PrimaryCategory(
    @PrimaryKey @ColumnInfo(name = "primary_name") val name: String
) {
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "primary_id")
//    var primaryId: Long = 0
}