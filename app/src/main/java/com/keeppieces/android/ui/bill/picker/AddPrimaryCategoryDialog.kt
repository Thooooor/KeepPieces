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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.ui.bill.BillViewModel

const val MaxPrimaryLength = 4
class AddPrimaryDialog(toSecondary: String, inDialogBefore: DialogFragment) : DialogFragment() {

    private lateinit var listener : AddPrimaryDialogListener
    private lateinit var viewModel : BillViewModel
    var textSecondary = toSecondary
    var dialogBefore = inDialogBefore as AddCategoryDialog

    interface AddPrimaryDialogListener {
        fun onDialogPositiveClickForAddPrimary(dialog: DialogFragment)
        fun onDialogNegativeClickForAddPrimary(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddPrimaryDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement AddPrimaryDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.bill_dialog_add_single, null)
            val addPrimary: EditText = view.findViewById(R.id.itemAdd)
            val imm : InputMethodManager = addPrimary.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            viewModel = ViewModelProvider(this)[BillViewModel::class.java]

            addPrimary.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textTmp: String
                    if (s != null && s.toString().length > MaxPrimaryLength) {
                        textTmp = s.subSequence(0, MaxPrimaryLength).toString()
                        addPrimary.setText(textTmp)
                        addPrimary.setSelection(textTmp.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            builder.setTitle("添加一级分类")
                .setView(view)
                .setPositiveButton(
                    "确定"
                ) { _, _ ->
                    if (addPrimary.text.toString() != "") {
                        val secondaryDialog = AddSecondaryDialog(addPrimary.text.toString(), textSecondary, dialogBefore)
                        secondaryDialog.show(requireActivity().supportFragmentManager, "AddSecondaryDialog")
//                        textPrimary = secondaryDialog.textPrimary
//                        textSecondary = secondaryDialog.textSecondary
//                        if (secondaryDialog.textSecondary != "") {
//                            textPrimary = primaryDialog.textPrimary
//                            textSecondary = secondaryDialog.textSecondary
//                            addPrimaryText.text = textPrimary
//                            addSecondaryText.text = textSecondary
//                        } else {
//                            Toast.makeText(activity, "二级分类输入为空", Toast.LENGTH_LONG).show()
//                        }
                    } else {
                        Toast.makeText(activity, "一级分类输入为空", Toast.LENGTH_LONG).show()
                    }
                    imm.hideSoftInputFromWindow(addPrimary.applicationWindowToken, 0)
                    listener.onDialogPositiveClickForAddPrimary(this)
                }

                .setNegativeButton("取消"
                ) { _, _ ->
                    imm.hideSoftInputFromWindow(addPrimary.applicationWindowToken, 0)
                    listener.onDialogNegativeClickForAddPrimary(this)
                }
            builder.create()
        } ?:throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawableResource(R.color.color_status_bar)
    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                val view = currentFocus
//                KeyboardUtils.hideKeyboard(ev, view, this)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    object KeyboardUtils {
//        fun hideKeyboard(event: MotionEvent, view: View?, activity: Activity) {
//            try {
//                if (view != null && view is EditText) {
//                    val location = intArrayOf(0, 0)
//                    view.getLocationInWindow(location)
//                    val left = location[0]
//                    val top = location[1]
//                    val right = (left
//                            + view.getWidth())
//                    val bottom = top + view.getHeight()
//                    // （判断是不是EditText获得焦点）判断焦点位置坐标是否在控件所在区域内，如果位置在控件区域外，则隐藏键盘
//                    if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bottom) {
//                        // 隐藏键盘
//                        val token = view.getWindowToken()
//                        val inputMethodManager =
//                            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                        inputMethodManager.hideSoftInputFromWindow(
//                            token,
//                            InputMethodManager.HIDE_NOT_ALWAYS
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
}