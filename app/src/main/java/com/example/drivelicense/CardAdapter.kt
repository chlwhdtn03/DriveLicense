package com.example.drivelicense

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_card.view.*

class CardAdapter(list: ArrayList<Card>, context: Context) : PagerAdapter() {

    var list:ArrayList<Card> = list
    var mContext : Context = context


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return (view == obj);
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater: LayoutInflater = LayoutInflater.from(mContext)
        var view: View = inflater.inflate(R.layout.main_card, null)

        view.card_title.text = list.get(position).title
        view.card.setCardBackgroundColor(Color.parseColor(list.get(position).color))
        view.card.setOnClickListener(list.get(position).onClick)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if(`object` is View)
            container.removeView(`object`)
    }


    override fun getCount(): Int = list.size

}