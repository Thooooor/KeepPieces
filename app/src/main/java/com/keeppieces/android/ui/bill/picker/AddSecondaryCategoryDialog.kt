package com.keeppieces.android.ui.bill.picker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.ui.bill.BillViewModel

const val MaxSecondaryLength = 4
class AddSecondaryDialog(toPrimary: String, toSecondary: String, inDialogBefore: DialogFragment) : DialogFragment() {

    private lateinit var listener : AddSecondaryDialogListener
    private lateinit var viewModel : BillViewModel
    var textPrimary = toPrimary
    var textSecondary = toSecondary
    var dialogBefore = inDialogBefore as AddCategoryDialog

    interface AddSecondaryDialogListener {
        fun onDialogPositiveClickForAddSecondary(dialog: DialogFragment, dialogBefore: DialogFragment)
        fun onDialogNegativeClickForAddSecondary(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddSecondaryDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement AddSecondaryDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.bill_dialog_add_single, null)
            val addSecondary: EditText = view.findViewById(R.id.itemAdd)
            val imm : InputMethodManager = addSecondary.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            viewModel = ViewModelProvider(this)[BillViewModel::class.java]

            addSecondary.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textTmp: String
                    if (s != null && s.toString().length > MaxSecondaryLength) {
                        textTmp = s.subSequence(0, MaxSecondaryLength).toString()
                        addSecondary.setText(textTmp)
                        addSecondary.setSelection(textTmp.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            builder.setTitle("添加二级分类")
                .setView(view)
                .setPositiveButton("确定"
                ) { _, _ ->
                    if (addSecondary.text.toString() != "") {
                        textSecondary = addSecondary.text.toString()
                        dialogBefore.textPrimary = textPrimary
                        dialogBefore.textSecondary = textSecondary
                        dialogBefore.addPrimary.text = textPrimary
                        dialogBefore.addSecondary.text = textSecondary
                    }
//                    dialogBefore.onViewStateRestored(dialogBefore.arguments)
                    imm.hideSoftInputFromWindow(addSecondary.applicationWindowToken, 0)
                    listener.onDialogPositiveClickForAddSecondary(this, dialogBefore)
                }

                .setNegativeButton("取消"
                ) { _, _ ->
                    imm.hideSoftInputFromWindow(addSecondary.applicationWindowToken, 0)
                    listener.onDialogNegativeClickForAddSecondary(this)
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