package com.keeppieces.android.ui.member.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.DailyMember
import com.keeppieces.android.logic.data.MemberDetail
import com.keeppieces.android.ui.member.MemberActivity
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData
import kotlin.math.abs

class MemberOverviewAdapter(
    private val memberSummary: List<MemberDetail>,
    private val memberIncome: Double,
    private val memberExpenditure: Double,
    private val startDate: String,
    private val endDate: String
) :
    RecyclerView.Adapter<MemberOverviewAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.carTitle)
        val cardVerticalBar: VerticalBar = view.findViewById(R.id.cardVerticalBar)
        val overviewInfo: RecyclerView = view.findViewById(R.id.overviewInfo)
        val materialCardView: com.google.android.material.card.MaterialCardView =
            view.findViewById(R.id.materialCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_type_overview_card, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: MemberOverviewAdapter.ViewHolder, position: Int) {
        // 收入 支出 占比最高的消费类型 占比最高的账户
        val oneMember = memberSummary[position]
        holder.cardTitle.text = oneMember.member
        holder.cardVerticalBar.renderData(
            VerticalBarData(100f, 100f, oneMember.color)
        )
        val memberDescription = getMemberDesciption(oneMember)
        holder.overviewInfo.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            if (itemDecorationCount == 0) {
                addItemDecoration(getItemDecoration())
            }
            adapter = MemberOverviewCardAdapter(memberDescription)
        }
        holder.materialCardView.setOnClickListener {
            MemberActivity.start(it.context, startDate, endDate, oneMember.member)
        }



    }

    class BillItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val barView: VerticalBar = view.findViewById(R.id.card_detail_bar)
        private val billType: TextView = view.findViewById(R.id.billType)
        private val billAmount: TextView = view.findViewById(R.id.billAmount)
        fun bind(model: DailyMember) {
            barView.renderData(VerticalBarData(100f, 100f, model.color))
            billType.text = model.member
            billAmount.text = model.amount.toMoneyFormatted()
        }
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = memberSummary.size

    private fun getMemberDesciption(oneMemberBillList: MemberDetail): List<Pair<String, String>> {
        val description: MutableList<Pair<String, String>> = mutableListOf()
        if (memberIncome != 0.00) {
            description.add(
                Pair(
                    "收入(占总收入比)：",
                    oneMemberBillList.income.toCHINADFormatted() + "(" + String.format(
                        "%.2f",
                        oneMemberBillList.income / memberIncome * 100
                    ) + "%)"
                )
            )
        } else {
            description.add(
                Pair(
                    "收入：",
                    oneMemberBillList.income.toCHINADFormatted()
                )
            )
        }
        if (memberExpenditure != 0.00) {
            description.add(
                Pair(
                    "支出(占总收入比)：",
                    oneMemberBillList.expenditure.toCHINADFormatted() + "(" + String.format(
                        "%.2f",
                        oneMemberBillList.expenditure / memberExpenditure * 100
                    ) + "%)"
                )
            )
        } else {
            description.add(
                Pair(
                    "支出：",
                    oneMemberBillList.expenditure.toCHINADFormatted()
                )
            )
        }
        description.add(
            Pair(
                "总计：",
                oneMemberBillList.lastAmount.toCHINADFormatted()
            )
        )
//        description.add(Pair("余额：",
//            oneAccountDetail.finalAmount.toCHINADFormatted()))
        if (oneMemberBillList.expenditure != 0.00) {
            description.add(
                Pair(
                    "支出最多分类：",
                    oneMemberBillList.outMaxCategory
                )
            )
            description.add(
                Pair(
                    "支出最多账户：",
                    oneMemberBillList.outMaxAccount
                )
            )
        }
        if (oneMemberBillList.income != 0.00) {
            description.add(
                Pair(
                    "收入最多分类：",
                    oneMemberBillList.inMaxCategory
                )
            )
            description.add(
                Pair(
                    "收入最多账户：",
                    oneMemberBillList.inMaxAccount
                )
            )
        }
        return description
    }

}