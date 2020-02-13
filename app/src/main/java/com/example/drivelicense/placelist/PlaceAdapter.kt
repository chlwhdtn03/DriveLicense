package com.example.drivelicense.placelist

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v7.view.menu.MenuView
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
        val expandLayout = itemView.place_expandLayout
        val info = itemView.place_info

        fun bind(data: Place, context: Context) {
            name.text = if (data.isPersonalPlace) data.name + "운전면허 전문학원" else data.name.replace(
                " ",
                ""
            ) + " 운전면허시험장"
            var region_name = data.locate.split(" ")[0].run {
                if (contains("서울"))
                    "서울"
                else if (contains("경기"))
                    "경기"
                else if (contains("인천"))
                    "인천"
                else if (contains("울산"))
                    "울산"
                else if (contains("부산"))
                    "부산"
                else if (contains("대전"))
                    "대전"
                else if (contains("광주"))
                    "광주"
                else if (contains("제주"))
                    "제주"
                else if (contains("강원"))
                    "강원"
                else if (contains("대구"))
                    "대구"
                else if (contains("충청북도"))
                    "충북"
                else if (contains("충청남도"))
                    "충북"
                else if (contains("전라남도"))
                    "전남"
                else if (contains("전라북도"))
                    "전북"
                else if (contains("경상남도"))
                    "경남"
                else if (contains("경상북도"))
                    "경북"
                else
                    this
            }
            region.text = region_name
            region.setTextColor(Color.parseColor(if (data.isPersonalPlace) "#0000FF" else "#000000"))

            expandLayout.visibility = (if (data.isExpanded) View.VISIBLE else View.GONE)

            info.text = "주소. ${data.locate}\n"

            data.TEL?.run {
                info.text = "${info.text}TEL. ${toTELString(data.TEL!!)}"
            }

            itemView.setOnClickListener {
                data.isExpanded = !data.isExpanded
                notifyItemChanged(list.indexOf(data))
            }

        }

        private fun toTELString(tel: String): String {
            if (tel.length == 8) {
                return tel.replaceFirst("^([0-9]{4})([0-9]{4})$".toRegex(), "$1-$2");
            } else if (tel.length == 12) {
                return tel.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$".toRegex(), "$1-$2-$3");
            }
            return tel.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$".toRegex(), "$1-$2-$3");

        }

    }

    override fun getItemViewType(position: Int): Int { // 가이드는 1번, 일반 정보는 0번 타입
        return if (list[position].name.equals("GUIDE")) 1 else 0
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): Holder {
        var view: View
        if (viewType == 0)
            view = LayoutInflater.from(context).inflate(R.layout.placeitem, p0, false)
        else
            view = LayoutInflater.from(context).inflate(R.layout.placeguide, p0, false)
        return Holder(view)

    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        if (list[p1].name.equals("GUIDE") == false) {
            p0.bind(list[p1], context)


        }
    }

}