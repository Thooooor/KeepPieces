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
        val billAmount: TextView = findViewById(R.id.billAmount)
        val billTime: TextView = findViewById(R.id.billTime)
        val billType: TextView = findViewById(R.id.billType)
        val billMember: TextView = findViewById(R.id.billMember)
        val billPrimaryCategory: TextView = findViewById(R.id.billPrimaryCategory)
        val billSecondaryCategory: TextView = findViewById(R.id.billSecondaryCategory)
        val billAccount: TextView = findViewById(R.id.billAccount)

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
