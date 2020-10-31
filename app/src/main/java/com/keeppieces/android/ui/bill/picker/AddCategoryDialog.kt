package com.keeppieces.android.ui.bill.picker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.keeppieces.android.R
import com.keeppieces.android.ui.bill.BillViewModel

//const val MaxCategoryLength = 4
class AddCategoryDialog(toPrimary: String, toSecondary: String) : DialogFragment()
//    AddPrimaryDialog.AddPrimaryDialogListener,
//    AddSecondaryDialog.AddSecondaryDialogListener
    {

    private lateinit var listener : AddCategoryDialogListener
    var textPrimary : String = toPrimary
    var textSecondary : String = toSecondary
    lateinit var viewModel : BillViewModel
    lateinit var addPrimaryCategory: CardView
    lateinit var addSecondaryCategory: CardView
    lateinit var addPrimary: TextView
    lateinit var addSecondary: TextView

    interface AddCategoryDialogListener {
        fun onDialogPositiveClickForAddCategory(dialog: DialogFragment)
        fun onDialogNegativeClickForAddCategory(dialog: DialogFragment)
//        fun onDialogClickForAddPrimaryCategory(dialog: DialogFragment)
//        fun onDialogClickForAddSecondaryCategory(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddCategoryDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement AddCategoryDialogListener"))
        }
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.bill_dialog_add_two, container,false)
//    }

//        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//            val dialog = super.onCreateDialog(savedInstanceState)
//            dialog.setTitle("添加类别")
//            dialog.setPosi
//            return dialog
//        }
//

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
//
//        addPrimaryBtn.setOnClickListener {
//            AddPrimaryDialog(textPrimary, this).show(requireActivity().supportFragmentManager, "AddPrimaryDialog")
//        }
//        addSecondaryBtn.setOnClickListener {
//            AddSecondaryDialog(textPrimary, textSecondary, this).show(requireActivity().supportFragmentManager, "AddSecondaryDialog")
//        }
////
////        val builder = AlertDialog.Builder(activity)
////        builder.setTitle("添加类别")
////            .setView(view)
////            .setPositiveButton("确定"
////            ) { _, _ ->
////                addPrimaryText.text = textPrimary
////                addSecondaryText.text = textSecondary
////                viewModel.addSecondary(SecondaryCategory(textSecondary, textPrimary))
////                listener.onDialogPositiveClickForAddCategory(this)
////            }
////            .setNegativeButton("取消"
////            ) { _, _ ->
////                listener.onDialogNegativeClickForAddCategory(this)
////            }
////        builder.create()
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.bill_dialog_add_two, null)

            addPrimaryCategory = view.findViewById(R.id.addPrimaryBtn)
            addSecondaryCategory = view.findViewById(R.id.addSecondaryBtn)
            addPrimary = view.findViewById(R.id.addPrimaryText)
            addSecondary = view.findViewById(R.id.addSecondaryText)
            val primaryDialog = AddPrimaryDialog(textPrimary, this)
            val secondaryDialog = AddSecondaryDialog(textPrimary, textSecondary, this)
            viewModel = ViewModelProvider(this)[BillViewModel::class.java]
            addPrimaryCategory.setOnClickListener {
                primaryDialog.show(requireActivity().supportFragmentManager, "AddPrimaryDialog")
//                if (primaryDialog.textPrimary != "") {
//                    val secondaryDialog = AddSecondaryDialog(textSecondary)
//                    secondaryDialog.show(requireActivity().supportFragmentManager, "AddSecondaryDialog")
//                    if (secondaryDialog.textSecondary != "") {
//                        textPrimary = primaryDialog.textPrimary
//                        textSecondary = secondaryDialog.textSecondary
//                        addPrimaryText.text = textPrimary
//                        addSecondaryText.text = textSecondary
//                    } else {
//                        Toast.makeText(activity, "二级分类输入为空", Toast.LENGTH_LONG).show()
//                    }
//                } else {
//                    Toast.makeText(activity, "一级分类输入为空", Toast.LENGTH_LONG).show()
//                }
            }

            addSecondaryCategory.setOnClickListener {
                secondaryDialog.show(requireActivity().supportFragmentManager, "AddSecondaryDialog")
//                if (secondaryDialog.textSecondary != "") {
//                    textSecondary = secondaryDialog.textSecondary
//                    addPrimaryText.text = textPrimary
//                    addSecondaryText.text = textSecondary
//                } else {
//                    Toast.makeText(activity, "二级分类输入为空", Toast.LENGTH_LONG).show()
//                }
            }

            builder.setTitle("添加类别")
                .setView(view)
                .setPositiveButton("确定"
                ) { _, _ ->
//                    addPrimary.text = textPrimary
//                    addSecondary.text = textSecondary
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

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        dialog?.addPrimary.text = textPrimary
//        addSecondary.text = textSecondary
//    }

//    override fun onDialogPositiveClickForAddPrimary(dialog: DialogFragment) {
////        textPrimary = (dialog as AddPrimaryDialog).text
////        Log.d("BillActivity", "1textPrimary$textPrimary")
////        addToPrimaryText.text = textPrimary
//    }
//
//    override fun onDialogNegativeClickForAddPrimary(dialog: DialogFragment) {
//    }
//
//    override fun onDialogPositiveClickForAddSecondary(dialog: DialogFragment) {
////        textSecondary = (dialog as AddSecondaryDialog).text
////        Log.d("BillActivity", "1textSecondary$textSecondary")
//    }
//
//    override fun onDialogNegativeClickForAddSecondary(dialog: DialogFragment) {
//    }
}