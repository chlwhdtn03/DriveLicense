package com.example.drivelicense.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.drivelicense.R
import kotlinx.android.synthetic.main.select_car.view.*
import kotlinx.android.synthetic.main.select_gear.view.*

class LicenseFindDialog {
    var context : Context? = null
    var dialog: AlertDialog? = null

    constructor(context: Context) {
        this.context = context
        makeDialog()
    }

    private fun makeDialog() {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view: View = inflater.inflate(R.layout.select_car, null)
        var selectgear: View = inflater.inflate(R.layout.select_gear, null)
        selectgear.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        view.select_car_Car.setOnClickListener {
            view.select_car_down.removeAllViews()
            view.select_car_up.removeView(view.select_car_Bus)

            selectgear.select_gear_auto.setOnClickListener { // 승용합차
                view.select_car_title.text = "2종 보통(자동)을 추천합니다"
            }
            selectgear.select_gear_semi.setOnClickListener {
                view.select_car_title.text = "1종 보통을 추천합니다"
            }

            view.select_car_down.addView(selectgear)
            view.select_car_title.text = "차량 변속기를 선택하세요"
        }

        view.select_car_Bus.setOnClickListener { // 버스
            view.select_car_down.removeAllViews()
            view.select_car_up.removeView(view.select_car_Car)

            selectgear.select_gear_auto.setOnClickListener {
                view.select_car_title.text = "1종 대형이 필요합니다"
            }
            selectgear.select_gear_semi.setOnClickListener {
                view.select_car_title.text = "1종 대형이 필요합니다"
            }

            view.select_car_down.addView(selectgear)
            view.select_car_title.text = "차량 변속기를 선택하세요"
        }

        view.select_car_Truck.setOnClickListener {
            view.select_car_up.removeAllViews()
            view.select_car_down.removeView(view.select_car_Motor)

            selectgear.select_gear_auto.setOnClickListener {
                view.select_car_title.text = "1종 보통이 필요합니다"
            }
            selectgear.select_gear_semi.setOnClickListener {
                view.select_car_title.text = "1종 보통이 필요합니다"
            }

            view.select_car_up.addView(selectgear)
            view.select_car_title.text = "차량 변속기를 선택하세요"
        }

        view.select_car_Motor.setOnClickListener {
            view.select_car_up.removeAllViews()
            view.select_car_down.removeView(view.select_car_Truck)

            selectgear.select_gear_auto.setOnClickListener {
                view.select_car_title.text = "2종 소형이 필요합니다"
            }
            selectgear.select_gear_semi.setOnClickListener {
                view.select_car_title.text = "2종 소형이 필요합니다"
            }

            view.select_car_up.addView(selectgear)
            view.select_car_title.text = "차량 변속기를 선택하세요"
        }

        dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()
    }

    fun show() {
        dialog!!.show()
        val window = dialog!!.window
        if (window != null) {
            dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun close() {
        if(dialog!!.isShowing)
            dialog!!.dismiss()
    }
}