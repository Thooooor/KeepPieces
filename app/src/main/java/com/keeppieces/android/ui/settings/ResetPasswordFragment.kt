package com.keeppieces.android.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keeppieces.android.R
import com.keeppieces.android.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_set_password.*
import kotlinx.android.synthetic.main.fragment_set_password.confirm

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
        var visible2 = false
        var visible3 = false
        visibleResetLogo1.setOnClickListener {

            if (visible1) {
                oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                visible2 = false
            } else {
                oldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                visible1 = true
            }
        }

        visibleResetLogo2.setOnClickListener {

            if (visible2) {
                passwordFirstReset.transformationMethod = PasswordTransformationMethod.getInstance()
                visible2 = false
            } else {
                passwordFirstReset.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                visible2 = true
            }
        }

        visibleResetLogo3.setOnClickListener {

            if (visible3) {
                passwordSecondReset.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                visible3 = false
            } else {
                passwordSecondReset.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                visible3 = true
            }
        }

        val min = 6
        val max = 20
        confirm.setOnClickListener {
            val oldPwd = oldPassword.text.toString()
            val pwd = readPwd()
            val firstPwd = passwordFirstReset.text.toString()
            val secondPwd = passwordSecondEdit.text.toString()
            if (pwd != oldPwd) {
                Toast.makeText(resetActivity, "旧密码错误", Toast.LENGTH_SHORT).show()
            } else {
                if (firstPwd.length < min) {
                    Toast.makeText(resetActivity, "密码最少为6位", Toast.LENGTH_SHORT).show()
                } else if (firstPwd.length > max) {
                    Toast.makeText(resetActivity, "密码最多为20位", Toast.LENGTH_SHORT).show()
                } else {
                    if (firstPwd == secondPwd && firstPwd != "") {
                        Toast.makeText(resetActivity, "密码设置成功！", Toast.LENGTH_SHORT).show()
                        val editor =
                            resetActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
                                ?.edit()
                        editor?.putString("pwd", secondPwd)
                        editor?.apply()

                        jump()
                    } else {
                        Toast.makeText(resetActivity, "两次密码不一致", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun readPwd(): String {
        val resetActivity = activity as ResetActivity
        val prefs = resetActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val pwd = prefs.getString("pwd", "")
        return pwd.toString()
    }

    private fun jump() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}