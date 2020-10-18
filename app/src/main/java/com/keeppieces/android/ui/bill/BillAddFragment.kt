package com.keeppieces.android.ui.bill

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.bill.picker.KeyboardPopupWindow
import kotlinx.android.synthetic.main.fragment_bill_add.*
import java.time.LocalDate

const val INTEGER_COUNT = 4
const val DECIMAL_COUNT = 2
class BillAddFragment(val bill: Bill? = null) : Fragment() {

    private lateinit var viewModel : BillViewModel
    private lateinit var initAddBill: Bill

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        val keyboardWindow = KeyboardPopupWindow(context, view, billAmount, false)

        billAdd.setOnClickListener {
            billAdd()
        }

        billBack.setOnClickListener {
            activity?.finish()
        }

        billAmount!!.setOnClickListener {
            keyboardWindow.show()
        }

        billAmount!!.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (activity != null) {
                keyboardWindow.refreshKeyboardOutSideTouchable(!hasFocus)
            }
            if (hasFocus) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(billAmount.windowToken, 0)
            }
        }

        billAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text : String
                //输入为 . 的情况 设为0.
                if (start == 0 && s.toString() == "." && count == 1) {
                    text = "0."
                    billAmount.setText(text)
                    billAmount.setSelection(text.length)
                //小数的位置设置
                } else if (s != null && s.toString().contains(".")){
                    val splitText = s.toString().split(".")
                    if (splitText[0].length > INTEGER_COUNT && splitText[1].length <= DECIMAL_COUNT) {
                        text = s.subSequence(0, INTEGER_COUNT).toString() + "." + s.subSequence(splitText[0].length, s.length).toString()
                        billAmount.setText(text)
                        billAmount.setSelection(text.length)
                    } else if (splitText[0].length > INTEGER_COUNT && splitText[1].length > DECIMAL_COUNT) {
                        text = s.subSequence(0, INTEGER_COUNT).toString() + "." + s.subSequence(splitText[0].length, splitText[0].length+ DECIMAL_COUNT).toString()
                        billAmount.setText(text)
                        billAmount.setSelection(text.length)
                    } else if (splitText[1].length > DECIMAL_COUNT) {
                        text = s.subSequence(0,splitText[0].length).toString() + "." + s.subSequence(splitText[0].length+1 ,splitText[0].length+1+DECIMAL_COUNT).toString()
                        billAmount.setText(text)
                        billAmount.setSelection(text.length)
                    }
                //整数的位置设置
                } else if (s.toString().length > INTEGER_COUNT) {
                    if (s != null) {
                        text = s.subSequence(0, INTEGER_COUNT).toString()
                        billAmount.setText(text)
                        billAmount.setSelection(text.length)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun billAdd() {
        if (bill == null) {
            viewModel.addBill(nowBill())
            activity?.finish()
        } else {
            val nowBill = nowBill()
            viewModel.updateBill(nowBill())
            val intent = Intent()
            intent.apply {
                putExtra("billTime", nowBill.date)
                putExtra("billAmount", nowBill.amount)
                putExtra("billAccount", nowBill.account)
                putExtra("billMember", nowBill.member)
                putExtra("billPrimary", nowBill.primaryCategory)
                putExtra("billSecondary", nowBill.secondaryCategory)
                putExtra("billType", nowBill.type)
            }
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val billTime: TextView = view.findViewById(R.id.billTime)
        val billAmount: TextView = view.findViewById(R.id.billAmount)
        val billAccount: TextView = view.findViewById(R.id.billAccount)
        val billMember: TextView = view.findViewById(R.id.billMember)
        val billPrimaryCategory: TextView = view.findViewById(R.id.billPrimary)
        val billSecondaryCategory: TextView = view.findViewById(R.id.billSecondary)
        val billType: TextView = view.findViewById(R.id.billType)

        if(bill != null) {
            billTime.text = bill.date
            billAmount.text = bill.amount.toString()
            billAccount.text = bill.account
            billMember.text = bill.member
            billPrimaryCategory.text = bill.secondaryCategory
            billSecondaryCategory.text = bill.primaryCategory
            billType.text = bill.type
        } else {
            initAddBill()
            billTime.text = initAddBill.date
            billAmount.text = initAddBill.amount.toString()
            billAccount.text = initAddBill.account
            billMember.text = initAddBill.member
            billPrimaryCategory.text = initAddBill.primaryCategory
            billSecondaryCategory.text = initAddBill.secondaryCategory
            billType.text = initAddBill.type
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAddBill() {
        initAddBill = Bill(
            LocalDate.now().toString(),
        0.00,
        "微信",
        "自己",
        "其他",
        "其他",
        "支出")
    }

    private fun nowBill() : Bill {
        return Bill(
            date = billTime.text.toString(),
            amount = billAmount.text.toString().toDouble(),
            account = billAccount.text.toString(),
            member = billMember.text.toString(),
            primaryCategory = billPrimary.text.toString(),
            secondaryCategory = billSecondary.text.toString(),
            type = billType.text.toString()
        )
    }
}