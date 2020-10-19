package com.keeppieces.android.ui.login

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
import kotlinx.android.synthetic.main.fragment_set_password.*

class SetPasswordFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        var visible1 = false
        var visible2 = false
        super.onActivityCreated(savedInstanceState)
        visibleLogo1.setOnClickListener {

            if(visible1){
                passwordFirstEdit.transformationMethod = PasswordTransformationMethod.getInstance()
                visible1 = false
            }else{
                passwordFirstEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
                visible1 = true
            }
        }

        visibleLogo2.setOnClickListener {

            if(visible2){
                passwordSecondEdit.transformationMethod = PasswordTransformationMethod.getInstance()
                visible2 = false
            }else{
                passwordSecondEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
                visible2 = true
            }
        }

        confirm.setOnClickListener{
            val firstPwd = passwordFirstEdit.text.toString()
            val secondPwd = passwordSecondEdit.text.toString()
            if(firstPwd == secondPwd && firstPwd !=""){
                Toast.makeText(activity,"密码设置成功！",Toast.LENGTH_SHORT).show()
                val editor = activity?.getSharedPreferences("password", Context.MODE_PRIVATE)
                    ?.edit()
                editor?.putString("pwd",secondPwd)
                editor?.apply()

                jump()
            }else{
                Toast.makeText(activity,"两次密码不一致",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun jump(){
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}