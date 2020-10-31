package com.keeppieces.android.ui.primaryCategory.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.logic.data.Bill

class CategoryBillFlowFragment(
    val billList: List<Bill>,
    val startDate: String,
    val endDate: String,
    private val level: Int,
    var cnt: Int = -1
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill_flow_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.bill_flow_recycler_view)
        val fragmentManager = parentFragmentManager
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if (cnt < 0) addItemDecoration(getItemDecoration())
            cnt++
            adapter = CategoryBillFlowFragmentAdapter(context, billList, startDate, endDate, level, fragmentManager)
        }
    }
}