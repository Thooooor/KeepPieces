package com.keeppieces.android.ui.bill.picker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import com.keeppieces.android.R
import com.keeppieces.android.ui.bill.BillViewModel


class BillAccountDialog : DialogFragment() {

    private lateinit var listener : BillAccountDialogListener
    private lateinit var viewModel : BillViewModel
     var account : String = "微信"

    interface BillAccountDialogListener{
        fun onDialogPositiveClickForBillAccount(dialog: DialogFragment)
        fun onDialogNegativeClickForBillAccount(dialog: DialogFragment)
        fun onDialogNeutralClickForBillAccount(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as BillAccountDialogListener
        }catch (e: ClassCastException){
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.bill_dialog_choose, null)
        val inAccount : WheelView = view.findViewById(R.id.options1)

        inAccount.setCyclic(false)
        inAccount.setTextColorCenter(
            -0x333334 //分割线之间的文字颜色
        )

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        var cardItem: List<String> = listOf("微信")
        viewModel.findAccountList().observe(this, { listTmp ->
            cardItem = if (listTmp.isEmpty()) listOf("微信") else listTmp.map { tmp -> tmp.name }
            inAccount.adapter = ArrayWheelAdapter(cardItem)
            account = cardItem[0]
        })

        inAccount.setOnItemSelectedListener { index ->
             account = cardItem[index]
        }

        builder.setTitle("成员选择")
            .setView(view)
            .setPositiveButton(
                "确定"
            ) { _, _ ->
                listener.onDialogPositiveClickForBillAccount(this)
            }

            .setNegativeButton(
                "取消"
            ) { _, _ ->
                listener.onDialogNegativeClickForBillAccount(this)
            }

            .setNeutralButton(
                "新建"
            ) { _, _ ->
                listener.onDialogNeutralClickForBillAccount(this)
            }
        builder.create()
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_status_bar)
    }
}