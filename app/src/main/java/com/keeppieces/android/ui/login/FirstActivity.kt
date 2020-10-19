package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        replaceFragment(WelcomeFragment())
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager //获取FragmentManager
        val transaction = fragmentManager.beginTransaction() //开启一个事务
        transaction.replace(R.id.welcomePage,fragment)  //替换容器内的fragment
        transaction.addToBackStack(null)    //返回栈,实现按下back键返回上一个fragment
        transaction.commit()    //提交事务
    }

    /**
    //切换到登录界面
    private fun login(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
     */
}

