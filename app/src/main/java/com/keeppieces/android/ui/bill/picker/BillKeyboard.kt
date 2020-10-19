package com.keeppieces.android.ui.bill.picker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.keeppieces.android.R
import java.lang.reflect.Method
import java.util.*

class KeyboardPopupWindow(
    private var context: Context?,
    private var anchorView: View?,
    private var editText: EditText?,
    isRandomSort: Boolean
) :
    PopupWindow() {
    private var parentView: View? = null
    private var isRandomSort = false //数字是否随机排序
    private var list: MutableList<Int>? = ArrayList()
    private val commonButtonIds = intArrayOf(
        R.id.button00, R.id.button01, R.id.button02, R.id.button03,
        R.id.button04, R.id.button05, R.id.button06, R.id.button07, R.id.button08, R.id.button09
    )

    private fun initConfig() {
        isOutsideTouchable = false
        isFocusable = false
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        forbidDefaultSoftKeyboard()
    }

     //禁止系统默认的软键盘弹出
    private fun forbidDefaultSoftKeyboard() {
        if (editText == null) {
            return
        }
         try {
             val cls = EditText::class.java
             val setShowSoftInputOnFocus: Method
             setShowSoftInputOnFocus =
                 cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
             setShowSoftInputOnFocus.isAccessible = true
             setShowSoftInputOnFocus.invoke(editText, false)
         } catch (e: Exception) {
             e.printStackTrace()
         }
     }

    //刷新自定义的键盘是否outside可触摸反应：如果是不可触摸的，则显示该软键盘view
    fun refreshKeyboardOutSideTouchable(isTouchable: Boolean) {
        isOutsideTouchable = isTouchable
        if (!isTouchable) {
            Log.d(TAG, "执行show")
            show()
        } else {
            Log.d(TAG, "执行dismiss")
            dismiss()
        }
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        parentView = LayoutInflater.from(context).inflate(R.layout.bill_keyboard, null)
        initKeyboardView(parentView)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        contentView = parentView
    }

    @SuppressLint("SetTextI18n")
    private fun initKeyboardView(view: View?) {
        val dropdownLl = view!!.findViewById<LinearLayout>(R.id.dropdownLl)
        dropdownLl.setOnClickListener { dismiss() }

        //①给数字键设置点击监听
        for (i in commonButtonIds.indices) {
            val button = view.findViewById<Button>(commonButtonIds[i])
            button.setOnClickListener {
                val curSelection = editText!!.selectionStart
                val length = editText!!.text.toString().length
                if (curSelection < length) {
                    val content = editText!!.text.toString()
                    editText!!.setText(
                        content.substring(
                            0,
                            curSelection
                        ) + button.text + content.subSequence(curSelection, length)
                    )
                    editText!!.setSelection(curSelection + 1)
                } else {
                    editText!!.setText(editText!!.text.toString() + button.text)
                    editText!!.setSelection(editText!!.text.toString().length)
                }
            }
        }

        //②给小数点按键设置点击监听
        view.findViewById<View>(R.id.buttonDot).setOnClickListener {
            val curSelection = editText!!.selectionStart
            val length = editText!!.text.toString().length
            if (curSelection < length) {
                val content = editText!!.text.toString()
                editText!!.setText(
                    content.substring(0, curSelection) + "." + content.subSequence(
                        curSelection,
                        length
                    )
                )
                editText!!.setSelection(curSelection + 1)
            } else {
                editText!!.setText(editText!!.text.toString() + ".")
                editText!!.setSelection(editText!!.text.toString().length)
            }
        }

        //③给叉按键设置点击监听
        view.findViewById<View>(R.id.buttonCross).setOnClickListener {
            val length = editText!!.text.toString().length
            val curSelection = editText!!.selectionStart
            if (length > 0 && curSelection > 0 && curSelection <= length) {
                val content = editText!!.text.toString()
                editText!!.setText(
                    content.substring(0, curSelection - 1) + content.subSequence(
                        curSelection,
                        length
                    )
                )
                editText!!.setSelection(curSelection - 1)
            }
        }
    }

    fun show() {
        if (!isShowing && anchorView != null) {
//            doRandomSortOp()
            showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
        }
    }

     //随机分布数字
    @SuppressLint("SetTextI18n")
    private fun doRandomSortOp() {
        if (parentView == null) {
            return
        }
        if (!isRandomSort) {
            for (i in commonButtonIds.indices) {
                val button = parentView!!.findViewById<Button>(commonButtonIds[i])
                button.text = "" + i
            }
        } else {
            list!!.clear()
            val ran = Random()
            while (list!!.size < commonButtonIds.size) {
                val n = ran.nextInt(commonButtonIds.size)
                if (!list!!.contains(n)) list!!.add(n) //如果n不包涵在list中，才添加
            }
            for (i in commonButtonIds.indices) {
                val button = parentView!!.findViewById<Button>(commonButtonIds[i])
                button.text = "" + list!![i]
            }
        }
    }

    fun refreshViewAndShow(context: Context?, anchorView: View?, editText: EditText?) {
        this.context = context
        this.anchorView = anchorView
        this.editText = editText
        if (context == null || anchorView == null) {
            return
        }
        show()
    }

    fun releaseResources() {
        dismiss()
        context = null
        anchorView = null
        if (list != null) {
            list!!.clear()
            list = null
        }
    }

    companion object {
        private const val TAG = "KeyboardPopupWindow"
    }

    init {
        this.isRandomSort = isRandomSort
        initConfig()
        initView()
    }
}