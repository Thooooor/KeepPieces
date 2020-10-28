package com.keeppieces.android.ui.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.keeppieces.android.R
import com.keeppieces.android.logic.Repository
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.account.adapter.AccountPagerAdapter
import com.keeppieces.android.ui.monthly.CustomMode
import com.keeppieces.android.ui.monthly.MonthMode
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.include_detail_datebar.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.chrono.IsoChronology
import kotlin.math.abs

class AccountActivity() : AppCompatActivity() {

    private val viewModel: AccountViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O) var startLocalDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O) var endLocalDate: LocalDate = LocalDate.now()
    private var timeSpan: Int = 1
    private var cnt: Int = -1
    private var mode = MonthMode
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var account: String
    private var billsOutFilter = mutableListOf<Bill>()
    private var billsInFilter = mutableListOf<Bill>()
    private var outAmount : Double = 0.00
    private var inAmount : Double = 0.00

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        setSupportActionBar(accountDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        startDate = intent.getStringExtra("startDate").toString()
        endDate = intent.getStringExtra("endDate").toString()
        account = intent.getStringExtra("account").toString()

        initDate()
        if (savedInstanceState == null) runEnterAnimation()
        setUpView()

        detailLeftArrow.setOnClickListener {
            updateDate(-timeSpan)
            setUpView()
        }

        detailRightArrow.setOnClickListener {
            updateDate(timeSpan)
            setUpView()
        }

        detailDateText.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()

            picker.show(supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val format = SimpleDateFormat("yyyy-MM-dd")
                timeSpan = ((it.second!! - it.first!!) / (1000 * 3600 * 24)).toInt() + 1
                timeSpan = if (timeSpan == 0) 1 else timeSpan
                startDate = format.format(it.first)
                startLocalDate = LocalDate.parse(startDate)
                endDate = format.format(it.second)
                endLocalDate = LocalDate.parse(endDate)
                updateMode()
                setUpView()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun setUpView() {
        account_title.text = account
        detailDateText.text = StringBuilder("$startDate ~ $endDate").toString()
        setUpAmount()
    }

    private fun setUpViewPager() {
        view_pager.adapter = AccountPagerAdapter(supportFragmentManager, startDate, endDate, account)
        view_pager.offscreenPageLimit = 0
        tab_layout.setUpWithViewPager(view_pager, true)
        view_pager.setCurrentItem(0, true)
    }

    private fun setUpAmount() {
        viewModel.getAccountPeriodList(startDate, endDate, account).observe(this) {billList ->
//            val bills = if (billList.isEmpty()) listOf() else
            inAmount = 0.00
            outAmount = 0.00
            billsInFilter.clear()
            billsOutFilter.clear()

            getBillsFilter(billList)
            for (item in billsInFilter) {
                inAmount += item.amount
            }
            for (item in billsOutFilter) {
                outAmount += item.amount
            }
            accountIncome.text = ("收入： $inAmount").toString()
            accountExpenditure.text = ("支出： $outAmount").toString()
            setUpViewPager()
            setUpPieView()
        }
    }

    private fun setUpPieView() {
        val accountPieList = listOf<accountPie>(accountPie("支出", outAmount, Repository.getColorInt("yellow", 0)),
                                                accountPie("收入", inAmount, Repository.getColorInt("blue", 1)))
        val piePortions = accountPieList.map {
            PiePortion(
                    it.name, abs(it.amount), ContextCompat.getColor(this, it.color)
            )// 这里的amount正负由颜色确定
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(account_pie_chart).apply {
            duration = 600
        }
        account_pie_chart.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun getBillsFilter(billList: List<Bill>){
        for (item in billList) {
            if (item.secondaryCategory == account || item.type == "收入") {
                billsInFilter.plusAssign(listOf(item))
            }
            if (item.type == "支出") {
                billsOutFilter.plusAssign(listOf(item))
            }
        }
    }

    private fun runEnterAnimation() {
        tab_layout.post {
            tab_layout.translationY -= tab_layout.height.toFloat()
            tab_layout.alpha = 0f
            tab_layout.animate()
                    .translationY(0f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .alpha(1f)
                    .setDuration(300)
                    .start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDate() {
        startLocalDate = LocalDate.parse(startDate)
        endLocalDate = LocalDate.parse(endDate)
        timeSpan = endLocalDate.dayOfYear - startLocalDate.dayOfYear + 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDate(span: Int) {
        if (mode == MonthMode) {
            val monthSpan = if (span > 0) 1 else -1
            startLocalDate = startLocalDate.plusMonths(monthSpan.toLong())
            endLocalDate = startLocalDate.plusMonths(1).plusDays(-1)
        } else {
            startLocalDate = startLocalDate.plusDays(span.toLong())
            endLocalDate = endLocalDate.plusDays(span.toLong())
        }

        startDate = startLocalDate.toString()
        endDate = endLocalDate.toString()
        Log.d("Monthly Date Update", "$startDate ~ $endDate")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMode() {
        val year = endLocalDate.year
        val month = endLocalDate.monthValue
        val lastDay = endLocalDate.dayOfMonth
        mode = if (startLocalDate.dayOfMonth == 1 && startLocalDate.monthValue == month && lastDay >= 28) {
            when (lastDay) {
                31 -> MonthMode
                30 -> when (month) {
                    4 -> MonthMode
                    6 -> MonthMode
                    9 -> MonthMode
                    11 -> MonthMode
                    else -> CustomMode
                }
                29 -> if (month == 2) MonthMode else CustomMode
                28 -> if (month == 2 && IsoChronology.INSTANCE.isLeapYear(year.toLong())) MonthMode else CustomMode
                else -> CustomMode
            }
        } else {
            CustomMode
        }
    }

    companion object {
        fun start(context: Context, startDate: String, endDate: String, account: String) {
            val intent = Intent(context, AccountActivity::class.java)
                intent.apply {
                    putExtra("startDate", startDate)
                    putExtra("endDate", endDate)
                    putExtra("account", account)
                }
            val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity)
            context.window.exitTransition = context.window.exitTransition
            context.startActivity(intent, options.toBundle())
        }
    }
}

data class accountPie(val name: String, val amount: Double, val color: Int)