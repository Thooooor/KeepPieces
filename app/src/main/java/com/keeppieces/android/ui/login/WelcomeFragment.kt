package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }

    private fun init() {
        val firstActivity = activity as FirstActivity
        //获取登录状态，判断是否有密码
        val share = firstActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val pwd = share.getString("pwd", "")
        val gesture = share.getString("gesture", "")
        //第一次登录
        if (pwd == "" && gesture == "") {
            passwordSelect?.setOnClickListener {
                firstActivity.replaceFragment(SetPasswordFragment())
            }
            gestureSelect?.setOnClickListener {
                firstActivity.replaceFragment(SetGestureFragment())
            }
        } else {
            login()
        }
    }

    //切换到登录界面
    private fun login() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}