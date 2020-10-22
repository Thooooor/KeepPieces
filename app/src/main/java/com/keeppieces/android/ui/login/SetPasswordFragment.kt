package com.keeppieces.android.ui.login

import android.app.VoiceInteractor.Prompt
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        super.onActivityCreated(savedInstanceState)

        init()

    }
    private fun init(){
        val firstActivity = activity as FirstActivity
        var visible1 = false
        var visible2 = false
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

        val min = 4
        val max = 20
        confirm.setOnClickListener{
            val firstPwd = passwordFirstEdit.text.toString()
            val secondPwd = passwordSecondEdit.text.toString()
            if (firstPwd.length <min){
                Toast.makeText(activity,"密码最少为6位",Toast.LENGTH_SHORT).show()
            }else if (firstPwd.length >max){
                Toast.makeText(activity,"密码最多为20位",Toast.LENGTH_SHORT).show()
            }else{
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
        /**
        passwordFirstEdit.addTextChangedListener(
            object: TextWatcher {

                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,start: Int,count: Int,after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(start >= 0){

                        val num = s.toString().toInt()
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
                            Toast.makeText(firstActivity.applicationContext,
                                "密码最长为20位",Toast.LENGTH_SHORT).show()
                        }else if(num < min){
                            Toast.makeText(firstActivity.applicationContext,
                                    "密码最短为6位",Toast.LENGTH_SHORT)
                                    .show()
                        }else{
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

                    }
                }

            }
        )*/


    }

    private fun jump(){
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}