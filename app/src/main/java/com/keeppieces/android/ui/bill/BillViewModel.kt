package com.keeppieces.android.ui.bill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository

class BillViewModel(billRepository: BillRepository) : ViewModel() {
    private val billLiveData = MutableLiveData<Bill>()
    val billList = billRepository.getBill()

    fun addBill(bill: Bill) {
        billLiveData.value = bill
    }
}