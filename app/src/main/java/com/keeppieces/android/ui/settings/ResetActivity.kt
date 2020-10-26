package com.keeppieces.android.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R

class ResetActivity : AppCompatActivity() {

    private lateinit var setData: String
    private lateinit var confirmData: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        addFragment(ResetFragment())

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendSetData(data: String) {
        setData = data
    }

    fun sendConfirmData(data: String) {
        confirmData = data
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager //获取FragmentManager
        val transaction = fragmentManager.beginTransaction() //开启一个事务
        transaction.replace(R.id.resetPage, fragment)  //替换容器内的fragment
        transaction.commit()    //提交事务
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager //获取FragmentManager
        val transaction = fragmentManager.beginTransaction() //开启一个事务
        transaction.replace(R.id.resetPage, fragment)  //替换容器内的fragment
        transaction.addToBackStack(null)    //返回栈,实现按下back键返回上一个fragment
        transaction.commit()    //提交事务
    }

    //对比密码
    fun compare() {
        if (setData == confirmData) {
            Toast.makeText(
                applicationContext,
                "密码修改成功！", Toast.LENGTH_SHORT
            )
                .show()
            val editor = getSharedPreferences("password", Context.MODE_PRIVATE)
                .edit()
            editor.putString("gesture", confirmData)
            editor.apply()
            jump()
        } else {
            Toast.makeText(
                applicationContext,
                "两次图案不一致！", Toast.LENGTH_SHORT
            )
                .show()
            replaceFragment(ResetConfirmGestureFragment())
        }
    }

    private fun jump() {
        finish()
    }


}