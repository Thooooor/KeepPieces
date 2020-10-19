package com.keeppieces.android.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keeppieces.android.R

class SetGestureFragment :Fragment(){
    private var mGestureLockViewGroup: GestureLockViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_gesture, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mGestureLockViewGroup = activity?.findViewById<View>(R.id.id_gestureLockViewGroup) as GestureLockViewGroup?
        mGestureLockViewGroup!!.setAnswer(intArrayOf(1, 2, 3, 4, 5)) //设置解锁密码
        mGestureLockViewGroup!!
            .setOnGestureLockViewListener(object : GestureLockViewGroup.OnGestureLockViewListener {

                override fun onUnmatchedExceedBoundary() {
                    Log.i("test","错误5次...")
                    mGestureLockViewGroup!!.setUnMatchExceedBoundary(5)
                }

                override fun onGestureEvent(mChoose:List<Int>) {
                    Log.i("test", "密码$mChoose")
//                        mGestureLockViewGroup!!.checkAnswer()//检查解锁密码
                    mGestureLockViewGroup!!.reset()// 重置
                }

                override fun onBlockSelected(cId: Int) {
//                        Log.i("test","点击了"+cId)
                }
            })
    }

}