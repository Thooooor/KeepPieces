package com.keeppieces.android.logic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "member"
)
data class Member(
    @ColumnInfo(name = "member_name") val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "member_id")
    val memberId: Long = 0
}