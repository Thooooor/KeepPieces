package com.keeppieces.android.ui.bill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.activity_bill.*

class BillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        addButton.setOnClickListener {
        }
    }
}