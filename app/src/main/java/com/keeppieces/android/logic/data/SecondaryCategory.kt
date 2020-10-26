package com.keeppieces.android.logic.data

import androidx.room.*

@Entity(
    tableName = "secondaryCategory",
    foreignKeys = [
        ForeignKey(entity = PrimaryCategory::class, parentColumns = ["primary_name"], childColumns = ["primary_category"])
    ],
    indices = [Index("primary_category"),(Index(value = ["secondary_name"], unique = true))]
)
data class SecondaryCategory(
    @ColumnInfo(name = "secondary_name") val name: String,
    @ColumnInfo(name = "primary_category") val primaryName: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "secondary_id")
    var secondaryId: Long = 0
}