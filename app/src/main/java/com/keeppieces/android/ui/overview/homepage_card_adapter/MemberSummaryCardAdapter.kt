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
import com.keeppieces.android.logic.data.DailyMember
import com.keeppieces.android.ui.member.MemberActivity
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData
import java.time.LocalDate
import java.time.chrono.IsoChronology
import java.util.*

class MemberSummaryCardAdapter(private val content:Context, private val memberSummaryList: List<DailyMember>):
    RecyclerView.Adapter<MemberSummaryCardAdapter.ViewHolder>(){
    
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val memberTitle:TextView = view.findViewById(R.id.title_in_item)
        val moneyType:TextView = view.findViewById(R.id.money_type_in_item)  // 收入、支出的区分标志
        val moneySymbol:TextView = view.findViewById(R.id.money_symbol_in_item)
        val moneyAmount:TextView = view.findViewById(R.id.money_amount_in_item)
        val bar:VerticalBar = view.findViewById(R.id.item_vertical_bar)
        val memberMoreInformationButton: ConstraintLayout = view.findViewById(R.id.more_information)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(content).inflate(R.layout.item_summary_card,parent,false)
        val viewHolder = ViewHolder(view)
        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH) + 1
        val lastDay = when(month) {
            2 -> if (IsoChronology.INSTANCE.isLeapYear(year.toLong())) 29 else 28
            4 -> 30
            6 -> 30
            9 -> 30
            11 -> 30
            else -> 31
        }

        val startDate = LocalDate.of(year, month, 1).toString()
        val endDate = LocalDate.of(year, month, lastDay).toString()

        viewHolder.memberMoreInformationButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            val member = memberSummaryList[position].member
            MemberActivity.start(content, startDate, endDate, member)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memberItem = memberSummaryList[position]
        holder.apply {
            memberTitle.text = memberItem.member
            // moneyType.text = if (memberItem.amount < 0) "-" else if (memberItem.amount > 0) "+" else ""
            moneySymbol.text = "￥"  // 后期扩展
            moneyAmount.text = memberItem.amount.toMoneyFormatted()  // 这里自带符号，所以考虑把前面的符号抛弃
            bar.renderData(VerticalBarData(100f, 100f, memberItem.color))
        }
    }

    override fun getItemCount() = memberSummaryList.size
}