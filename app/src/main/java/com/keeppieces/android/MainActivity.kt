package com.keeppieces.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.SavedStateHandle
import com.keeppieces.android.logic.data.AppDatabase
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.daily.DailyActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val context = KeepPiecesApplication.context
    private val billListLiveData = MutableLiveData<List<Bill>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val billDao = AppDatabase.getDatabase(this).billDao()
        val dateString = LocalDate.now().toString()
        addBillButton.setOnClickListener {
            thread {
                val bill1 = Bill(dateString, 23.2)
                bill1.id = billDao.insertBill(bill1)
                Log.d("MainActivity${bill1.id}", bill1.toString())
            }
        }

        queryButton.setOnClickListener {
            /*
            val billList: LiveData<List<Bill>> = Transformations.switchMap(billListLiveData) {
                billDao.loadAllBills()
            }
            if (billList.value == null) {
                Log.d("MainActivity", "Null")
            } else {
                for (bill in billList.value!!) {
                    Log.d("MainActivity", bill.toString())
                }
            }*/
            billDao.loadAllBills().observe(this, {bills ->
                for (bill in bills) {
                    Log.d("Main ${bill.id}", bill.toString())
                }
            })
            thread {
                val billList = billDao.loadAllBills().value
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