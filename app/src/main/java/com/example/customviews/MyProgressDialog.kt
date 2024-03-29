package com.example.customviews

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.example.demoproject.R

class MyProgressDialog(context: Context) : Dialog(context, R.style.ProgressDialog){

    init {
        val progressBar = ProgressBar(context)
        progressBar.indeterminateDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY)
        addContentView(progressBar, RelativeLayout.LayoutParams(90, 90))
    }

    fun show(title: CharSequence = "", cancelable: Boolean = false, cancelListener: DialogInterface.OnCancelListener? = null) {
        setTitle(title)
        setCancelable(cancelable)
        setOnCancelListener(cancelListener)
        show()
    }

    companion object {

        @JvmOverloads
        fun show(
            context: Context,
            title: CharSequence,
            cancelable: Boolean = false,
            cancelListener: DialogInterface.OnCancelListener? = null
        ): MyProgressDialog {
            val dialog = MyProgressDialog(context)
            dialog.setTitle(title)

            dialog.setCancelable(cancelable)
            dialog.setOnCancelListener(cancelListener)

            val progressBar = ProgressBar(context)
            progressBar.indeterminateDrawable.setColorFilter(
                Color.BLACK,
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            dialog.addContentView(progressBar, RelativeLayout.LayoutParams(90, 90))
            dialog.show()

            return dialog
        }
    }

}