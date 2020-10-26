package com.keeppieces.android.ui.overview.homepage_card_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.BillRepository
import com.keeppieces.android.logic.data.GeneralBill
import com.keeppieces.android.ui.bill.BillActivity
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData

class TodaySummaryCardAdapter(private val content:Context, private val billList: List<GeneralBill>):
    RecyclerView.Adapter<TodaySummaryCardAdapter.ViewHolder>(){

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val billTitle:TextView = view.findViewById(R.id.title_in_item)
        val moneyType:TextView = view.findViewById(R.id.money_type_in_item)  // 收入、支出的区分标志
        val moneySymbol:TextView = view.findViewById(R.id.money_symbol_in_item)
        val moneyAmount:TextView = view.findViewById(R.id.money_amount_in_item)
        val bar:VerticalBar = view.findViewById(R.id.item_vertical_bar)
        val billMoreInformationButton:ImageButton = view.findViewById(R.id.more_information)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(content).inflate(R.layout.item_summary_card,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.billMoreInformationButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            val bill = billList[position]
            BillActivity.start(content,BillRepository().getBillByGeneralBill(bill))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val billItem = billList[position]
        holder.apply {
            billTitle.text = billItem.secondaryCategory
            moneyType.text = if (billItem.type == "支出") "-" else if (billItem.type == "收入") "+" else ""
            moneySymbol.text = "￥"  // 后期扩展
            moneyAmount.text = billItem.amount.toMoneyFormatted()
            bar.renderData(VerticalBarData(100f, 100f, billItem.color))
        }
    }

    override fun getItemCount() = billList.size
}