package com.keeppieces.android.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository
import com.keeppieces.android.ui.overview.getParentActivity
import com.keeppieces.line_chart.DataPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment(var startDate: String, var endDate: String, val timeSpan: Int) : Fragment() {
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val fragmentManager = parentFragmentManager
        viewModel.billList(startDate, endDate).observe(viewLifecycleOwner) {billList ->
            val bills = if (billList.isEmpty()) listOf() else billList
            getParentActivity<DetailActivity>().detailLineChart.addDataPoints(getPoints(bills))
            detailBillList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(getItemDecoration())
                adapter = DetailAdapter(bills, fragmentManager)
            }
        }
    }

    private fun getPoints(bills: List<Bill>): MutableList<DataPoint> {
        return getRandomPoints()
    }

    companion object
}