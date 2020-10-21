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


class BillCategoryDialog : DialogFragment() {

    private lateinit var listener : BillCategoryDialogListener
    private lateinit var viewModel : BillViewModel
    var primaryCategory : String = "其他"
    var secondaryCategory : String = "其他"

    interface BillCategoryDialogListener{
        fun onDialogPositiveClickForBillCategory(dialog: DialogFragment)
        fun onDialogNegativeClickForBillCategory(dialog: DialogFragment)
        fun onDialogNeutralClickForBillCategory(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as BillCategoryDialogListener
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
        val inPrimaryCategory : WheelView = view.findViewById(R.id.options1)
        val inSecondaryCategory : WheelView = view.findViewById(R.id.options2)

        inPrimaryCategory.setCyclic(false)
        inPrimaryCategory.setTextColorCenter(
            -0x333334 //分割线之间的文字颜色
        )
        inSecondaryCategory.setCyclic(false)
        inSecondaryCategory.setTextColorCenter(
            -0x333334
        )

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        var primaryItem: List<String> = listOf("其他")
        var secondaryItem: List<String> = listOf("其他")
        val primaryLiveData = viewModel.findPrimaryList()
        val secondaryLiveData = viewModel.findSecondaryList()
        val connectItem = mutableListOf<List<String>>()
        primaryLiveData.observe(this, { primaryTmp ->
            primaryItem = if (primaryTmp.isEmpty()) listOf("其他") else primaryTmp.map { tmp -> tmp.name }
            for (item in primaryItem) {
                connectItem.add(listOf())
            }
            inPrimaryCategory.adapter = ArrayWheelAdapter(primaryItem)

            secondaryLiveData.observe(this, { secondaryTmp ->
                var index : Int
                if (secondaryTmp.isEmpty()) {
                    index = primaryItem.indexOf("其他")   //默认Primary里有 "其他"
                    if (index != -1) {
                        connectItem[index] = listOf("其他")
                    }
                } else {
                    for (item in secondaryTmp) {
                        index = primaryItem.indexOf(item.primaryName)   //所有二级类都有一级类，index恒不为-1
                        if (index != -1) {
                            connectItem[index] = connectItem[index] + listOf(item.name)
                        }
                    }
                }
                secondaryItem = connectItem[0]
                inSecondaryCategory.adapter = ArrayWheelAdapter(connectItem[0])
            })
        })

        inPrimaryCategory.setOnItemSelectedListener { index ->
            primaryCategory = primaryItem[index]
            inSecondaryCategory.adapter = ArrayWheelAdapter(connectItem[index])
            secondaryCategory = connectItem[index][0]
            secondaryItem = connectItem[index]
        }

        inSecondaryCategory.setOnItemSelectedListener { index ->
            secondaryCategory = secondaryItem[index]
        }

        builder.setTitle("类别")
            .setView(view)
            .setPositiveButton(
                "确定"
            ) { _, _ ->
                listener.onDialogPositiveClickForBillCategory(this)
            }

            .setNegativeButton(
                "取消"
            ) { _, _ ->
                listener.onDialogNegativeClickForBillCategory(this)
            }

            .setNeutralButton(
                "新建"
            ) { _, _ ->
                listener.onDialogNeutralClickForBillCategory(this)
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