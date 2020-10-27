package com.keeppieces.android.logic.data

import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.keeppieces.android.KeepPiecesApplication
import com.keeppieces.android.logic.Repository
import kotlin.math.abs


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
                val memberIndex = memberList.size
                val memberColor = repository.getColorInt(color, memberIndex)
                if (bill.type == "收入") {
                    memberList.add(DailyMember(bill.member, bill.amount, memberColor))
                } else {
                    memberList.add(DailyMember(bill.member, -bill.amount, memberColor))
                }

            } else {
                continue
            }
        }
        return memberList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMemberMonthSummary(monthBillList: List<Bill>, color: String): MutableList<DailyMember> {
        val memberMonthCondition = mutableMapOf<String, Pair<Double, Int>>()
        for (bill in monthBillList) {
            val amount = if (bill.type == "收入") bill.amount else -bill.amount
            val colorInt = repository.getColorInt(color, memberMonthCondition.size)
            val memberData = memberMonthCondition.getOrDefault(bill.member, Pair(0.00, colorInt))
            memberMonthCondition[bill.member] = Pair(memberData.first + amount, memberData.second)
        }
        val memberMonthSummary = mutableListOf<DailyMember>()
        memberMonthCondition.forEach { (member, condition) ->
            memberMonthSummary.add(DailyMember(member, condition.first, condition.second))
        }
        memberMonthSummary.sortByDescending { abs(it.amount) }
        return memberMonthSummary
    }

    fun getMemberClassification(bills: List<Bill>): MutableMap<String, Pair<List<Bill>, Int>> {
        val memberClassification = mutableMapOf<String, Pair<List<Bill>, Int>>()
        var count = 0
        for (bill in bills) {
            if (bill.member !in memberClassification) {
                memberClassification[bill.member] = Pair(mutableListOf(bill), count++)
            } else if (bill.type != "转账") {
                memberClassification[bill.member] = Pair(
                    memberClassification[bill.member]!!.first.plus(bill),
                    memberClassification[bill.member]!!.second
                )
            } else {
                memberClassification[bill.member] = Pair(
                    memberClassification[bill.member]!!.first.plus(bill),
                    memberClassification[bill.member]!!.second
                )
            }
        }
        return memberClassification
    }

    fun getMemberSummary(
        bills: List<Bill>,
        positiveColor: String,
        negativeColor: String
    ): MutableList<MemberDetail> {
        val memberList = mutableListOf<MemberDetail>()
        val memberClassification = getMemberClassification(bills)
        for (memberBill in memberClassification) {
            var income = 0.00
            var expenditure = 0.00
            val outCategoryAmount = HashMap<String, Double>()
            val inCategoryAmount = HashMap<String, Double>()
            val outAccountAmount = HashMap<String, Double>()
            val inAccountAmount = HashMap<String, Double>()
            for (bill in memberBill.value.first) {
                if (bill.type == "收入" /*|| (bill.type == "转账" && bill.secondaryCategory == memberBill.key)*/) {
                    income += bill.amount
                    if (bill.primaryCategory !in inCategoryAmount) {
                        inCategoryAmount[bill.primaryCategory] = bill.amount
                    } else {
                        inCategoryAmount[bill.primaryCategory] =
                            inCategoryAmount[bill.primaryCategory]!! + bill.amount
                    }
                    if (bill.account !in inAccountAmount) {
                        inAccountAmount[bill.account] = bill.amount
                    } else {
                        inAccountAmount[bill.account] =
                            inAccountAmount[bill.account]!! + bill.amount
                    }
                } else if (bill.type == "支出" /*|| (bill.type == "转账" && bill.member == memberBill.key)*/) {
                    expenditure += bill.amount
                    if (bill.primaryCategory !in outCategoryAmount) {
                        outCategoryAmount[bill.primaryCategory] = bill.amount
                    } else {
                        outCategoryAmount[bill.primaryCategory] =
                            outCategoryAmount[bill.primaryCategory]!! + bill.amount
                    }
                    if (bill.account !in outAccountAmount) {
                        outAccountAmount[bill.account] = bill.amount
                    } else {
                        outAccountAmount[bill.account] =
                            outAccountAmount[bill.account]!! + bill.amount
                    }
                }
            }
//            val account = getAAccount(accountBill.key)
            val accountColor = repository.getColorInt(
                if (income - expenditure >= 0) {
                    positiveColor
                } else {
                    negativeColor
                }, memberBill.value.second
            )
//            val inMaxCategory = inCategoryAmount.maxOf { tmp -> tmp.key}
//            val inMaxCategory = inCategoryAmount.
            val inMaxCategory = getMapMax(inCategoryAmount)
            val outMaxCategory = getMapMax(outCategoryAmount)
            val inMaxMember = getMapMax(inAccountAmount)
            val outMaxMember = getMapMax(outAccountAmount)
            memberList.add(
                MemberDetail(
                    memberBill.key,
                    income,
                    expenditure,
                    income - expenditure,
//                accountBill.,
                    accountColor,
                    outMaxCategory,
                    inMaxCategory,
                    outMaxMember,
                    inMaxMember
                )
            )
        }
        return memberList
    }

    private fun getMapMax(map: HashMap<String, Double>): String {
        var maxValue = 0.00
        var maxText = ""
        for (item in map) {
            if (item.value > maxValue) {
                maxValue = item.value
                maxText = item.key
            }
        }
        return maxText
    }


}

data class DailyMember(
    val member: String,
    var amount: Double,
    @ColorRes val color: Int
)

data class MemberDetail(
    val member: String,
    //var amount: Double,
    var income: Double = 0.00,
    var expenditure: Double = 0.00,
    var lastAmount: Double,
//    var finalAmount : Double,
    @ColorRes val color: Int,
    var outMaxCategory: String,
    var inMaxCategory: String,
    var outMaxAccount: String,
    var inMaxAccount: String
)


fun getDailyMemberMember(dailyMember: DailyMember): String = dailyMember.member
fun getDailyMemberAmount(dailyMember: DailyMember): Double = dailyMember.amount
fun getDailyMemberColorInt(dailyMember: DailyMember): Int = dailyMember.color
