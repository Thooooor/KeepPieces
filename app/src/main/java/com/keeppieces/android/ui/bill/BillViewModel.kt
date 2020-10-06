package com.keeppieces.android.ui.bill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.model.Bill

class BillViewModel : ViewModel() {
    private val billLiveData = MutableLiveData<Bill>()
    val billList = ArrayList<Bill>()

    fun addBill(bill: Bill) {
        billLiveData.value = bill
    }
}