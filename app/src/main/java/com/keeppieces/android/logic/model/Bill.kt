package com.keeppieces.android.logic.model

import android.location.Location
import java.sql.Time

data class BillResponse(val bills: List<Bill>)

data class Bill (
    val time: Time,
    val type: Type,
    val amount: Float,
    val user: User,
    val location: Location
)