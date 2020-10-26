package com.keeppieces.android.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.fragment_reset.*


class ResetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }

    private fun init() {
        val resetActivity = activity as ResetActivity
        //获取原有密码
        val share = resetActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val pwd = share.getString("pwd", "")
        val gesture = share.getString("gesture", "")
        passwordResetSelect.setOnClickListener {
            if (pwd != "") {
                resetActivity.replaceFragment(ResetPasswordFragment())
            } else {
                resetActivity.replaceFragment(SetNewPasswordFragment())
            }
        }
        gestureResetSelect.setOnClickListener {
            if (gesture != "") {
                resetActivity.replaceFragment(CheckGestureFragment())
            } else {
                resetActivity.replaceFragment(ResetGestureFragment())
            }
        }
    }

}