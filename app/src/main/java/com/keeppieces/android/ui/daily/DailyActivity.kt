package com.keeppieces.android.ui.daily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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