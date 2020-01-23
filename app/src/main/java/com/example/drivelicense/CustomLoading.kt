package com.example.drivelicense

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.custom_loading.view.*


class CustomLoading {
    var context : Context? = null
    var msg : String? = null
    var dialog: AlertDialog? = null
    var progress: ProgressBar? = null

    constructor(context: Context, msg: String) {
        this.context = context
        this.msg = msg
        makeLoading()
    }

    private fun makeLoading() {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view: View = inflater.inflate(R.layout.custom_loading, null)

        if(msg != null) {
            if(msg!!.isNotEmpty()) {
                view.loading_msg.text = msg
            }
        }

        progress = view.loading_progress

        dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()
    }

    fun show() {
        dialog!!.show()
        val window = dialog!!.window
        if (window != null) {
            dialog!!.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        }
    }

    fun close() {
        if(dialog!!.isShowing)
            dialog!!.dismiss()
    }
}