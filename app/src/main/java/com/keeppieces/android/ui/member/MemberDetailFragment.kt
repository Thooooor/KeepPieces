package com.keeppieces.android.ui.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.detail.DetailAdapter
import kotlinx.android.synthetic.main.fragment_member_flow_view.*


class MemberDetailFragment(var startDate: String, var endDate: String, var member: String, val type: String) : Fragment() {
    private val viewModel: MemberViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_flow_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        viewModel.getMemberPeriodList(startDate, endDate, member).observe(viewLifecycleOwner) {billList ->
            val bills = if (billList.isEmpty()) listOf() else getBillsFilter(billList)

            bill_flow_recycler_view.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(getItemDecoration())
                adapter = DetailAdapter(bills)
            }
        }
    }

    private fun getBillsFilter(billList: List<Bill>) : List<Bill> {
        val billsFilter = mutableListOf<Bill>()
        if (type == "收入") {
            for (item in billList) {
                if ( item.type == type) {
                    billsFilter += listOf(item)
                }
            }
        } else {
            for (item in billList) {
                if (item.type == type) {
                    billsFilter += listOf(item)
                }
            }
        }
        return billsFilter
    }

    companion object
}
