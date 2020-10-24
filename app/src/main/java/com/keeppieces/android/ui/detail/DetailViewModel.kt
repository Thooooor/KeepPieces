package com.keeppieces.android.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.BillRepository

class DetailViewModel @ViewModelInject internal constructor(): ViewModel(){
    fun billList(startDate:String, endDate: String) = BillRepository().getPeriodBill(startDate, endDate)
}