package com.keeppieces.android.ui.daily.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.inflate
import com.keeppieces.android.logic.data.DailyPrimary
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData

class DailyPrimaryOverviewAdapter(private val primaryList: List<DailyPrimary>) :
    RecyclerView.Adapter<DailyPrimaryOverviewAdapter.BillItemViewHolder>() {
    override fun getItemCount(): Int = primaryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyPrimaryOverviewAdapter.BillItemViewHolder {
        return DailyPrimaryOverviewAdapter.BillItemViewHolder(parent.inflate(R.layout.item_daily_card_detail))
    }

    override fun onBindViewHolder(holder: DailyPrimaryOverviewAdapter.BillItemViewHolder, position: Int) {
        holder.bind(primaryList[position])
    }

    class BillItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val barView: VerticalBar = view.findViewById(R.id.card_detail_bar)
        private val billType: TextView = view.findViewById(R.id.billType)
        private val billAmount: TextView = view.findViewById(R.id.billAmount)
        fun bind(model: DailyPrimary) {
            barView.renderData(VerticalBarData(100f, 100f, model.color))
            billType.text = model.primaryCategory
            billAmount.text = model.amount.toString()
        }
    }
}