package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "type",
    indices = (arrayOf(Index(value = ["type_name"], unique = true)))
)
data class Type(
    @ColumnInfo(name = "type_name") val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "type_id")
    var typeId: Long = 0
}