package com.keeppieces.android.ui.bill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository

class BillViewModel() : ViewModel() {
    private val billLiveData = MutableLiveData<Bill>()

    init {

    }
    fun addBill(bill: Bill) {
        billLiveData.value = bill
    }
}