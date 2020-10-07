package com.keeppieces.android.ui.daily

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.BillRepository

class DailyViewModel @ViewModelInject internal constructor(): ViewModel() {
    val billList = BillRepository().loadBills()
}