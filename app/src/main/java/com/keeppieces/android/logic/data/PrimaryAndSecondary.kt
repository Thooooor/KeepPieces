package com.keeppieces.android.logic.data

import androidx.room.Embedded
import androidx.room.Relation

data class PrimaryAndSecondary (
    @Embedded
    val primaryCategory: PrimaryCategory,

    @Relation(parentColumn = "primary_name", entityColumn = "secondary_category")
    val secondaryCategory: List<SecondaryCategory> = emptyList()
)