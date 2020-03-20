package com.chlwhdtn.drivelicense.scorecard

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chlwhdtn.drivelicense.R
import kotlinx.android.synthetic.main.scorecard.view.*

class ScoreCardAdapter(val context:Context, val list: ArrayList<ScoreCard>) : RecyclerView.Adapter<ScoreCardAdapter.Holder>() {



    inner class Holder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val score = itemView.score
        val quiz_amount = itemView.quiz_amount
        val correct_amount = itemView.quiz_correct
        val quiz_tip = itemView.quiz_tip
        val background = itemView.cardbackground
        val date = itemView.date

        fun bind(data: ScoreCard, context: Context) {
            score.text = "${data.score} 점"
            if (data.score < +0) {
                data.score *= -1
                score.text = "${data.score} 점"
                score.setTextColor(Color.parseColor("#FF5555"))
            }
            quiz_amount.text = "${data.quiz_amount}문제 중"
            correct_amount.text = "${data.correct_amount}문제 정답"
            date.text = data.date
            quiz_tip.text =
                if (data.score >= 90) {
                    "무조건 합격하실 수 있을겁니다!"
                } else if (data.score >= 80) {
                    "1,2종 모두 합격을 예상합니다!"
                } else if (data.score >= 70) {
                    "나머지 10점은 운에 맡겨봅시다!"
                } else if (data.score >= 60) {
                    "2종 보통이라면 합격할 수도 있습니다.\n(강남운전면허연습장 한정)"
                } else if (data.score >= 50) {
                    "좀만 더 연습하세요"
                } else if (data.score >= 30) {
                    "이제 막 시험준비를 시작하셨네요"
                } else if (data.score >= 20) {
                    "대중교통 이용에는 많은 장점이 있습니다!"
                } else {
                    "심심풀이로 오셨군요!"
                }
//            background.setBackgroundResource(data.gradient_id) // 배경색
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.scorecard, p0, false)
        return Holder(view)

    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(list[p1], context)
    }

}