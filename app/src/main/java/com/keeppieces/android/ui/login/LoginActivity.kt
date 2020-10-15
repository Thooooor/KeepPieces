package com.keeppieces.android.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.keeppieces.android.MainActivity
import com.keeppieces.android.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_top_tab.*
import kotlinx.android.synthetic.main.layout_top_tab.view.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var visible = false

        fingerprint.setOnClickListener {
            val password = passwordEdit.text.toString()
            val pwd =  readPwd()
            if(password == pwd){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val editor =getSharedPreferences("password", Context.MODE_PRIVATE).edit()
                editor.putString("pwd",password)
                editor.apply()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
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
}

