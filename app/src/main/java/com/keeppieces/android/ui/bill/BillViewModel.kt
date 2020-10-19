package com.keeppieces.android.ui.bill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*
import kotlin.concurrent.thread

class BillViewModel() : ViewModel() {
    private val billLiveData = MutableLiveData<Bill>()

    var bill = Bill(
        System.currentTimeMillis().toString(),
        0.00,
        "现金",
        "自己",
        "三餐",
        "早餐",
        "支出")

    fun addBill(bill: Bill) {
        thread {
            AccountRepository().createAccount(Account(bill.account,bill.amount))
            MemberRepository().createMember(Member(bill.member))
            PrimaryCategoryRepository().createPrimaryCategory(PrimaryCategory(bill.primaryCategory))
            SecondaryCategoryRepository().createSecondaryCategory(SecondaryCategory(bill.secondaryCategory, bill.primaryCategory))
            TypeRepository().createType(Type(bill.type))
            BillRepository().insertBill(bill)
        }
    }

    fun updateBill(bill: Bill) {
        thread {
            BillRepository().updateBill(bill)
        }
    }

    fun deleteBill(bill: Bill) {
        thread {
            BillRepository().deleteBill(bill)
        }
    }
}