package com.keeppieces.android.ui.overview.homepage_card_adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.DailyAccount
import com.keeppieces.android.ui.account.AccountActivity
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData
import java.time.LocalDate
import java.time.chrono.IsoChronology
import java.util.*

class AccountSummaryCardAdapter(private val content:Context, private val accountSummaryList: List<DailyAccount>):
    RecyclerView.Adapter<AccountSummaryCardAdapter.ViewHolder>(){
    @RequiresApi(Build.VERSION_CODES.O)
    val month = LocalDate.now().monthValue
    @RequiresApi(Build.VERSION_CODES.O)
    val year = LocalDate.now().year
    @RequiresApi(Build.VERSION_CODES.O)
    val startDate = LocalDate.parse("${year}-${month}-01")
    @RequiresApi(Build.VERSION_CODES.O)
    val endDate = startDate.plusMonths(1).minusDays(1)
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val accountTitle:TextView = view.findViewById(R.id.title_in_item)
        val moneyType:TextView = view.findViewById(R.id.money_type_in_item)  // 收入、支出的区分标志
        val moneySymbol:TextView = view.findViewById(R.id.money_symbol_in_item)
        val moneyAmount:TextView = view.findViewById(R.id.money_amount_in_item)
        val bar:VerticalBar = view.findViewById(R.id.item_vertical_bar)
        val accountMoreInformationButton: ConstraintLayout = view.findViewById(R.id.more_information)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(content).inflate(R.layout.item_summary_card,parent,false)
        val viewHolder = ViewHolder(view)

        view.setOnClickListener {
            val position = viewHolder.adapterPosition
            val account = accountSummaryList[position].account
            AccountActivity.start(content, startDate.toString(), endDate.toString(), account)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val accountItem = accountSummaryList[position]
        holder.apply {
            accountTitle.text = accountItem.account
            // moneyType.text = if (accountItem.amount < 0) "-" else if (accountItem.amount > 0) "+" else ""
            moneySymbol.text = "￥"  // 后期扩展
            moneyAmount.text = accountItem.amount.toMoneyFormatted()  // 这里自带符号，所以考虑把前面的符号抛弃
            bar.renderData(VerticalBarData(100f, 100f, accountItem.color))
            accountMoreInformationButton.setOnClickListener {
                val account = accountSummaryList[position].account
                AccountActivity.start(content, startDate.toString(), endDate.toString(), account)
            }
        }
    }

    override fun getItemCount() = accountSummaryList.size
}