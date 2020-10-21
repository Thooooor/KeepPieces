package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.fragment_login_passsword.*
import java.util.concurrent.Executor

class LoginFragment:Fragment() {
    //private lateinit var promptInfo: BiometricPrompt.PromptInfo
    //private val TAG = "LoginActivity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_passsword, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }

    private fun init() {
        val loginActivity = activity as LoginActivity
        val promptInfo = createPromptInfo()

        loginWithPassword()

        val biometricPrompt: BiometricPrompt = createBiometricPrompt()
        val biometricLoginButton = loginActivity.findViewById<ImageView>(R.id.fingerprint)
        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }


    }

    private fun loginWithPassword(){
        val loginActivity = activity as LoginActivity
        var visible = false
        val pwd =  readPwd()
        passwordEdit.addTextChangedListener(
            object: TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString() == pwd){
                        Toast.makeText(loginActivity.applicationContext,
                            "登录成功",Toast.LENGTH_SHORT)
                            .show()
                        jump()
                    }//else{

//                        Toast.makeText(loginActivity.applicationContext,
//                            "密码错误",Toast.LENGTH_SHORT)
//                            .show()
//                   }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,start: Int,count: Int,after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            }
        )

        visibleLogo.setOnClickListener {

            if(visible){
                passwordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
                visible = false
            }else{
                passwordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
                visible = true
            }
        }

        changeToGesture.setOnClickListener {
            loginActivity.replaceFragment(GestureFragment())
        }
    }

    private fun readPwd(): String? {
        val loginActivity = activity as LoginActivity
        val prefs = loginActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        return prefs.getString("pwd","")
    }

    private fun createBiometricPrompt():BiometricPrompt{
        val loginActivity = activity as LoginActivity
        val executor = ContextCompat.getMainExecutor(loginActivity)
        val TAG = "LoginActivity"
        val callback = @RequiresApi(Build.VERSION_CODES.P)
        object :BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG,"$errorCode::$errString")
                Toast.makeText(loginActivity.applicationContext,
                    "Authentication error:$errString", Toast.LENGTH_SHORT)
                    .show()
                loginWithPassword()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG,"身法验证失败，原因未知")
                Toast.makeText(loginActivity.applicationContext,"Authentication failed",
                    Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG,"身份验证成功")
                Toast.makeText(loginActivity.applicationContext,
                    "Authentication succeeded!", Toast.LENGTH_SHORT)
                    .show()
                jump()
            }
        }
        return BiometricPrompt(this,executor,callback)
    }

    private fun createPromptInfo():BiometricPrompt.PromptInfo{
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_info_title))
            .setSubtitle(getString(R.string.prompt_info_subtitle))
            //.setDescription(getString(R.string.prompt_info_description))
            .setConfirmationRequired(false)
            .setNegativeButtonText(getString(R.string.prompt_info_use_app_password))
            .build()
    }

    //登录成功跳转
    private fun jump(){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }



}