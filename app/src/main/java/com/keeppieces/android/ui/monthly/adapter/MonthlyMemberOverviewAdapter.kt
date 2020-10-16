package com.keeppieces.android.ui.monthly.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.inflate
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.DailyMember
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData

class MonthlyMemberOverviewAdapter(private val memberList: List<DailyMember>) :
    RecyclerView.Adapter<MonthlyMemberOverviewAdapter.BillItemViewHolder>() {
    override fun getItemCount(): Int = memberList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyMemberOverviewAdapter.BillItemViewHolder {
        return MonthlyMemberOverviewAdapter.BillItemViewHolder(parent.inflate(R.layout.item_daily_card_detail))
    }

    override fun onBindViewHolder(holder: MonthlyMemberOverviewAdapter.BillItemViewHolder, position: Int) {
        holder.bind(memberList[position])
    }

    class BillItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val barView: VerticalBar = view.findViewById(R.id.card_detail_bar)
        private val billType: TextView = view.findViewById(R.id.billType)
        private val billAmount: TextView = view.findViewById(R.id.billAmount)
        fun bind(model: DailyMember) {
            barView.renderData(VerticalBarData(100f, 100f, model.color))
            billType.text = model.member
            billAmount.text = model.amount.toMoneyFormatted()
        }
    }
}