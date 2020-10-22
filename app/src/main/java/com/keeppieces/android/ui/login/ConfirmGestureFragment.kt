package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import com.keeppieces.ninelock.GestureActivity
import com.keeppieces.ninelock.NineLockListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_confirm_gesture.*
import kotlinx.android.synthetic.main.fragment_set_gesture.*

class ConfirmGestureFragment:Fragment(), NineLockListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_gesture, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }
    private fun init() {
        //val firstActivity = activity as FirstActivity

        gestureConfirm.setOnClickListener { gestureConfirm.invalidate() }
        gestureConfirm.setLockListener(this)

    }

    override fun onLockResult(result: IntArray?) {
        val firstActivity = context as FirstActivity
        val stringBuffer=StringBuffer()
        for(i in result!!.indices){
            stringBuffer.append(result[i])
            //print("${result[i]}  -> ")
        }
        val tempPwd = stringBuffer.toString()
        firstActivity.sendConfirmData(tempPwd)
        firstActivity.compare()

        //resultDialog!!.setTitle(stringBuffer.toString())
        //println()
        //resultDialog!!.show()

    }

    override fun onError() {
        println("请重新绘制图案")
    }

}