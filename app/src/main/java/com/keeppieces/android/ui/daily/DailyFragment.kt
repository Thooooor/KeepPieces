package com.keeppieces.android.ui.daily

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.logic.Repository
import kotlinx.android.synthetic.main.fragment_daily.*
import java.time.LocalDate

class DailyFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        Log.d(TAG, "onActivityCreated")
    }

    private fun setUpRecyclerView() {
        billDaily.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyAdapter(Repository.getDailyItems(LocalDate.now()))
        }
    }

    companion object {
        const val TAG = "DailyFragment"
        private const val KEY_DAY = "key-day"
        fun newInstance(day: Int): DailyFragment {
            return DailyFragment().apply {
                arguments = Bundle().apply { putInt(KEY_DAY, day) }
            }
        }
    }
}