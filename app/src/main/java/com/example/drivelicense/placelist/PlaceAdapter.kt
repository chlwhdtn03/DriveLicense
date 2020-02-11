package com.example.drivelicense.placelist

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drivelicense.R
import kotlinx.android.synthetic.main.main_card.view.*
import kotlinx.android.synthetic.main.placeitem.view.*

class PlaceAdapter(val context: Context, val list: ArrayList<Place>) :
    RecyclerView.Adapter<PlaceAdapter.Holder>() {


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.place_name
        val region = itemView.place_region

        fun bind(data: Place, context: Context) {
            name.text = data.name.replace(" ", "") + " 운전면허시험장"
            region.text = data.locate.split(" ")[0]
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.placeitem, null, false)
        return Holder(view)

    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(list[p1], context)
    }

}