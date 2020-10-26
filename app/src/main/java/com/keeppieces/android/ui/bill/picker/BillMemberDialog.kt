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


class BillMemberDialog : DialogFragment() {

    private lateinit var listener : BillMemberDialogListener
    private lateinit var viewModel : BillViewModel
    var member : String = "自己"

    interface BillMemberDialogListener{
        fun onDialogPositiveClickForBillMember(dialog: DialogFragment)
        fun onDialogNegativeClickForBillMember(dialog: DialogFragment)
        fun onDialogNeutralClickForBillMember(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as BillMemberDialogListener
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
        val inMember : WheelView = view.findViewById(R.id.options1)

        inMember.setCyclic(false)
        inMember.setTextColorCenter(
            -0x333334 //分割线之间的文字颜色
        )

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        var cardItem: List<String> = listOf("自己","无成员")
        viewModel.findMemberList().observe(this, { listTmp ->
            cardItem = if (listTmp.isEmpty()) listOf("自己","无成员") else listTmp.map { tmp -> tmp.name }
            inMember.adapter = ArrayWheelAdapter(cardItem)
            member = cardItem[0]
        })

        inMember.setOnItemSelectedListener { index ->
            member = cardItem[index]
        }

        builder.setTitle("成员选择")
            .setView(view)
            .setPositiveButton(
                "确定"
            ) { _, _ ->
                listener.onDialogPositiveClickForBillMember(this)
            }

            .setNegativeButton(
                "取消"
            ) { _, _ ->
                listener.onDialogNegativeClickForBillMember(this)
            }

            .setNeutralButton(
                "新建"
            ) { _, _ ->
                listener.onDialogNeutralClickForBillMember(this)
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