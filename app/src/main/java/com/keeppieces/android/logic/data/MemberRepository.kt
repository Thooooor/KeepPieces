package com.keeppieces.android.logic.data

import androidx.annotation.ColorRes
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
                    member.amount += bill.amount
                    isMemberExist = true
                    break
                } else {
                    continue
                }
            }
            if (!isMemberExist) {
                val primaryIndex = memberList.size
                val primaryColor = repository.getColorInt(color, primaryIndex)
                memberList.add(DailyMember(bill.member, bill.amount, primaryColor))
            } else {
                continue
            }
        }
        return memberList
    }
}

data class DailyMember(
    val member: String,
    var amount: Double,
    @ColorRes val color: Int
)