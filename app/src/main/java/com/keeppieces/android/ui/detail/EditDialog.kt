package com.keeppieces.android.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.bill.BillActivity
import com.keeppieces.android.ui.bill.BillViewModel
import kotlinx.android.synthetic.main.dialog_detail.*

class EditDialog(context: Context, val bill: Bill): DialogFragment() {
    private val myContext = context
    private val viewModel: BillViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.editButton.setOnClickListener {
            dismiss()
            BillActivity.start(myContext, bill)
        }
        this.deleteButton.setOnClickListener {
            viewModel.deleteBill(bill)
            dismiss()
        }
    }
}