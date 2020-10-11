package com.keeppieces.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.keeppieces.android.logic.data.AppDatabase
import com.keeppieces.android.ui.bill.BillActivity
import com.keeppieces.android.ui.daily.DailyActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

@Suppress("COMPATIBILITY_WARNING")
class MainActivity : AppCompatActivity() {
    val context = KeepPiecesApplication.context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val billDao = AppDatabase.getDatabase(this).billDao()
        addBillButton.setOnClickListener {
            val intent = Intent(this, BillActivity::class.java)
            startActivity(intent)
        }

        queryButton.setOnClickListener {
            billDao.loadAllBillByAmount().observe(this, {bills ->
                for (bill in bills) {
                    Log.d("Main ${bill.billId}", bill.toString())
                }
            })
            thread {
                val billList = billDao.loadAllBillByAmount().value
                if (billList == null) {
                    Log.d("MainActivity", "Null")
                } else {
                    for (bill in billList) {
                        Log.d("MainActivity", bill.toString())
                    }
                }
            }
        }

        dailyButton.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }
    }

}