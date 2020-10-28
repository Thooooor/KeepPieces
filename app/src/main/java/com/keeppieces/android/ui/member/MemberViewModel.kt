package com.keeppieces.android.ui.member

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.MemberRepository
import com.keeppieces.android.logic.data.*

class MemberViewModel @ViewModelInject internal constructor(): ViewModel() {
    private val memberRepository = MemberRepository()
    fun billList(startDate: String, endDate: String) = BillRepository().getPeriodBill(startDate, endDate)



    fun getMemberSummary(bills: List<Bill>, positiveColor: String, negativeColor: String) : MutableList<MemberDetail> {
        return memberRepository.getMemberSummary(bills, positiveColor, negativeColor)
    }

    // 得到每一个成员的概要：收支总和、颜色（大圆环）、花费最多的一级类，并把这个概要信息存放到ViewModel中，并返回一个一级类总和

}