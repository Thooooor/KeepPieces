package com.keeppieces.android.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import com.keeppieces.ninelock.NineLockListener
import com.keeppieces.ninelock.NineLockView
import kotlinx.android.synthetic.main.fragment_reset_gesture.*
import kotlinx.android.synthetic.main.fragment_set_gesture.*

class ResetGestureFragment : Fragment(), NineLockListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_gesture, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }


    private fun init() {


        gestureReset.setOnClickListener { gestureReset.invalidate() }
        gestureReset.setLockListener(this)

    }

    override fun onLockResult(result: IntArray?) {
        val resetActivity = activity as ResetActivity
        val stringBuffer = StringBuffer()
        for (i in result!!.indices) {
            stringBuffer.append(result[i])
            //print("${result[i]}  -> ")
        }
        val tempPwd = stringBuffer.toString()
        resetActivity.sendSetData(tempPwd)
        Toast.makeText(resetActivity, "请确认图案密码", Toast.LENGTH_SHORT).show()
        resetActivity.replaceFragment(ResetConfirmGestureFragment())

    }

    override fun onError() {
        println("请重新绘制图案")
    }


}