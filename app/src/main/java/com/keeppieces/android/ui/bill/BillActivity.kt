package com.keeppieces.android.ui.bill

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Bill
import com.keeppieces.android.ui.bill.picker.*
import kotlinx.android.synthetic.main.activity_bill.*
import java.time.LocalDate


private const val BillUpdate = 1
private const val BillAdd = 0
const val INTEGER_COUNT = 4
const val DECIMAL_COUNT = 2

class BillActivity : AppCompatActivity(),
    BillTimeDialog.BillTimeDialogListener,
    BillTypeDialog.BillTypeDialogListener,
    BillMemberDialog.BillMemberDialogListener,
    AddMemberDialog.AddMemberDialogListener,
    BillCategoryDialog.BillCategoryDialogListener,
    AddCategoryDialog.AddCategoryDialogListener,
    BillAccountDialog.BillAccountDialogListener,
    AddAccountDialog.AddAccountDialogListener,
    AddPrimaryDialog.AddPrimaryDialogListener,
    AddSecondaryDialog.AddSecondaryDialogListener,
    BillAccountCategoryDialog.BillAccountCategoryDialogListener{

    private var mode: Int = 0
    private lateinit var bill: Bill
    private lateinit var viewModel : BillViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

//        val toolbar: Toolbar = findViewById(R.id.billAddToolbar)
        setSupportActionBar(billAddToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""
////        setSupportActionBar(billAddToolbar)
//        setUpToolbar()

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        mode =  intent.getIntExtra("billMode", 0)
        bill = Bill(
            intent.getStringExtra("billTime").toString(),
            intent.getDoubleExtra("billAmount", 0.00),
            intent.getStringExtra("billAccount").toString(),
            intent.getStringExtra("billMember").toString(),
            intent.getStringExtra("billPrimary").toString(),
            intent.getStringExtra("billSecondary").toString(),
            intent.getStringExtra("billType").toString(),
            intent.getStringExtra("billInfo").toString()
        )
        bill.billId = intent.getLongExtra("billId", 0)

        billAmount.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        billAmountListen()
        initView()

        billCardTime.setOnClickListener {
            BillTimeDialog(billTime.text.toString()).show(supportFragmentManager, "BillTimeDialog")
        }

        billCardType.setOnClickListener {
            BillTypeDialog().show(supportFragmentManager, "BillTypeDialog")
        }

        billCardMember.setOnClickListener {
            BillMemberDialog().show(supportFragmentManager, "BillMemberDialog")
        }

        billCardCategory.setOnClickListener {
            if (billType.text == "转账") {
                BillAccountCategoryDialog().show(supportFragmentManager, "BillAccountCategoryDialog")
            } else {
                BillCategoryDialog().show(supportFragmentManager, "BillCategoryDialog")
            }
        }

        billCardAccount.setOnClickListener {
            BillAccountDialog().show(supportFragmentManager, "BillAccountDialog")
        }
    }

//    private fun setUpToolbar() {
//
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bill_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val nowBill = Bill(
                date = billTime.text.toString(),
                amount = if (billAmount.text.isEmpty()) 0.00 else billAmount.text.toString().toDouble(),
                account = billAccount.text.toString(),
                member = billMember.text.toString(),
                primaryCategory = billPrimary.text.toString(),
                secondaryCategory = billSecondary.text.toString(),
                type = billType.text.toString(),
                info = billInfo.text.toString()
        )
        if (item.itemId == R.id.billAdd) {
            if (mode == BillAdd) {
                if (nowBill.amount == 0.00) {
                    Toast.makeText(this,"输入金额不能为零",Toast.LENGTH_LONG).show()
                } else if (nowBill.account == nowBill.secondaryCategory && nowBill.type == "转账") {
                    Toast.makeText(this,"转入转出账户不能相同",Toast.LENGTH_LONG).show()
                } else {
                    viewModel.addBill(nowBill)
                    finish()
                    Log.d("Bill", "After Add")
                }
            } else if (mode == BillUpdate) {
                nowBill.billId = bill.billId
                if (nowBill.amount == 0.00) {
                    viewModel.deleteBill(nowBill)
                    finish()
                } else if (nowBill.account == nowBill.secondaryCategory && nowBill.type == "转账") {
                    Toast.makeText(this,"转入转出账户不能相同",Toast.LENGTH_LONG).show()
                } else {
                    viewModel.updateBill(nowBill)
                    finish()
                    Log.d("Bill", "After update")
                }
            }
        }
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
//        return super.onOptionsItemSelected(item)
    }

//    private fun billAdd(nowBill: Bill) {
//        if (nowBill.amount == 0.00) {
//            Toast.makeText(this,"输入金额不能为零",Toast.LENGTH_LONG).show()
//        } else if (nowBill.account == nowBill.secondaryCategory && nowBill.type == "转账") {
//            Toast.makeText(this,"转入转出账户不能相同",Toast.LENGTH_LONG).show()
//        } else {
//            viewModel.addBill(nowBill)
//            finish()
//        }
//    }
//
//    private fun billUpdate(nowBill: Bill) {
//        nowBill.billId = bill.billId
//        if (nowBill.amount == 0.00) {
//            viewModel.deleteBill(nowBill)
//            finish()
//        } else if (nowBill.account == nowBill.secondaryCategory && nowBill.type == "转账") {
//            Toast.makeText(this,"转入转出账户不能相同",Toast.LENGTH_LONG).show()
//        } else {
//            viewModel.updateBill(nowBill)
//            finish()
//        }
//    }

    private fun billAmountListen() {
        billAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text: String
                //输入为 . 的情况 设为0.
                if (start == 0 && s.toString() == "." && count == 1) {
                    text = "0."
                    billAmount.setText(text)
                    billAmount.setSelection(text.length)
                    //小数的位置设置
                } else if (s != null && s.toString().contains(".")) {
                    val splitText = s.toString().split(".")
                    if (splitText[0].length > INTEGER_COUNT && splitText[1].length <= DECIMAL_COUNT) {
                        text = s.subSequence(0, INTEGER_COUNT).toString() + "." + s.subSequence(
                            splitText[0].length,
                            s.length
                        ).toString()
                        billAmount.setText(text)
                        billAmount.setSelection(text.length)
                    } else if (splitText[0].length > INTEGER_COUNT && splitText[1].length > DECIMAL_COUNT) {
                        text = s.subSequence(0, INTEGER_COUNT).toString() + "." + s.subSequence(
                            splitText[0].length,
                            splitText[0].length + DECIMAL_COUNT
                        ).toString()
                        billAmount.setText(text)
                        billAmount.setSelection(text.length)
                    } else if (splitText[1].length > DECIMAL_COUNT) {
                        text =
                            s.subSequence(0, splitText[0].length).toString() + "." + s.subSequence(
                                splitText[0].length + 1,
                                splitText[0].length + 1 + DECIMAL_COUNT
                            ).toString()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        if (mode == BillUpdate) {
            billTime.text = bill.date
            billAmount.setText(bill.amount.toString())
            billAccount.text = bill.account
            billMember.text = bill.member
            billPrimary.text = bill.secondaryCategory
            billSecondary.text = bill.primaryCategory
            billType.text = bill.type
            billInfo.setText(bill.info)
        } else {
            billTime.text = LocalDate.now().toString()
            billAmount.setText("")
            billAccount.text = "微信"
            billMember.text = "自己"
            billPrimary.text = "其他"
            billSecondary.text = "其他"
            billType.text = "支出"
        }
    }

    companion object {
        fun start(context: Context, bill: Bill? = null) {
            val intent = Intent(context, BillActivity::class.java)
            if (bill != null) {
                intent.apply {
                    putExtra("billMode", BillUpdate)
                    putExtra("billTime", bill.date)
                    putExtra("billAmount", bill.amount)
                    putExtra("billAccount", bill.account)
                    putExtra("billMember", bill.member)
                    putExtra("billPrimary", bill.primaryCategory)
                    putExtra("billSecondary", bill.secondaryCategory)
                    putExtra("billType", bill.type)
                    putExtra("billInfo", bill.info)
                    putExtra("billId", bill.billId)
                }
            } else {
                intent.putExtra("billMode", BillAdd)
            }
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity)
            context.window.exitTransition = context.window.exitTransition
            context.startActivity(intent, options.toBundle())
        }
    }

    override fun onDialogPositiveClickForBillTime(dialog: DialogFragment) {
        billTime.text = (dialog as BillTimeDialog).date.toString()
    }

    override fun onDialogNegativeClickForBillTime(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClickForBillType(dialog: DialogFragment) {
        billType.text = (dialog as BillTypeDialog).type
        if (billType.text == "转账") {
            billPrimary.text = "转账"
            billSecondary.text = "微信"
        }
    }

    override fun onDialogNegativeClickForBillType(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClickForBillMember(dialog: DialogFragment) {
        billMember.text = (dialog as BillMemberDialog).member
    }

    override fun onDialogNegativeClickForBillMember(dialog: DialogFragment) {
    }

    override fun onDialogNeutralClickForBillMember(dialog: DialogFragment) {
        AddMemberDialog().show(supportFragmentManager, "AddMemberDialog")
    }

    override fun onDialogPositiveClickForAddMember(dialog: DialogFragment) {
        val text = (dialog as AddMemberDialog).text
        if (text != "") {
            billMember.text = text
        }
    }

    override fun onDialogNegativeClickForAddMember(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClickForBillCategory(dialog: DialogFragment) {
        billPrimary.text = (dialog as BillCategoryDialog).primaryCategory
        billSecondary.text = dialog.secondaryCategory
    }

    override fun onDialogNegativeClickForBillCategory(dialog: DialogFragment) {
    }

    override fun onDialogNeutralClickForBillCategory(dialog: DialogFragment) {
        val textPrimary = (dialog as BillCategoryDialog).primaryCategory
        val textSecondary = dialog.secondaryCategory
        AddCategoryDialog(textPrimary, textSecondary).show(supportFragmentManager, "AddCategoryDialog")
    }

    override fun onDialogPositiveClickForAddCategory(dialog: DialogFragment) {
        val textPrimary = (dialog as AddCategoryDialog).textPrimary
        val textSecondary = dialog.textSecondary
        if (textPrimary != "" && textSecondary != "") {
            billPrimary.text = textPrimary
            billSecondary.text = textSecondary
        }
    }

    override fun onDialogNegativeClickForAddCategory(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClickForBillAccount(dialog: DialogFragment) {
        billAccount.text = (dialog as BillAccountDialog).account
        billPrimary.text = "转账"
        billSecondary.text = "微信"
    }

    override fun onDialogNegativeClickForBillAccount(dialog: DialogFragment) {
    }

    override fun onDialogNeutralClickForBillAccount(dialog: DialogFragment) {
        AddAccountDialog().show(supportFragmentManager, "AddAccountDialog")
    }

    override fun onDialogPositiveClickForAddAccount(dialog: DialogFragment) {
        val account = (dialog as AddAccountDialog).text
        if (account != "") {
            billAccount.text = account
        }
    }

    override fun onDialogNegativeClickForAddAccount(dialog: DialogFragment) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                val view = currentFocus
                KeyboardUtils.hideKeyboard(ev, view, this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    object KeyboardUtils {
        fun hideKeyboard(event: MotionEvent, view: View?, activity: Activity) {
            try {
                if (view != null && view is EditText) {
                    val location = intArrayOf(0, 0)
                    view.getLocationInWindow(location)
                    val left = location[0]
                    val top = location[1]
                    val right = (left
                            + view.getWidth())
                    val bottom = top + view.getHeight()
                    // （判断是不是EditText获得焦点）判断焦点位置坐标是否在控件所在区域内，如果位置在控件区域外，则隐藏键盘
                    if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bottom) {
                        // 隐藏键盘
                        val token = view.getWindowToken()
                        val inputMethodManager =
                            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(
                            token,
                            InputMethodManager.HIDE_NOT_ALWAYS
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDialogPositiveClickForAddPrimary(dialog: DialogFragment) {
    }

    override fun onDialogNegativeClickForAddPrimary(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClickForAddSecondary(dialog: DialogFragment, dialogBefore: DialogFragment) {
    }

    override fun onDialogNegativeClickForAddSecondary(dialog: DialogFragment) {
    }

    override fun onDialogPositiveClickForBillAccountCategory(dialog: DialogFragment) {
        billSecondary.text = (dialog as BillAccountCategoryDialog).account
    }

    override fun onDialogNegativeClickForBillAccountCategory(dialog: DialogFragment) {
    }

    override fun onDialogNeutralClickForBillAccountCategory(dialog: DialogFragment) {
        AddAccountDialog().show(supportFragmentManager, "AddAccountDialog")
    }
}