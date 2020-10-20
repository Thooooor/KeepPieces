package com.keeppieces.ninelock

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class GestureActivity : AppCompatActivity()/* ,NineLockListener*/{
/**   private var count = 0
    //private var resultDialog:ResultDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nine.setOnClickListener { nine.invalidate() }
        nine.setLockListener(this)

        //resultDialog= ResultDialog(this)
    }

    override fun onLockResult(result: IntArray?) {
        val stringBuffer=StringBuffer()
        for(i in result!!.indices){
            stringBuffer.append(result[i])
            print("${result[i]}  -> ")
        }
        val tempPwd = stringBuffer.toString()
//        count++
//        val tempGesture = getGesture()
//
//        if(tempGesture == "" ){
//            setGesture(tempPwd,count)
//        }else if (tempGesture == tempPwd){
//            setGesture(tempPwd,count)
//        }

        //resultDialog!!.setTitle(stringBuffer.toString())
        //println()
        //resultDialog!!.show()
    }

    override fun onError() {
        println("请重新绘制图案")
    }

    private fun setGesture(tempPwd:String,int: Int){
        val editor = getSharedPreferences("password", Context.MODE_PRIVATE)
            .edit()
        editor.putString("gesture",tempPwd)
        editor.putInt("check",int)
        editor.apply()
    }

    private fun getGesture():String{
        val share = getSharedPreferences("password", Context.MODE_PRIVATE)
        val gesture = share.getString("gesture", "")
        return gesture.toString()
    }*/
}
