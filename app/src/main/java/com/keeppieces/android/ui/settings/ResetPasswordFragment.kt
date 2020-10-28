package com.keeppieces.android.ui.settings

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()

    }

    private fun init() {
        val resetActivity = activity as ResetActivity
        var visible1 = false
        val pwd = getPwd()
        visibleResetLogo1.setOnClickListener {

            if (visible1) {
                oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                visible1 = false
            } else {
                oldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                visible1 = true
            }
        }

        confirmReset.setOnClickListener {
            val tempPwd = oldPassword.text.toString()
            if (tempPwd == pwd) {
                Toast.makeText(resetActivity, "验证成功！", Toast.LENGTH_SHORT).show()
                resetActivity.addFragment(ResetFragment())
            } else {
                Toast.makeText(resetActivity, "密码错误！", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getPwd(): String {
        val resetActivity = activity as ResetActivity
        val prefs = resetActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val pwd = prefs.getString("pwd", "")
        return pwd.toString()
    }

}