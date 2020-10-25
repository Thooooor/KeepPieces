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


class BillTypeDialog : DialogFragment() {

    private lateinit var listener : BillTypeDialogListener
    private lateinit var viewModel : BillViewModel
    var type : String = "支出"

    interface BillTypeDialogListener{
        fun onDialogPositiveClickForBillType(dialog: DialogFragment)
        fun onDialogNegativeClickForBillType(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as BillTypeDialogListener
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
        val inType : WheelView = view.findViewById(R.id.options1)

        inType.setCyclic(false)
        inType.setTextColorCenter(
            -0x333334 //分割线之间的文字颜色
        )

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        val cardItem: ArrayList<String> = viewModel.findTypeList()
        val adapter = ArrayWheelAdapter(cardItem)
        inType.adapter = adapter
        type = cardItem[0]

        inType.setOnItemSelectedListener { index ->
            type = cardItem[index]
        }

        builder.setTitle("方式选择")
            .setView(view)
            .setPositiveButton(
                "确定"
            ) { _, _ ->
                listener.onDialogPositiveClickForBillType(this)
            }

            .setNegativeButton(
                "取消"
            ) { _, _ ->
                listener.onDialogNegativeClickForBillType(this)
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