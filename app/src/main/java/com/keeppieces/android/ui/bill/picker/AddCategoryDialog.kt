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
import com.keeppieces.android.logic.data.PrimaryCategory
import com.keeppieces.android.logic.data.SecondaryCategory
import com.keeppieces.android.ui.bill.BillViewModel
import kotlinx.android.synthetic.main.bill_dialog_add_two.*

const val MaxCategoryLength = 4
class AddCategoryDialog : DialogFragment() {

    private lateinit var listener : AddCategoryDialogListener
    private lateinit var viewModel : BillViewModel
    lateinit var textPrimary : String
    lateinit var textSecondary : String

    interface AddCategoryDialogListener {
        fun onDialogPositiveClickForAddCategory(dialog: DialogFragment)
        fun onDialogNegativeClickForAddCategory(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddCategoryDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement AddCategoryDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.bill_dialog_add_two, null)
            val addPrimaryCategory: EditText = view.findViewById(R.id.addPrimary)
            val addSecondaryCategory: EditText = view.findViewById(R.id.addSecondary)

            viewModel = ViewModelProvider(this)[BillViewModel::class.java]

            addPrimaryCategory.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textTmp: String
                    if (s != null && s.toString().length > MaxCategoryLength) {
                        textTmp = s.subSequence(0, MaxCategoryLength).toString()
                        addPrimaryCategory.setText(textTmp)
                        addPrimaryCategory.setSelection(textTmp.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            addSecondaryCategory.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textTmp: String
                    if (s != null && s.toString().length > MaxCategoryLength) {
                        textTmp = s.subSequence(0, MaxCategoryLength).toString()
                        addSecondaryCategory.setText(textTmp)
                        addSecondaryCategory.setSelection(textTmp.length)
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })

            builder.setTitle("添加类别")
                .setView(view)
                .setPositiveButton("确定"
                ) { _, _ ->
                    if (addPrimaryCategory.toString() != "" && addSecondaryCategory.toString() != "") {
                        val primaryCategory = PrimaryCategory(addPrimaryCategory.text.toString())
                        viewModel.addPrimary(primaryCategory)
                        val secondaryCategory = SecondaryCategory(addSecondaryCategory.text.toString(), addPrimaryCategory.text.toString())
                        viewModel.addSecondary(secondaryCategory)
                        textPrimary = addPrimaryCategory.text.toString()
                        textSecondary = addSecondaryCategory.text.toString()
                    }
                    listener.onDialogPositiveClickForAddCategory(this)
                }

                .setNegativeButton("取消"
                ) { _, _ ->
                    listener.onDialogNegativeClickForAddCategory(this)
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