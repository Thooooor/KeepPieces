package com.keeppieces.android.ui.bill

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill

private const val BillSee = 1
private const val BillAdd = 0
class BillActivity : AppCompatActivity() {

    private var mode: Int = 0
    private lateinit var bill: Bill
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        mode =  intent.getIntExtra("billMode", 0)
        bill = Bill(intent.getStringExtra("billTime").toString(),
            intent.getDoubleExtra("billAmount", 0.00),
            intent.getStringExtra("billAccount").toString(),
            intent.getStringExtra("billMember").toString(),
            intent.getStringExtra("billPrimary").toString(),
            intent.getStringExtra("billSecondary").toString(),
            intent.getStringExtra("billType").toString())
        when (mode) {
            BillAdd -> {
                billAddFragment()
            }
            BillSee -> {
                Log.d("BillActivity","make mode")
                billSeeFragment(bill)
            }
            else -> {
                billAddFragmentUpdate(bill)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data != null) {
                val resultBill = Bill(
                    data.getStringExtra("billTime").toString(),
                    data.getDoubleExtra("billAmount", 0.00),
                    data.getStringExtra("billAccount").toString(),
                    data.getStringExtra("billMember").toString(),
                    data.getStringExtra("billPrimary").toString(),
                    data.getStringExtra("billSecondary").toString(),
                    data.getStringExtra("billType").toString())
                billSeeFragment(resultBill)
            } else {
                finish()
            }
        }
    }

    private fun billAddFragment() {
        val fragment = BillAddFragment(null)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(R.id.bill_main, fragment)
            commit()
        }
    }

    private fun billAddFragmentUpdate(bill: Bill) {
        Log.d("BillActivity","billAddUpdate")
        val fragment = BillAddFragment(bill)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(R.id.bill_main, fragment)
            commit()
        }
    }

    private fun billSeeFragment(bill: Bill) {
        Log.d("BillActivity","billSeeUpdate")
        val fragment = BillSeeFragment(bill)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(R.id.bill_main, fragment)
            commit()
        }
    }

    companion object {
        fun start(context: Context, bill: Bill?, inMode: Int=1) {
            val intent = Intent(context, BillActivity::class.java)
            intent.putExtra("billMode", inMode)
            if (bill != null) {
                intent.apply {
                    putExtra("billTime", bill.date)
                    putExtra("billAmount", bill.amount)
                    putExtra("billAccount", bill.account)
                    putExtra("billMember", bill.member)
                    putExtra("billPrimary", bill.primaryCategory)
                    putExtra("billSecondary", bill.secondaryCategory)
                    putExtra("billType", bill.type)
                }
            }
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity)
            context.window.exitTransition = context.window.exitTransition
            if (inMode == 2) {
                context.startActivityForResult(intent,1, options.toBundle())
            } else {
                context.startActivity(intent, options.toBundle())
            }
        }
    }
}
