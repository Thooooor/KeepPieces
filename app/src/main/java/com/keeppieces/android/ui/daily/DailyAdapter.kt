package com.keeppieces.android.ui.daily

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.inflate
import com.keeppieces.android.logic.model.DailyItem

class DailyAdapter(private val items: List<DailyItem>) : RecyclerView.Adapter<DailyItemViewHolder>() {
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemViewHolder {
        return DailyItemViewHolder(parent.inflate(R.layout.item_daily))
    }

    override fun onBindViewHolder(holder: DailyItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class DailyItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val billType: TextView = view.findViewById(R.id.billType)
    private val billAmount: TextView = view.findViewById(R.id.billAmount)
    private val billDate: TextView = view.findViewById(R.id.billDate)

    fun bind(model: DailyItem) {
        billType.text = model.type
        billAmount.text = model.amount.toString()
        billDate.text = model.date.toString()
    }
}