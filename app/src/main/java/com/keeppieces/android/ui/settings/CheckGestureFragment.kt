package com.keeppieces.android.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import com.keeppieces.android.ui.login.LoginActivity
import com.keeppieces.ninelock.NineLockListener
import kotlinx.android.synthetic.main.fragment_check_gesture.*

class CheckGestureFragment : Fragment(), NineLockListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_gesture, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }

    private fun init() {
        //val firstActivity = activity as FirstActivity

        gestureCheck.setOnClickListener { gestureCheck.invalidate() }
        gestureCheck.setLockListener(this)

    }

    override fun onLockResult(result: IntArray?) {
        val resetActivity = activity as ResetActivity
        val pwd = getGesture()
        val stringBuffer = StringBuffer()
        for (i in result!!.indices) {
            stringBuffer.append(result[i])
            //print("${result[i]}  -> ")
        }
        val tempPwd = stringBuffer.toString()
        if (tempPwd != pwd) {
            Toast.makeText(
                resetActivity.applicationContext,
                "图案错误", Toast.LENGTH_SHORT
            )
                .show()
            resetActivity.addFragment(CheckGestureFragment())
        } else {
            Toast.makeText(resetActivity, "验证成功！", Toast.LENGTH_SHORT).show()
            resetActivity.addFragment(ResetFragment())
        }


    }

    private fun getGesture(): String {
        val resetActivity = activity as ResetActivity
        val share = resetActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val gesture = share.getString("gesture", "")
        return gesture.toString()
    }


    override fun onError() {
        println("请重新绘制图案")
    }
}