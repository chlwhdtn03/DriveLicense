package com.example.drivelicense

import android.content.Context
import android.content.res.TypedArray
import android.text.Layout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.iconmenu.view.*


class CustomIconMenu : LinearLayout {

    private lateinit var tv: TextView
    private lateinit var ImgView: ImageView

    constructor(context : Context) : super(context) {
        initView()
    }

    constructor(context : Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
        getAttrs(attrs)
    }

    constructor(context : Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
        getAttrs(attrs, defStyle)
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.iconmenu, this, false)

        addView(view)
        tv = view.text
        ImgView = view.icon

    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomIconMenu)
        setTypeArray(typedArray)
    }

    // 만들어 놓은 attrs을 참조합니다.
    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomIconMenu, defStyleAttr, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {

        val text = typedArray.getString(R.styleable.CustomIconMenu_text)
        val textcolor = typedArray.getColor(R.styleable.CustomIconMenu_textColor, 0)
        val img = typedArray.getResourceId(R.styleable.CustomIconMenu_image, R.drawable.recent)



        setIcon(img)
        setText(text)
        setTextColor(textcolor)
        typedArray.recycle()

    }

    fun setIcon(symbol_resID: Int) {
        ImgView.setImageResource(symbol_resID)
    }

    fun setTextColor(color: Int) {
        tv.setTextColor(color)
    }

    fun setText(text_string: String?) {
        tv.text = text_string
    }

    fun setText(text_resID: Int) {
        tv.setText(text_resID)
    }


}