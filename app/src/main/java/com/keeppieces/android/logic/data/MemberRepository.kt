package com.keeppieces.android.logic.data

import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.logic.Repository


class MemberRepository {
    private val context = KeepPiecesApplication.context
    private val memberDao = AppDatabase.getDatabase(context).memberDao()
    private val repository = Repository

    fun createMember(member: Member) {
        memberDao.insertMember(member)
    }

    fun getMember() = memberDao.getMember()

    fun updateMember(member: Member) {
        memberDao.updateMember(member)
    }

    fun deleteMember(member: Member) {
        memberDao.deleteMember(member)
    }

    fun getDailyMemberList(bills: List<Bill>, color: String): MutableList<DailyMember> {
        val memberList = mutableListOf<DailyMember>()
        for (bill in bills) {
            var isMemberExist = false
            for (member in memberList) {
                if (member.member == bill.member) {
                    if (bill.type == "收入") {
                        member.amount += bill.amount
                    } else {
                        member.amount -= bill.amount
                    }
                    isMemberExist = true
                    break
                } else {
                    continue
                }
            }
            if (!isMemberExist) {
                val primaryIndex = memberList.size
                val primaryColor = repository.getColorInt(color, primaryIndex)
                if (bill.type == "收入"){
                    memberList.add(DailyMember(bill.member, bill.amount, primaryColor))
                } else {
                    memberList.add(DailyMember(bill.member, -bill.amount, primaryColor))
                }

            } else {
                continue
            }
        }
        return memberList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMemberMonthSummary(monthBillList:List<Bill>,color: String) : MutableList<DailyMember> {
        val memberMonthCondition = mutableMapOf<String,Pair<Double,Int>>()
        for(bill in monthBillList) {
            val amount = if (bill.type == "收入") bill.amount else -bill.amount
            val colorInt = repository.getColorInt(color, memberMonthCondition.size)
            val memberData = memberMonthCondition.getOrDefault(bill.member,Pair(0.00,colorInt))
            memberMonthCondition[bill.member] = Pair(memberData.first+amount,memberData.second)
        }
        val memberMonthSummary = mutableListOf<DailyMember>()
        memberMonthCondition.forEach { member, condition ->
            memberMonthSummary.add(DailyMember(member,condition.first,condition.second)) }
        return memberMonthSummary
    }
}

data class DailyMember(
    val member: String,
    var amount: Double,
    @ColorRes val color: Int
)

fun getDailyMemberMember(dailyMember: DailyMember) : String = dailyMember.member
fun getDailyMemberAmount(dailyMember: DailyMember) : Double = dailyMember.amount
fun getDailyMemberColorInt(dailyMember: DailyMember) : Int = dailyMember.color
