package com.keeppieces.android.logic.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BillDao {

    @Insert
    fun insertBill(bill: Bill) : Long

    @Update
    fun updateBill(newBill: Bill)

    @Delete
    fun deleteBill(bill: Bill)

    @Query("SELECT * FROM bill WHERE date = :date ORDER BY bill_id DESC")
    fun getOneDayBill(date: String): LiveData<List<Bill>>

//    @Query("SELECT * FROM bill WHERE date LIKE :year+'-'+:month+'-%' ORDER BY bill_id")
//    fun getOneMonthBill(year:String, month: String): LiveData<List<Bill>>

    @Query("SELECT * FROM bill WHERE date >= :startDate and date <= :endDate")
    fun getPeriodBill(startDate: String, endDate: String): LiveData<List<Bill>>

    @Query("SELECT * FROM bill WHERE date >= :startDate and date <= :endDate and (account = :account or secondary_category = :account)")
    fun getPeriodAccountBill(startDate: String, endDate: String, account: String): LiveData<List<Bill>>

//    @Query("SELECT * FROM bill where DATETIME(date) BETWEEN DATETIME(:startDate) AND DATETIME(:endDate)")
//    fun getPeriodBill(startDate:String,endDate:String):LiveData<List<Bill>>
    @Query("SELECT * FROM bill WHERE date >= :startDate and date <= :endDate and member == :member ")
    fun getPeriodMemberBill(startDate: String, endDate: String, member: String): LiveData<List<Bill>>

    @Query("SELECT * FROM bill ORDER BY amount")
    fun loadAllBillByAmount(): LiveData<List<Bill>>

    @Query("SELECT * FROM bill")
    fun getAllBill(): LiveData<List<Bill>>

    @Query("SELECT * FROM bill WHERE bill_id = :billId ")
    fun getBillById(billId:Long):LiveData<Bill>

    @Query("SELECT * FROM bill WHERE bill_id == :id")
    fun getABill(id: Long): Bill

    @Query("SELECT * FROM bill WHERE primary_category == :primaryCategory AND date >= :startDate AND date <= :endDate")
    fun getPrimaryCategoryBillInTimeSpan(startDate: String, endDate: String, primaryCategory: String): LiveData<List<Bill>>

    @Query("SELECT * FROM bill WHERE secondary_category == :secondaryCategory AND date >= :startDate AND date <= :endDate")
    fun getSecondaryCategoryBillInTimeSpan(startDate: String, endDate: String, secondaryCategory: String): LiveData<List<Bill>>
}