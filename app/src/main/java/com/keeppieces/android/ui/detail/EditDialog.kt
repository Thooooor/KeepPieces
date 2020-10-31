package com.keeppieces.android.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.logic.data.BillRepository
import com.keeppieces.android.ui.bill.BillActivity
import kotlinx.android.synthetic.main.dialog_detail.*
import kotlin.concurrent.thread

class EditDialog(context: Context, val bill: Bill): DialogFragment() {
    private val myContext = context

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
            thread {
                BillRepository().deleteBill(bill)
            }
            dismiss()
        }
    }
}