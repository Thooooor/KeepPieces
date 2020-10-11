package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "type"
)
data class Type(
    @PrimaryKey @ColumnInfo(name = "type_name") val name: String
) {
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "type_id")
//    var typeId: Long = 0
}