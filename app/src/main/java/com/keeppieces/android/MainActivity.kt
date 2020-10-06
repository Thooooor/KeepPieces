package com.keeppieces.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keeppieces.android.ui.bill.BillActivity
import com.keeppieces.android.ui.daily.DailyActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addBillButton.setOnClickListener {
            val intent = Intent(this, BillActivity::class.java)
            startActivity(intent)
        }
    }
}