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
import com.keeppieces.android.logic.data.Member
import com.keeppieces.android.ui.bill.BillViewModel

const val MaxMemberLength = 4
class AddMemberDialog : DialogFragment() {

    private lateinit var listener : AddMemberDialogListener
    private lateinit var viewModel : BillViewModel
    lateinit var text : String

    interface AddMemberDialogListener {
        fun onDialogPositiveClickForAddMember(dialog: DialogFragment)
        fun onDialogNegativeClickForAddMember(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddMemberDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement AddMemberDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.bill_dialog_add_single, null)
            val addMember: EditText = view.findViewById(R.id.itemAdd)

            viewModel = ViewModelProvider(this)[BillViewModel::class.java]

            addMember.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textTmp: String
                    if (s != null && s.toString().length > MaxMemberLength) {
                        textTmp = s.subSequence(0, MaxMemberLength).toString()
                        addMember.setText(textTmp)
                        addMember.setSelection(textTmp.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            builder.setTitle("添加成员")
                .setView(view)
                .setPositiveButton("确定"
                ) { _, _ ->
                    if (addMember.toString() != "") {
                        val member = Member(addMember.text.toString())
                        viewModel.addMember(member)
                        text = addMember.text.toString()
                    }
                    listener.onDialogPositiveClickForAddMember(this)
                }

                .setNegativeButton("取消"
                ) { _, _ ->
                    listener.onDialogNegativeClickForAddMember(this)
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