package com.keeppieces.android.ui.account.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.ui.account.AccountActivity
import com.keeppieces.android.ui.account.AccountDetail
import com.keeppieces.line_indicator.VerticalBar
import com.keeppieces.line_indicator.VerticalBarData

class AccountOverviewAdapter(
        private val accountList: MutableList<AccountDetail>,
        private val inAmount: Double,
        private val outAmount: Double,
        private val startDate: String,
        private val endDate: String
) :
    RecyclerView.Adapter<AccountOverviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val cardTitle:TextView = view.findViewById(R.id.carTitle)
        val cardVerticalBar: VerticalBar=view.findViewById(R.id.cardVerticalBar)
        val overviewInfo: RecyclerView = view.findViewById(R.id.overviewInfo)
        val materialCardView: MaterialCardView =view.findViewById(R.id.materialCardView)
    }

    override fun getItemCount(): Int = accountList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_type_overview_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oneAccount = accountList[position]
        holder.cardTitle.text = oneAccount.account
        holder.cardVerticalBar.renderData(
            VerticalBarData(100f, 100f, oneAccount.color)
        )
        val description = getAccountDescription(oneAccount)
        holder.overviewInfo.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            if (itemDecorationCount ==0) {
                addItemDecoration(getItemDecoration())
            }
            adapter = AccountOverviewCardAdapter(description)
        }

        holder.materialCardView.setOnClickListener {
            AccountActivity.start(it.context, startDate, endDate, oneAccount.account)
        }
    }

    private fun getAccountDescription(oneAccountDetail: AccountDetail):List<Pair<String,String>>{
        val description:MutableList<Pair<String,String>> = mutableListOf()
        if (inAmount != 0.00) {
            description.add(Pair("收入(占总收入比)：",
                oneAccountDetail.inAmount.toCHINADFormatted()+"("+String.format("%.2f",oneAccountDetail.inAmount/inAmount*100)+"%)"))
        } else {
            description.add(Pair("收入：",
                oneAccountDetail.inAmount.toCHINADFormatted()))
        }
        if (outAmount != 0.00) {
            description.add(Pair("支出(占总收入比)：",
                oneAccountDetail.outAmount.toCHINADFormatted()+"("+String.format("%.2f",oneAccountDetail.outAmount/outAmount*100)+"%)"))
        } else {
            description.add(Pair("支出：",
                oneAccountDetail.outAmount.toCHINADFormatted()))
        }
        description.add(Pair("总计：",
            oneAccountDetail.lastAmount.toCHINADFormatted()))
//        description.add(Pair("余额：",
//            oneAccountDetail.finalAmount.toCHINADFormatted()))
        if (oneAccountDetail.outAmount != 0.00) {
            description.add(Pair("支出最多分类：",
                oneAccountDetail.outMaxCategory))
            description.add(Pair("支出最多成员：",
                oneAccountDetail.outMaxMember))
        }
        if (oneAccountDetail.inAmount != 0.00) {
            description.add(Pair("收入最多分类：",
                oneAccountDetail.inMaxCategory))
            description.add(Pair("收入最多成员：",
                oneAccountDetail.inMaxMember))
        }
        return description
    }

//    class BillItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
//        private val barView: VerticalBar = view.findViewById(R.id.card_detail_bar)
//        private val billType: TextView = view.findViewById(R.id.billType)
//        private val billAmount: TextView = view.findViewById(R.id.billAmount)
//        fun bind(model: DailyAccount) {
//            barView.renderData(VerticalBarData(100f, 100f, model.color))
//            billType.text = model.account
//            billAmount.text = model.amount.toMoneyFormatted()
//        }
//    }
//
//    class CardItemViewHolder(private  val view:View) :RecyclerView.ViewHolder(view){
//        private val barView: VerticalBar = view.findViewById(R.id.card_detail_bar)
//        private val accountType: TextView = view.findViewById(R.id.billType)
//
//        //private val billAmount: TextView = view.findViewById(R.id.billAmount)
//        fun bind(model: DailyAccount) {
//            barView.renderData(VerticalBarData(100f, 100f, model.color))
//            accountType.text = model.account
//            //billAmount.text = model.amount.toMoneyFormatted()
//        }
//    }
}