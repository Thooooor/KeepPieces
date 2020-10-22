package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.keeppieces.ninelock.NineLockListener
import kotlinx.android.synthetic.main.fragment_login_gesture.*
import kotlinx.android.synthetic.main.fragment_set_gesture.*

class GestureFragment:Fragment() ,NineLockListener{
    private lateinit var tempPwd :String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_gesture, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }
    private fun init(){

        val loginActivity = activity as LoginActivity
        val promptInfo = createPromptInfo()

        loginWithGesture()

        val biometricPrompt: BiometricPrompt = createBiometricPrompt()
        val biometricLoginButton = loginActivity.findViewById<ImageView>(R.id.fingerprintOfGesture)
        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }

    private fun loginWithGesture(){
        gestureLogin.setOnClickListener { gestureLogin.invalidate() }
        gestureLogin.setLockListener(this)
    }

    override fun onLockResult(result: IntArray?) {
        val loginActivity = activity as LoginActivity
        val stringBuffer=StringBuffer()
        for(i in result!!.indices){
            stringBuffer.append(result[i])
            //print("${result[i]}  -> ")
        }
        tempPwd = stringBuffer.toString()
        val gesture = getGesture()

        if(gesture == tempPwd){
            Toast.makeText(loginActivity.applicationContext,
                "登录成功", Toast.LENGTH_SHORT)
                .show()
            jump()
        }else{
            Toast.makeText(loginActivity.applicationContext,
                "图案错误", Toast.LENGTH_SHORT)
                .show()
            loginActivity.replaceFragment(GestureFragment())
        }
    }

    override fun onError() {
        println("请重新绘制图案")
    }

    private fun getGesture():String{
        val loginActivity = activity as LoginActivity
        val share = loginActivity.getSharedPreferences("password", Context.MODE_PRIVATE)
        val gesture = share.getString("gesture", "")
        return gesture.toString()
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
                loginWithGesture()
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

    private fun createPromptInfo(): BiometricPrompt.PromptInfo{
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_info_title))
            .setSubtitle(getString(R.string.prompt_info_subtitle))
            //.setDescription(getString(R.string.prompt_info_description))
            .setConfirmationRequired(false)
            .setNegativeButtonText(getString(R.string.prompt_info_use_app_gesture))
            .build()
    }

    //登录成功跳转
    private fun jump(){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}