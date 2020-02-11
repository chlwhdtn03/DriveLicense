package com.example.drivelicense

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.drivelicense.custom.CustomToast
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlin.collections.ArrayList

class QuizActivity : AppCompatActivity() {


     var isReadOnly:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        progress_quiz.max = MainActivity.QuestionList.size - 1

        isReadOnly = intent.getIntExtra("READ", 0) == 1
        if(!isReadOnly) {
            previous_quiz.setOnClickListener {
                if(MainActivity.nowQuestionIndex - 1 < 0)
                    MainActivity.run {
                        CustomToast.makeToast(applicationContext, "더 이상 뒤로갈 수 없습니다")
                    }
                    else
                    MainActivity.run {
                        initScreen(QuestionList.get(nowQuestionIndex - 1))
                        nowQuestionIndex -= 1
                        progress_quiz.progress = nowQuestionIndex
                    }

            }

            next_quiz.setOnClickListener {
                if(MainActivity.nowQuestionIndex + 1 >= MainActivity.QuestionList.size)
                    MainActivity.run {
                        AlertDialog.Builder(this@QuizActivity)
                            .setTitle("채점하기")
                            .setMessage("문제를 다 풀었으면 채점할 수 있습니다.")
                            .setPositiveButton("채점하기") { dialog, id ->
                                var resultarr = ArrayList<Int>()

                                var corret = 0
                                for(quiz in QuestionList) {
                                    if(omr[quiz.id] == null)  {
                                        resultarr.add(0)
                                        continue
                                    }

                                    if(omr[quiz.id]!!.isEmpty()) {
                                        resultarr.add(0)
                                        continue
                                    }

                                    omr[quiz.id]!!.sort()
                                    quiz.answer.sort()
                                    if(omr[quiz.id]!! == quiz.answer) {
                                        resultarr.add(1)
                                        corret++
                                    } else {
                                        resultarr.add(0)
                                    }
                                }
                                var intent = Intent(this@QuizActivity, ResultActivity::class.java)
                                intent.putExtra("QuizAmount", QuestionList.size)
                                intent.putExtra("Correct", corret)
                                intent.putIntegerArrayListExtra("ResultArray", resultarr) // 채점 결과 배열
                                finish()
                                startActivity(intent)
                            }
                            .setNegativeButton("취소", null)
                            .show()
                    }
                    else
                    MainActivity.run {
                        initScreen(QuestionList.get(nowQuestionIndex + 1))
                        nowQuestionIndex += 1
                        progress_quiz.progress = nowQuestionIndex
                    }
            }
        } else {
            quiz_toolbar.removeAllViews()
        }

        quiz_hint.setOnClickListener {
            var nowQuiz: Quiz = MainActivity.QuestionList.get(MainActivity.nowQuestionIndex)
            AlertDialog.Builder(this)
                .setTitle(nowQuiz.id.toString() + "번 문제 해설")
                .setMessage(nowQuiz.hint + if(isReadOnly) "\n정답 : ${nowQuiz.answer}" else "")
                .setNegativeButton("닫기", null)
                .show()
        }


        initScreen(MainActivity.QuestionList.get(MainActivity.nowQuestionIndex))


    }

    fun clearScreen() {
        question_box.removeAllViewsInLayout()
        quiz_title.text = ""
    }

    fun initScreen(quiz: Quiz) {

        quiz_title.text = quiz.id.toString() + "번 문제. " + quiz.title
        question_box.removeAllViewsInLayout()


        for(count in 0 until quiz.question.size) {
            var cb : CheckBox = CheckBox(baseContext)
            var hr:View = View(baseContext)
            hr.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            hr.setBackgroundColor(Color.parseColor("#000000"))
            cb.run {
                text = quiz.question[count].substring(2)
                textSize = 16F

                setTextColor(Color.parseColor("#000000"))
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                question_box.addView(this)

                if(MainActivity.omr.containsKey(quiz.id)) {
                    var checks: ArrayList<Int> = MainActivity.omr[quiz.id]!!
                    if(checks.contains(count+1))
                        isChecked = true
                }


                setOnClickListener {
                    if(isReadOnly) {
                        isChecked = !isChecked
                    } else {
                        if (isChecked) {
                            var checks: ArrayList<Int>? = MainActivity.omr[quiz.id]
                            if (checks == null)
                                checks = ArrayList()
                            checks.add(count + 1)
                            MainActivity.omr.put(quiz.id, checks)
                        } else {
                            var checks: ArrayList<Int> = MainActivity.omr[quiz.id]!!
                            checks.remove(count + 1)
                            if (checks.isEmpty()) {
                                MainActivity.omr.remove(quiz.id)
                            } else {
                                MainActivity.omr.put(quiz.id, checks)
                            }
                        }
                    }
                }
            }
        }
    }
}
