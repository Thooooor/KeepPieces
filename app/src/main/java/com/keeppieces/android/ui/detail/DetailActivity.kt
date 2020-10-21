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
import com.keeppieces.android.R
import com.keeppieces.line_chart.DataPoint
import kotlinx.android.synthetic.main.activity_detail.*
import java.time.LocalDate

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailLineChart.setCurveBorderColor(colorInt)
        Log.d("Detail", KEY_COLOR)
        setUpToolbar()
        setUpTab()
        detailLineChart.addDataPoints(getRandomPoints())
    }

    private fun setUpToolbar() {
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

    private fun setUpTab() {
        tab.addTabs(
            listOf(
                "Jan 2018", "Feb 2018", "Mar 2018", "Apr 2018", "May 2018", "Jun 2018", "July 2018",
                "Aug 2018", "Sep 2018", "Oct 2018", "Nov 2018", "Dec 2018"
            )
        )
        tab.addOnPageChangeListener {
            detailLineChart.addDataPoints(getRandomPoints())
        }

        detailViewPager.adapter = DailyDetailPagerAdapter(supportFragmentManager)
        tab.setUpWithViewPager(detailViewPager)
    }

    companion object {
        private const val KEY_COLOR = "key-color"
        private const val KEY_DATE = "key-date"
        private var colorInt = R.color.dark_green
        @RequiresApi(Build.VERSION_CODES.O) private var startDate = LocalDate.now()
        @RequiresApi(Build.VERSION_CODES.O) private var endDate = LocalDate.now()
        fun start(
            context: Context,
            date1: LocalDate,
            date2: LocalDate,
            @ColorRes color: Int
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_COLOR, color)
            colorInt = color
            startDate = date1
            endDate = date2

            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity)

            val transition = context.window.exitTransition
            // transition.excludeTarget(shareView, true)
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