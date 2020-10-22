package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R
import com.keeppieces.ninelock.NineLockListener
import kotlinx.android.synthetic.main.fragment_login_gesture.*
import kotlinx.android.synthetic.main.fragment_set_gesture.*

class GestureFragment:Fragment() ,NineLockListener{
    private lateinit var tempPwd :String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_gesture, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }
    private fun init(){

        gestureLogin.setOnClickListener { gestureLogin.invalidate() }
        gestureLogin.setLockListener(this)

    }

    override fun onLockResult(result: IntArray?) {
        val loginActivity = activity as LoginActivity
        val stringBuffer=StringBuffer()
        for(i in result!!.indices){
            stringBuffer.append(result[i])
            //print("${result[i]}  -> ")
        }
        tempPwd = stringBuffer.toString()
        val gesture = getGesture()

        if(gesture == tempPwd){
            Toast.makeText(loginActivity.applicationContext,
                "登录成功", Toast.LENGTH_SHORT)
                .show()
            jump()
        }else{
            Toast.makeText(loginActivity.applicationContext,
                "图案错误", Toast.LENGTH_SHORT)
                .show()
            loginActivity.replaceFragment(GestureFragment())
        }
        //resultDialog!!.setTitle(stringBuffer.toString())
        //println()
        //resultDialog!!.show()

    }

    override fun onError() {
        println("请重新绘制图案")
    }

    private fun getGesture():String{
        val loginActivity = activity as LoginActivity
        val share = loginActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val gesture = share.getString("gesture", "")
        return gesture.toString()
    }

    //登录成功跳转
    private fun jump(){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}