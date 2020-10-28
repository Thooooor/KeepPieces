package com.keeppieces.android.ui.PrimaryCategory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill

class SecondaryCategoryBillFlowFragmentAdapter(val billList:List<Bill>) : RecyclerView.Adapter<SecondaryCategoryBillFlowFragmentAdapter.ViewHolder>(){

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val billTypeSymbol:TextView = view.findViewById(R.id.billType)
        val billDetailSecondary:TextView = view.findViewById(R.id.billDetailSecondary)
        val billAccount:TextView = view.findViewById(R.id.billAccount)
        val billDate:TextView = view.findViewById(R.id.billDate)
        val billAmountString:TextView = view.findViewById(R.id.billAmount)
        val detailArrow:ImageView = view.findViewById(R.id.detailArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bill = billList[position]
        holder.billAccount.text = bill.account
        holder.billAmountString.text = bill.amount.toString()
        holder.billDate.text= bill.date
        holder.billDetailSecondary.text = bill.secondaryCategory
        holder.billTypeSymbol.text = when(bill.type) {
            "收入" -> "+￥"
            "支出" -> "-￥"
            "转账" -> " ￥"
            else -> "TypeError"
        }
        holder.detailArrow.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = billList.size


}