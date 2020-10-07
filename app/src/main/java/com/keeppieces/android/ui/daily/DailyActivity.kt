package com.keeppieces.android.ui.daily

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.activity_daily.*


class DailyActivity : AppCompatActivity() {
    lateinit var viewModel: DailyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)
        viewModel = ViewModelProvider(this).get(DailyViewModel::class.java)
        setUpToolbar()
        addDailyFragment(DailyFragment())
    }

    private fun addDailyFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.detailLayout, fragment)
        transaction.commit()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""
    }

}