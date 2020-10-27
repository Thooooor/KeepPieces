package com.keeppieces.android.ui.account.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R

class AccountOverviewCardAdapter(private val accountDescription: List<Pair<String,String>>):
    RecyclerView.Adapter<AccountOverviewCardAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val infoName: TextView = view.findViewById(R.id.infoName)
        val infoDetail: TextView = view.findViewById(R.id.infoDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type_overview_info,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val description = accountDescription[position]
        holder.infoName.text = description.first
        holder.infoDetail.text = description.second
    }

    override fun getItemCount(): Int = accountDescription.size


}