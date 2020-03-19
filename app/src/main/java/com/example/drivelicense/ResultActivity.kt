package com.example.drivelicense

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.res.ResourcesCompat
import android.text.Layout
import android.view.Gravity
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.itextpdf.text.Font
import kotlinx.android.synthetic.main.activity_result.*
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var point = 0;
        var quiz_amount = intent.getIntExtra("QuizAmount",0)
        var quiz_correct = intent.getIntExtra("Correct",0)
        var score = ((quiz_correct.toFloat() / quiz_amount.toFloat()) * 100).toInt()
        result_score.text = "${score} 점"
        point = score
        result_msg.text =
            if (score >= 90) {
                "무조건 합격하실 수 있을겁니다!"
            } else if (score >= 80) {
                "1,2종 모두 합격을 예상합니다!"
            } else if (score >= 70) {
                "나머지 10점은 운에 맡겨봅시다!"
            } else if (score >= 60) {
                "2종 보통이라면 합격할 수도 있습니다.\n(강남운전면허연습장 한정)"
            } else if (score >= 50) {
                "좀만 더 연습하세요"
            } else if (score >= 30) {
                "이제 막 시험준비를 시작하셨네요"
            } else if (score >= 20) {
                "대중교통 이용에는 많은 장점이 있습니다!"
            } else {
                point = 0
                "심심풀이로 오셨군요!"
            }
        result_point.text = "포인트 ${point} 획득!"
        result_detail.text = "${quiz_amount}문제 중 ${quiz_correct}문제 정답"

        var resultarr = intent.getIntegerArrayListExtra("ResultArray")

        for(cnt in 0 until resultarr.size) {
            var checkbox: CheckBox = CheckBox(this)
            checkbox.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            checkbox.buttonDrawable = getDrawable(R.drawable.scoring)
            checkbox.typeface = Typeface.create("font", Typeface.NORMAL)
            checkbox.text = "${cnt + 1}번 문제"
            checkbox.isChecked = (resultarr[cnt] == 1)
            checkbox.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
                var quiz_intent: Intent = Intent(this, QuizActivity::class.java)
                quiz_intent.putExtra("READ", 1)
                MainActivity.nowQuestionIndex = cnt
                startActivity(quiz_intent)
            }
            checkbox.setPadding(0, 0, 10, 0)
            checkbox.textSize = 10f
            checkbox.typeface = ResourcesCompat.getFont(applicationContext, R.font.font)
            result_grid.addView(checkbox)
        }

        var sf: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sf.edit().run {
            putInt(
                "HighScore",
                if (sf.getInt("HighScore", 0) < score) score else sf.getInt("HighScore", 0)
            )
            putInt("Point", sf.getInt("Point", 0) + point)
            apply()
        }

        sf = getSharedPreferences("Recent", Context.MODE_PRIVATE)

        sf.edit().run {
            var list = hashSetOf<String>(
                "C " + quiz_correct.toString(),
                "T " + quiz_amount.toString(),
                "S " + score.toString(),
                "B " +
                        when (Random().nextInt(4)) {
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

                        },
                "D " + SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(Date())
            )
            putStringSet(SimpleDateFormat("yyyyMMdd-HHmmss").format(Date()), list)
            apply()
        }



//        <!--                <CheckBox-->
//        <!--                    android:layout_width="wrap_content"-->
//        <!--                    android:layout_height="wrap_content"-->
//        <!--                    android:button="@drawable/scoring"-->
//        <!--                    android:fontFamily="@font/font"-->
//        <!--                    android:layout_gravity="fill"-->
//        <!--                    android:text="1번 문제"-->
//        <!--                    android:paddingRight="10dp"-->
//        <!--                    android:textSize="15sp" />-->
//        <!--                -->
//        <!--                -->

    }
}
