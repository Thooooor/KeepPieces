package com.keeppieces.android.ui.categoryDetail.primaryCategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.GeneralPrimary
import com.keeppieces.line_indicator.VerticalBarData
import kotlin.math.abs

class PrimaryCategoryOverviewAdapter(
    private val primarySummary: List<GeneralPrimary>,
    private val primaryIncome: Double,
    private val primaryExpenditure: Double,
    private val startDate: String,
    private val endDate: String
) :
    RecyclerView.Adapter<PrimaryCategoryOverviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.carTitle)
        val cardVerticalBar: com.keeppieces.line_indicator.VerticalBar =
            view.findViewById(R.id.cardVerticalBar)
        val overviewInfo: RecyclerView = view.findViewById(R.id.overviewInfo)
        val materialCardView: com.google.android.material.card.MaterialCardView =
            view.findViewById(R.id.materialCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_type_overview_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 收入/支出/两者都有 二级类个数 占比最高的消费类型
        val onePrimaryCategory = primarySummary[position]
        holder.cardTitle.text = onePrimaryCategory.primaryCategory
        holder.cardVerticalBar.renderData(
            VerticalBarData(100f, 100f, onePrimaryCategory.color)
        )
        val primaryDescription = getDesciptionOfThePrimary(onePrimaryCategory)
        holder.overviewInfo.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            if (itemDecorationCount == 0) {
                addItemDecoration(getItemDecoration())
            }
            adapter = PrimaryCategoryOverviewCardAdapter(primaryDescription)
        }
        holder.materialCardView.setOnClickListener {
            PrimaryCategoryDetailActivity.start(
                it.context,
                startDate,
                endDate,
                onePrimaryCategory.primaryCategory
            )
        }

    }

    override fun getItemCount(): Int = primarySummary.size  // 一级类数量

    private fun getDesciptionOfThePrimary(onePrimaryBillList: GeneralPrimary): List<Pair<String, String>> {
        val description: MutableList<Pair<String, String>> = mutableListOf()
        if (abs(primaryIncome - 0) > 0.01 && abs(onePrimaryBillList.income - 0) > 0.01) {
            description.add(
                Pair(
                    "${onePrimaryBillList.primaryCategory}收入(占总收入比)：",
                    onePrimaryBillList.income.toCHINADFormatted() + "(" + String.format(
                        "%.2f",
                        onePrimaryBillList.income / primaryIncome * 100
                    ) + "%)"
                )
            )
        }
        if (abs(primaryExpenditure - 0) > 0.01 && abs(onePrimaryBillList.expenditure - 0) > 0.01) {
            description.add(
                Pair(
                    "${onePrimaryBillList.primaryCategory}支出(占总支出比)：",
                    onePrimaryBillList.expenditure.toCHINADFormatted() + "(" + String.format(
                        "%.2f",
                        onePrimaryBillList.expenditure / primaryExpenditure * 100
                    ) + "%)"
                )
            )
        }
        description.add(
            Pair(
                "${onePrimaryBillList.primaryCategory}下的二级类个数:",
                onePrimaryBillList.secondaryNum.toString()
            )
        )
        return description
    }
}
