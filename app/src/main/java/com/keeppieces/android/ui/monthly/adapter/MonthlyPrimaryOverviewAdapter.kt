package com.keeppieces.android.ui.monthly.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.inflate
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.DailyPrimary
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData

class MonthlyPrimaryOverviewAdapter(private val primaryList: List<DailyPrimary>) :
    RecyclerView.Adapter<MonthlyPrimaryOverviewAdapter.BillItemViewHolder>() {
    override fun getItemCount(): Int = primaryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyPrimaryOverviewAdapter.BillItemViewHolder {
        return MonthlyPrimaryOverviewAdapter.BillItemViewHolder(parent.inflate(R.layout.item_daily_card_detail))
    }

    override fun onBindViewHolder(holder: MonthlyPrimaryOverviewAdapter.BillItemViewHolder, position: Int) {
        holder.bind(primaryList[position])
    }

    class BillItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val barView: VerticalBar = view.findViewById(R.id.card_detail_bar)
        private val billType: TextView = view.findViewById(R.id.billType)
        private val billAmount: TextView = view.findViewById(R.id.billAmount)
        fun bind(model: DailyPrimary) {
            barView.renderData(VerticalBarData(100f, 100f, model.color))
            billType.text = model.primaryCategory
            billAmount.text = model.amount.toMoneyFormatted()
        }
    }
}