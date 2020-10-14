package com.keeppieces.android.ui.bill

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill
import kotlinx.android.synthetic.main.activity_bill.*
import kotlin.concurrent.thread


class BillActivity : AppCompatActivity() {

    private lateinit var viewModel: BillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]

        billAdd?.setOnClickListener {
            thread {
                val bill = nowData()
                viewModel.addBill(bill)
                finish()
            }
        }

        billBack?.setOnClickListener {
            finish()
        }
    }

    private fun nowData() : Bill {
        val billAmount: TextView = findViewById(R.id.bill_amount)
        val billTime: TextView = findViewById(R.id.bill_time)
        val billType: TextView = findViewById(R.id.bill_type)
        val billMember: TextView = findViewById(R.id.bill_member)
        val billPrimaryCategory: TextView = findViewById(R.id.bill_primaryCategory)
        val billSecondaryCategory: TextView = findViewById(R.id.bill_secondCategory)
        val billAccount: TextView = findViewById(R.id.bill_account)

        return Bill(
            billTime.text.toString(),
            billAmount.text.toString().toDouble(),
            billAccount.text.toString(),
            billMember.text.toString(),
            billPrimaryCategory.text.toString(),
            billSecondaryCategory.text.toString(),
            billType.text.toString()
        )
    }
}
