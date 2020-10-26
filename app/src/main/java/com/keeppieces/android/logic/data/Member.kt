package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "member",
    indices = (arrayOf(Index(value = ["member_name"], unique = true)))
)
data class Member(
    @ColumnInfo(name = "member_name") val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "member_id")
    var memberId: Long = 0
}