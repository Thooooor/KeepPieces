package com.keeppieces.android.ui.bill.picker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.keeppieces.android.R
import java.time.LocalDate

const val maxYear = 2200
const val minYear = 1900

class BillTimeDialog(private val inputDate: String) : DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.now()

    private lateinit var listener : BillTimeDialogListener
    interface BillTimeDialogListener{
        fun onDialogPositiveClickForBillTime(dialog: DialogFragment)
        fun onDialogNegativeClickForBillTime(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as BillTimeDialogListener
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
        val view = inflater.inflate(R.layout.bill_dialog_date, null)
        val year : NumberPicker = view.findViewById(R.id.yearPicker)
        val month : NumberPicker = view.findViewById(R.id.monthPicker)
        val day : NumberPicker = view.findViewById(R.id.dayPicker)

        date = LocalDate.parse(inputDate)
        year.apply {
            maxValue = maxYear
            minValue = minYear
            value = date.year
            wrapSelectorWheel = false
        }
        month.apply {
            maxValue = 12
            minValue = 1
            value = date.monthValue
            wrapSelectorWheel = false
        }
        day.value = date.dayOfMonth

        daySet(year.value, month.value, day)
        year.setOnValueChangedListener { _, _, _ ->
            daySet(year.value, month.value, day)
        }

        month.setOnValueChangedListener { _, _, _ ->
            daySet(year.value, month.value, day)
        }

        builder.setTitle("日期选择")
            .setView(view)

            .setPositiveButton(
                "确定"
            ) { _, _ ->
                date = LocalDate.of(year.value, month.value, day.value)
                listener.onDialogPositiveClickForBillTime(this)
            }

            .setNegativeButton(
                "取消"
            ) { _, _ ->
                listener.onDialogNegativeClickForBillTime(this)
            }
        builder.create()
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_status_bar)
    }

    private fun daySet(year: Int, month: Int, day: NumberPicker) {
        if (month in listOf<Int>(1,3,5,7,8,10,12)) {
            day.apply {
                maxValue = 31
                minValue = 1
                value = day.value
            }
        } else if (month in listOf<Int>(4,6,9,11)) {
            day.apply {
                maxValue = 30
                minValue = 1
                value = day.value
            }
        } else {
            if (year%4 == 0 && year%400 != 0) {
                day.apply {
                    maxValue = 29
                    minValue = 1
                    value = day.value
                }
            } else {
                day.apply {
                    maxValue = 28
                    minValue = 1
                    value = day.value
                }
            }
        }
    }
}