package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import androidx.biometric.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var executor:Executor

    //private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        val promptInfo = createPromptInfo()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        executor = ContextCompat.getMainExecutor(this)

        loginWithPassword()

        biometricPrompt = createBiometricPrompt()
        val biometricLoginButton = findViewById<ImageView>(R.id.fingerprint)
        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }


    private fun loginWithPassword(){
        var visible = false
        val pwd =  readPwd()
        passwordEdit?.addTextChangedListener(
            object:TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    if(s.toString() == pwd){
                        jump()
                    }else{
                        Toast.makeText(applicationContext,
                            "密码错误",Toast.LENGTH_SHORT)
                            .show()
                    }
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
    }

    private fun readPwd(): String? {
        val prefs = getSharedPreferences("password",Context.MODE_PRIVATE)
        return prefs.getString("pwd","")
    }

    private fun createBiometricPrompt():BiometricPrompt{
        val executor = ContextCompat.getMainExecutor(this)

        val callback = @RequiresApi(Build.VERSION_CODES.P)
        object :BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG,"$errorCode::$errString")
                Toast.makeText(applicationContext,
                    "Authentication error:$errString",Toast.LENGTH_SHORT)
                    .show()
                loginWithPassword()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG,"身法验证失败，原因未知")
                Toast.makeText(applicationContext,"Authentication failed",
                    Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG,"身份验证成功")
                Toast.makeText(applicationContext,
                    "Authentication succeeded!",Toast.LENGTH_SHORT)
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
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}

/**
private fun EditText.addTextChangedListener(loginActivity: LoginActivity) {

}
*/
