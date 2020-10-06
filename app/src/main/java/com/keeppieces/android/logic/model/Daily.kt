package com.keeppieces.android.logic.model

import java.sql.Date
import java.sql.Time
import java.time.LocalDate

data class Daily(val time: Time, val bills: List<Bill>)

data class DailyResponse(val time: Time, val bills: List<Bill>)

data class DailyItem(val date: LocalDate, val type: String, val amount: Double) {
}