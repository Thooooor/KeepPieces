package com.keeppieces.android.logic.model

import java.sql.Time


data class MonthlyResponse(val start: Time, val end: Time, val bills: List<Bill>) {
    data class Monthly(val start: Time, val end: Time, val bills: List<Bill>)

    data class Result(val monthly: Monthly)

}