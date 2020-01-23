package com.example.drivelicense

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.custom_toast.view.*

object CustomToast {

    fun makeToast(context: Context, msg: String) {

        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view: View = inflater.inflate(R.layout.custom_toast, null)

        view.toast_text.text = msg

        var toast: Toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = view
        toast.show()

    }

}