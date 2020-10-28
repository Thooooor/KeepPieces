package com.keeppieces.android.ui.member

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val myCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            Log.d("Detail", position.toString())
        }

        override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            c.clipRect(0f, viewHolder.itemView.top.toFloat(), dX, viewHolder.itemView.bottom.toFloat())
        }
    }

    private fun setUpRecyclerView() {
        viewModel.getMemberPeriodList(startDate, endDate, member).observe(viewLifecycleOwner) {billList ->
            val bills = if (billList.isEmpty()) listOf() else getBillsFilter(billList)

            bill_flow_recycler_view.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(getItemDecoration())
                adapter = DetailAdapter(bills)
                val myHelper = ItemTouchHelper(myCallback)
                myHelper.attachToRecyclerView(this)
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
