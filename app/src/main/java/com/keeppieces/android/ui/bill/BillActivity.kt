package com.keeppieces.android.ui.bill

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
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
    AddAccountDialog.AddAccountDialogListener{

    private var mode: Int = 0
    private lateinit var bill: Bill
    private lateinit var viewModel : BillViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        setSupportActionBar(billAddToolbar)
        setUpToolbar()

        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        mode =  intent.getIntExtra("billMode", 0)
        bill = Bill(intent.getStringExtra("billTime").toString(),
            intent.getDoubleExtra("billAmount", 0.00),
            intent.getStringExtra("billAccount").toString(),
            intent.getStringExtra("billMember").toString(),
            intent.getStringExtra("billPrimary").toString(),
            intent.getStringExtra("billSecondary").toString(),
            intent.getStringExtra("billType").toString())
        bill.billId = intent.getLongExtra("billId",0)

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
            BillCategoryDialog().show(supportFragmentManager, "BillCategoryDialog")
        }

        billCardAccount.setOnClickListener {
            BillAccountDialog().show(supportFragmentManager, "BillAccountDialog")
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(billAddToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bill_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.billAdd -> {
                if (mode == BillAdd) {
                    billAdd()
                } else if (mode == BillUpdate){
                    billUpdate()
                }
                finish()
            }
        }
        return true
    }

    private fun billAdd() {
        val nowBill = Bill(
            date = billTime.text.toString(),
            amount = if (billAmount.text.isEmpty()) 0.00 else billAmount.text.toString().toDouble(),
            account = billAccount.text.toString(),
            member = billMember.text.toString(),
            primaryCategory = billPrimary.text.toString(),
            secondaryCategory = billSecondary.text.toString(),
            type = billType.text.toString()
        )
        if (nowBill.amount != 0.00) {
            viewModel.addBill(nowBill)
        }
    }

    private fun billUpdate() {
        val nowBill = Bill(
            date = billTime.text.toString(),
            amount = billAmount.text.toString().toDouble(),
            account = billAccount.text.toString(),
            member = billMember.text.toString(),
            primaryCategory = billPrimary.text.toString(),
            secondaryCategory = billSecondary.text.toString(),
            type = billType.text.toString()
        )
        nowBill.billId = bill.billId
        if (nowBill.amount != 0.00) {
            viewModel.updateBill(nowBill)
        } else {
            viewModel.deleteBill(bill)
        }
    }

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            if (data != null) {
//                val resultBill = Bill(
//                    data.getStringExtra("billTime").toString(),
//                    data.getDoubleExtra("billAmount", 0.00),
//                    data.getStringExtra("billAccount").toString(),
//                    data.getStringExtra("billMember").toString(),
//                    data.getStringExtra("billPrimary").toString(),
//                    data.getStringExtra("billSecondary").toString(),
//                    data.getStringExtra("billType").toString())
////                billSeeFragment(resultBill)
//            } else {
//                finish()
//            }
//        }
//    }

//    private fun billAddFragment() {
//        val fragment = BillAddFragment(null)
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.apply {
//            replace(R.id.bill_main, fragment)
//            commit()
//        }
//    }

//    private fun billAddFragmentUpdate(bill: Bill) {
//        Log.d("BillActivity","billAddUpdate")
//        val fragment = BillAddFragment(bill)
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.apply {
//            replace(R.id.bill_main, fragment)
//            commit()
//        }
//    }

//    private fun billSeeFragment(bill: Bill) {
//        Log.d("BillActivity","billSeeUpdate")
//        val fragment = BillSeeFragment(bill)
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.apply {
//            replace(R.id.bill_main, fragment)
//            commit()
//        }
//    }

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
        fun start(context: Context, bill: Bill?=null) {
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
        AddCategoryDialog().show(supportFragmentManager,"AddCategoryDialog")
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
}
