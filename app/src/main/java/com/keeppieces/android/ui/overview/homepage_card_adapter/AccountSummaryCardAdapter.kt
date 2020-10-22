package com.keeppieces.android.ui.overview.homepage_card_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.DailyAccount
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData

class AccountSummaryCardAdapter(private val content:Context, private val accountList: List<DailyAccount>):
    RecyclerView.Adapter<AccountSummaryCardAdapter.ViewHolder>(){
    
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val accountTitle:TextView = view.findViewById(R.id.title_in_item)
        val moneyType:TextView = view.findViewById(R.id.money_type_in_item)  // 收入、支出的区分标志
        val moneySymbol:TextView = view.findViewById(R.id.money_symbol_in_item)
        val moneyAmount:TextView = view.findViewById(R.id.money_amount_in_item)
        val bar:VerticalBar = view.findViewById(R.id.item_vertical_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(content).inflate(R.layout.item_summary_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val accountItem = accountList[position]
        holder.apply {
            accountTitle.text = accountItem.account
            // moneyType.text = if (accountItem.amount < 0) "-" else if (accountItem.amount > 0) "+" else ""
            moneySymbol.text = "￥"  // 后期扩展
            moneyAmount.text = accountItem.amount.toMoneyFormatted()  // 这里自带符号，所以考虑把前面的符号抛弃
            bar.renderData(VerticalBarData(100f, 100f, accountItem.color))
        }
    }

    override fun getItemCount() = accountList.size
}