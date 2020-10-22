package com.keeppieces.android.ui.bill.picker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.logic.data.Account
import com.keeppieces.android.ui.bill.BillViewModel

const val MaxAccountLength = 4
class AddAccountDialog : DialogFragment() {

    private lateinit var listener : AddAccountDialogListener
    private lateinit var viewModel : BillViewModel
    lateinit var text : String

    interface AddAccountDialogListener {
        fun onDialogPositiveClickForAddAccount(dialog: DialogFragment)
        fun onDialogNegativeClickForAddAccount(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddAccountDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement AddAccountDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.bill_dialog_add_single, null)
            val addAccount: EditText = view.findViewById(R.id.itemAdd)

            viewModel = ViewModelProvider(this)[BillViewModel::class.java]

            addAccount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textTmp: String
                    if (s != null && s.toString().length > MaxAccountLength) {
                        textTmp = s.subSequence(0, MaxAccountLength).toString()
                        addAccount.setText(textTmp)
                        addAccount.setSelection(textTmp.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            builder.setTitle("添加成员")
                .setView(view)
                .setPositiveButton("确定"
                ) { _, _ ->
                    if (addAccount.toString() != "") {
                        val account = Account(addAccount.text.toString())
                        viewModel.addAccount(account)
                        text = addAccount.text.toString()
                    }
                    listener.onDialogPositiveClickForAddAccount(this)
                }

                .setNegativeButton("取消"
                ) { _, _ ->
                    listener.onDialogNegativeClickForAddAccount(this)
                }
            builder.create()
        } ?:throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_status_bar)
    }
}