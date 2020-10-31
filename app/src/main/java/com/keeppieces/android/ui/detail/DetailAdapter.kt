package com.keeppieces.android.ui.detail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.inflate
import com.keeppieces.android.extension.toMoneyFormatted
import com.keeppieces.android.logic.data.Bill

class DetailAdapter(private val items: List<Bill>, val fragmentManager: FragmentManager) : RecyclerView.Adapter<DetailItemViewHolder>() {
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailItemViewHolder {
        return DetailItemViewHolder(parent.inflate(R.layout.item_detail))
    }

    override fun onBindViewHolder(
        holder: DetailItemViewHolder,
        position: Int
    ) {
        holder.bind(items[position], fragmentManager)
    }
}

class DetailItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val billSecondary: TextView = view.findViewById(R.id.billDetailSecondary)
    private val billAmount: TextView = view.findViewById(R.id.billAmount)
    private val billDate: TextView = view.findViewById(R.id.billDate)
    private val billAccount: TextView = view.findViewById(R.id.billAccount)
    private  val billType: TextView = view.findViewById(R.id.billType)

    fun bind(model: Bill, fragmentManager: FragmentManager) {
        billSecondary.text = model.secondaryCategory
        billAmount.text = model.amount.toMoneyFormatted()
        billDate.text = model.date
        billAccount.text = model.account
        billType.text = when (model.type) {
            "收入" -> "+"
            "支出" -> "-"
            else -> "￥"
        }
        view.setOnClickListener {
            val editDialog = EditDialog(it.context ,model)
            editDialog.show(fragmentManager, "edit")
        }
    }
}