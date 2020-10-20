package com.keeppieces.android.ui.login


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R
import com.keeppieces.ninelock.NineLockListener

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        replaceFragment(LoginFragment())
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager //获取FragmentManager
        val transaction = fragmentManager.beginTransaction() //开启一个事务
        transaction.replace(R.id.loginPage,fragment)  //替换容器内的fragment
        transaction.addToBackStack(null)    //返回栈,实现按下back键返回上一个fragment
        transaction.commit()    //提交事务
    }

}
