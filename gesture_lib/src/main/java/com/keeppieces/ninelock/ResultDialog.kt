package com.keeppieces.ninelock

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.dialog_result.view.*

/**
 * 作者： 吴昶 .
 * 时间: 2018/12/11 9:51
 * 功能简介：
 */
class ResultDialog(context: Context):Dialog(context){

    private var fview: View?=null

    init {
        fview=LayoutInflater.from(context).inflate(R.layout.dialog_result,null)
        fview!!.btn_close.setOnClickListener { cancel() }
        this.setCanceledOnTouchOutside(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(fview!!)
    }

    fun setTitle(title:String){
        fview!!.tv_result.text=title
    }

}