package com.keeppieces.android.ui.overview

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.dialog_set_month_budget.*
import java.time.LocalDate

class AddMonthBudgetDialog:DialogFragment() {
    private lateinit var setter: SetMonthBudgetInterface
    private val monthBudgetFile = "month_budget"
    private val nowMonthBudgetString = "nowMonthBudget"
    private val nowMonthString = "nowMonth"
    private val nowYearString = "nowYear"
    @RequiresApi(Build.VERSION_CODES.O)
    val nowDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val nowMonth = nowDate.monthValue
    @RequiresApi(Build.VERSION_CODES.O)
    val nowYear = nowDate.year

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            return inflater.inflate(R.layout.dialog_set_month_budget, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(monthBudgetFile,MODE_PRIVATE)
        val savedYear = prefs.getInt(nowYearString,-1)
        val savedMonth = prefs.getInt(nowMonthString,-1)
        val savedMonthBudget = prefs.getString(nowMonthBudgetString,null)
        if (savedYear == nowYear && savedMonth == nowMonth && savedMonthBudget!=null){
            edit_month_budget.setText(savedMonthBudget)
            edit_month_budget.focusable = View.FOCUSABLE_AUTO
            edit_month_budget.hint = ""
        }
        give_up_button.setOnClickListener {
                dismiss()
            }
        ok_button.setOnClickListener {
            val monthBudget = edit_month_budget.text.toString()
            if (monthBudget.isNotEmpty()) {
                prefs.edit().apply {
                    putString(nowMonthBudgetString,monthBudget)
                    putInt(nowMonthString,nowMonth)
                    putInt(nowYearString,nowYear)
                    apply()
                }
                setter.setMonthBudgetButtonText(monthBudget)
                Toast.makeText(requireContext(), "设置成功", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            else {
                Toast.makeText(requireContext(),"请重新输入预算",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            setter = context as SetMonthBudgetInterface  // 接口实现
            Log.d("checkpoint",setter.toString())
        } catch (e:ClassCastException) {
            throw ClassCastException(context.toString() + "must implement setMonthBudgetInterface")
        }
    }
    interface SetMonthBudgetInterface {
        fun setMonthBudgetButtonText(monthBudget: String)
    }

}