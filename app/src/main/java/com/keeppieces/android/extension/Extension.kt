package com.keeppieces.android.extension

import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


fun ViewGroup.inflate(@LayoutRes res: Int): View {
    return LayoutInflater.from(context)
        .inflate(res, this, false)
}

fun RecyclerView.getItemDecoration() =
    DividerItemDecoration(this.context, RecyclerView.VERTICAL).apply {
        val divider =
            ContextCompat.getDrawable(this@getItemDecoration.context, R.drawable.divider)!!
        val margin = resources.getDimensionPixelSize(R.dimen.spacing_medium)
        setDrawable(InsetDrawable(divider, margin, 0, margin, 0))
    }


fun Float.toMoneyFormatted(removeSuffix: Boolean = false): String {
    return DecimalFormat("###,###,##0.00").format(this).apply {
        if (removeSuffix) {
            return this.removeSuffix(".00")
        }
    }
}

fun Float.toUSDFormatted(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
}