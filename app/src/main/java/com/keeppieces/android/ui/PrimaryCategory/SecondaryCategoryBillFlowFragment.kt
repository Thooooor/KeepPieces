package com.keeppieces.android.ui.PrimaryCategory

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
import com.keeppieces.android.ui.PrimaryCategory.adapter.SecondaryCategoryBillFlowFragmentAdapter

class SecondaryCategoryBillFlowFragment(val billList:List<Bill>, var cnt:Int = -1):Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill_flow_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView:RecyclerView = view.findViewById(R.id.bill_flow_recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            if(cnt<0) addItemDecoration(getItemDecoration())
            cnt++
            adapter = SecondaryCategoryBillFlowFragmentAdapter(billList)
        }
    }
}