package com.keeppieces.android.logic

import com.keeppieces.android.logic.model.Bill
import com.keeppieces.android.logic.model.DailyItem
import java.time.LocalDate

object Repository {
    fun getDailyItems(date: LocalDate): List<DailyItem> {
        val items = listOf(
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99),
            DailyItem(date, "Lunch", 52.3),
            DailyItem(date, "Drink", 1.99)
        )
        print(items)
        return items
    }
}