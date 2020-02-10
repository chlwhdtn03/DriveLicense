package com.example.drivelicense

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recent_quiz.*
import java.util.*
import kotlin.collections.ArrayList

class RecentQuiz : AppCompatActivity() {

    var list: ArrayList<ScoreCard> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_quiz)

        var scorecard: ScoreCard
        var datas = getSharedPreferences("Recent", Context.MODE_PRIVATE)
        for(data in datas.all.toSortedMap()) {
            var value = (data.value as MutableSet<String>)
            var iterator = value.iterator()
            scorecard = ScoreCard(0, 0, 0, 0)
            while(iterator.hasNext()) {
                var now = iterator.next()
                if(now.startsWith("S")) {
                    scorecard.score = now.split("S ")[1].toInt()
                }

                if(now.startsWith("T")) {
                    scorecard.quiz_amount = now.split("T ")[1].toInt()
                }

                if(now.startsWith("C")) {
                    scorecard.correct_amount = now.split("C ")[1].toInt()
                }

                if (now.startsWith("B")) {
                    scorecard.gradient_id = now.split("B ")[1].toInt()
                }
            }
            if (scorecard.gradient_id == 0) {
                scorecard.gradient_id = when (Random().nextInt(4)) {
                    0 -> {
                        R.drawable.gradient_copper
                    }
                    1 -> {
                        R.drawable.gradient_mango
                    }
                    2 -> {
                        R.drawable.gradient_pink
                    }
                    3 -> {
                        R.drawable.gradient_moonlight
                    }
                    else -> {
                        R.drawable.gradient_copper
                    }

                }
            }
            list.add(scorecard)
//            list.add(ScoreCard(iterator.next().toInt(), iterator.next().toInt(),iterator.next().toInt(), 1))
        }


        list.reverse()
        val mAdapter = ScoreCardAdapter(this, list)
        recentlist.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        recentlist.layoutManager = lm;
        recentlist.setHasFixedSize(true)
    }


}
