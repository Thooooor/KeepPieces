package com.keeppieces.android.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.keeppieces.android.R
import com.keeppieces.android.ui.monthly.CustomMode
import com.keeppieces.android.ui.monthly.DayMode
import com.keeppieces.android.ui.monthly.MonthMode
import com.keeppieces.line_chart.DataPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.include_detail_datebar.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.chrono.IsoChronology

const val DailyDetail = 0
const val MonthlyDetail = 1
const val TypeDetail = 2
const val CategoryDetail = 3
const val AccountDetail = 4
const val MemberDetail = 5


class DetailActivity : AppCompatActivity() {
    private lateinit var startDate: String
    private lateinit var endDate: String
    private var mode = MonthMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initDate()
        detailLineChart.setCurveBorderColor(colorInt)
        setupToolbar()
        setupFragment()

        detailLeftArrow.setOnClickListener {
            updateDate(-timeSpan)
            setupFragment()
        }
        detailRightArrow.setOnClickListener {
            updateDate(timeSpan)
            setupFragment()
        }
        detailDateText.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()
            picker.show(supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val format = SimpleDateFormat("yyyy-MM-dd")
                timeSpan = ((it.second!! - it.first!!) / (1000 * 3600 * 24)).toInt()
                timeSpan = if (timeSpan == 0) 1 else timeSpan
                startDate = format.format(it.first)
                startLocalDate = LocalDate.parse(startDate)
                endDate = format.format(it.second)
                endLocalDate = LocalDate.parse(endDate)
                updateMode()
                setupFragment()
            }
        }
    }

    private fun initDate() {
        startDate = startLocalDate.toString()
        endDate = endLocalDate.toString()
        mode = when (timeSpan) {
            1 -> DayMode
            else -> MonthMode
        }
    }

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
        Log.d("Detail Date Update", "$startDate ~ $endDate")
    }

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
        } else if (timeSpan == 1) {
            DayMode
        } else {
            CustomMode
        }
    }

    private fun setupFragment() {
        val fragment = DetailFragment(startDate, endDate, detailType)
        replaceFragment(fragment)
        setupDatebar()
        detailLineChart.addDataPoints(getRandomPoints())
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transition = fragmentManager.beginTransaction()
        transition.replace(R.id.detailFragment, fragment)
        transition.commit()
    }

    private fun setupDatebar() {
        detailDateText.text = if (timeSpan == 1) startDate else "$startDate ~ $endDate"
    }

    private fun setupToolbar() {
        setSupportActionBar(detailToolbar as Toolbar?)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_COLOR = "key-color"
        private var colorInt = R.color.dark_green
        @RequiresApi(Build.VERSION_CODES.O) private var startLocalDate = LocalDate.now()
        @RequiresApi(Build.VERSION_CODES.O) private var endLocalDate = LocalDate.now()
        private var detailType: Int = MonthlyDetail
        private var timeSpan: Int = 1

        fun start(
            context: Context,
            date1: String,
            date2: String,
            type: Int,
            @ColorRes color: Int,
            span: Int?=null
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_COLOR, color)
            colorInt = color
            startLocalDate = LocalDate.parse(date1)
            endLocalDate = LocalDate.parse(date2)
            detailType = type
            if (span != null) timeSpan = span
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity)

            val transition = context.window.exitTransition
            context.window.exitTransition = transition
            context.startActivity(intent, options.toBundle())
        }
    }
}

fun getRandomPoints(): MutableList<DataPoint> {
    val list = mutableListOf<DataPoint>()
    val range = (0..10)

    (1..15).forEach { _ ->
        list.add(DataPoint(range.random()*100f))
    }
    return list
}