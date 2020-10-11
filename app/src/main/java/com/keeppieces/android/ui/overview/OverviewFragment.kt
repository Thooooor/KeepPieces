package com.keeppieces.android.ui.overview

import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.keeppieces.android.R


class OverviewFragment : Fragment() {

    private val content by lazy {
        requireView().findViewById<ViewGroup>(R.id.content)
    }

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) runEnterAnimation()
    }

    private fun runEnterAnimation() {
        content.post {
            var duration = 300L
            content.children.filterNot { it is Guideline }
                .forEach { child ->
                    duration += 100
                    child.translationY += 400
                    child.alpha = 0f
                    child.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(duration)
                        .setInterpolator(DecelerateInterpolator())
                        .setListener(object : AnimatorListenerAdapter() {
                        })
                        .start()
                }
        }
    }
}
