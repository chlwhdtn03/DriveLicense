package com.example.drivelicense

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.text.Layout
import android.view.View
import android.widget.*
import com.example.drivelicense.custom.CustomToast
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {


    var isReadOnly: Boolean = false
    var isViewOnly: Boolean = false
    var page: Int = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        progress_quiz.max = MainActivity.QuestionList.size - 1

        isReadOnly = intent.getIntExtra("READ", 0) == 1
        isViewOnly = intent.getIntExtra("VIEW", 0) == 1

        if (!isViewOnly) {
            if (!isReadOnly) {
                previous_quiz.setOnClickListener {
                    if (MainActivity.nowQuestionIndex - 1 < 0)
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
                    if (MainActivity.nowQuestionIndex + 1 >= MainActivity.QuestionList.size)
                        MainActivity.run {
                            AlertDialog.Builder(this@QuizActivity)
                                .setTitle("채점하기")
                                .setMessage("문제를 다 풀었으면 채점할 수 있습니다.")
                                .setPositiveButton("채점하기") { dialog, id ->
                                    var resultarr = ArrayList<Int>()

                                    var corret = 0
                                    for (quiz in QuestionList) {
                                        if (omr[quiz.id] == null) {
                                            resultarr.add(0)
                                            continue
                                        }

                                        if (omr[quiz.id]!!.isEmpty()) {
                                            resultarr.add(0)
                                            continue
                                        }

                                        omr[quiz.id]!!.sort()
                                        quiz.answer.sort()
                                        if (omr[quiz.id]!! == quiz.answer) {
                                            resultarr.add(1)
                                            corret++
                                        } else {
                                            resultarr.add(0)
                                        }
                                    }
                                    var intent =
                                        Intent(this@QuizActivity, ResultActivity::class.java)
                                    intent.putExtra("QuizAmount", QuestionList.size)
                                    intent.putExtra("Correct", corret)
                                    intent.putIntegerArrayListExtra(
                                        "ResultArray",
                                        resultarr
                                    ) // 채점 결과 배열
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
                // 읽기 전용
                quiz_toolbar.removeAllViews()
            }

            initScreen(MainActivity.QuestionList.get(MainActivity.nowQuestionIndex))
        } else {
            // 보기 전용
            loadQuiz(page)
            initScreen(MainActivity.QuestionList.get(MainActivity.nowQuestionIndex))
            previous_quiz.setOnClickListener {
                if (MainActivity.nowQuestionIndex - 1 < 0) {
                    if (page < 3) {
                        MainActivity.run {
                            CustomToast.makeToast(applicationContext, "더 이상 뒤로갈 수 없습니다")
                        }
                        return@setOnClickListener
                    }
                    loadQuiz(--page)
                    MainActivity.nowQuestionIndex = MainActivity.QuestionList.size - 1
                    MainActivity.run {
                        initScreen(MainActivity.QuestionList.get(MainActivity.QuestionList.size - 1))
                        progress_quiz.progress = MainActivity.nowQuestionIndex
                    }
                } else {
                    MainActivity.run {
                        initScreen(QuestionList.get(nowQuestionIndex - 1))
                        nowQuestionIndex -= 1
                        progress_quiz.progress = nowQuestionIndex
                    }
                }
            }

            next_quiz.setOnClickListener {
                if (MainActivity.nowQuestionIndex + 1 >= MainActivity.QuestionList.size) {
                    loadQuiz(++page)
                    MainActivity.nowQuestionIndex = 0
                    initScreen(MainActivity.QuestionList.get(0))
                    progress_quiz.progress = MainActivity.nowQuestionIndex
                } else {
                    MainActivity.run {
                        initScreen(QuestionList.get(nowQuestionIndex + 1))
                        nowQuestionIndex += 1
                        progress_quiz.progress = nowQuestionIndex
                    }
                }
            }

        }

        quiz_hint.setOnClickListener {
            var nowQuiz: Quiz = MainActivity.QuestionList.get(MainActivity.nowQuestionIndex)
            AlertDialog.Builder(this)
                .setTitle(nowQuiz.id.toString() + "번 문제 해설")
                .setMessage(nowQuiz.hint + if (isReadOnly) "\n정답 : ${nowQuiz.answer}" else "")
                .setNegativeButton("닫기", null)
                .show()
        }

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
            var cv: CardView = CardView(baseContext)
            hr.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            hr.setBackgroundColor(Color.parseColor("#000000"))

            cv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).also {
                it.bottomMargin = 16
                it.topMargin = 16
                it.leftMargin = 30
                it.rightMargin = 30
            }

            cv.cardElevation = 15f

            /*

                <CheckBox
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="테스트 입니다"
                    android:checked="true"
                    android:button="null"
                    android:background="@drawable/quiz_checkbox"/>

             */
            cb.run {
                buttonDrawable = null
                background = getDrawable(R.drawable.quiz_checkbox)
                setTextColor(getColorStateList(R.color.quiz_checkbox_text))
                text = quiz.question[count].substring(2).trim()
                textSize = 16F
                textAlignment = LinearLayout.TEXT_ALIGNMENT_CENTER


                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                cv.addView(this)
                question_box.addView(cv)

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


    fun loadQuiz(page: Int) {

        MainActivity.omr.clear()
        MainActivity.QuestionList.clear()

        var random = Random
        var cache = 0
        var Text: List<String>
        var reader: PdfReader = PdfReader(assets.open("2020.pdf"))
        var n = 2

        var title: String = ""
        var num: Int = 0
        var questionCache: String = ""
        var question: ArrayList<String> = ArrayList<String>()
        var hint: String = ""
        var hintmode: Boolean = false
        Text = PdfTextExtractor.getTextFromPage(reader, page)
            .replace("문제은행", "")
            .replace("1종, 2종 학과시험 문제은행", "")
            .replace("1종, 2종 학과시험 ", "")
            .trim()
            .split("\n")
        println(Text)
        for (t in 0..(Text.size - 1)) {

            try {
                if (Text[t + 1].trim().startsWith("정답") || Text[t].trim().startsWith("정답")) {
                    continue
                }
            } catch (err: IndexOutOfBoundsException) {
                if (Text[t].trim().startsWith("정답")) {
                    var arr = Text[t].trim().replace("정답", "").split("""\b""".toRegex())

                    for (index in 1..(arr.size - 1) step 2) {

                        println("${arr[index]}번 정답 :: ${arr[index + 1]}")

                        try {
                            MainActivity.QuestionList[MainActivity.QuestionList.size - ((arr.size - index) / 2)].answer.add(
                                arr[index + 1].replace(". ", "")
                                    .replace(9312.toChar().toString(), "1")
                                    .replace(9313.toChar().toString(), "2")
                                    .replace(9314.toChar().toString(), "3")
                                    .replace(9315.toChar().toString(), "4")
                                    .replace(9316.toChar().toString(), "5").trim().toInt()
                            )
                        } catch (err: NumberFormatException) {
                            var temp = arr[index + 1].replace(". ", "")
                                .replace(9312.toChar().toString(), "1")
                                .replace(9313.toChar().toString(), "2")
                                .replace(9314.toChar().toString(), "3")
                                .replace(9315.toChar().toString(), "4")
                                .replace(9316.toChar().toString(), "5").trim().split(",")
                            for (answers in temp) {
                                MainActivity.QuestionList[MainActivity.QuestionList.size - ((arr.size - index) / 2)].answer.add(
                                    answers.trim().toInt()
                                );
                            }
                        }
                    }
                    continue
                }
            }
            if ((title.trim().endsWith("?") || title.trim().endsWith(")")) == false) {
                if (Text[t].trim().toIntOrNull() is Int) {
                    num = Text[t].trim().toInt()
                    continue
                }
                title += Text[t]
            } else {
                println("${num} 번 확인")
                if (num == 0) {
                    num = Text[t].trim().toInt()
                    continue
                }
                // 9312부터 1번 동그라미
                if (hintmode == false && (Text[t].trim().startsWith("해설") == false && (Text[t].contains(
                        9312.toChar()
                    ) || Text[t].contains(9313.toChar()) || Text[t].contains(9314.toChar()) || Text[t].contains(
                        9315.toChar()
                    ) || Text[t].contains(9316.toChar())
                            || Text[t + 1].contains(9312.toChar()) || Text[t + 1].contains(9313.toChar()) || Text[t + 1].contains(
                        9314.toChar()
                    ) || Text[t + 1].contains(9315.toChar()) || Text[t + 1].contains(9316.toChar())
                            || Text[t + 2].contains(9312.toChar()) || Text[t + 2].contains(9313.toChar()) || Text[t + 2].contains(
                        9314.toChar()
                    ) || Text[t + 2].contains(9315.toChar()) || Text[t + 2].contains(9316.toChar())))
                    || (Text[t + 1].trim().startsWith("해설") && (Text[t + 1].trim().equals("해설") == false)) || Text[t + 2].trim().equals(
                        "해설"
                    )
                ) {

                    questionCache += Text[t].trim()
                    if (Text[t + 1].contains(9312.toChar())) {
                        if (questionCache.contains(9312.toChar()))
                            hintmode = true
                    }
                    if (Text[t + 1].contains(9313.toChar())) {
                        if (questionCache.contains(9313.toChar()))
                            hintmode = true
                    }
                    if (Text[t + 1].contains(9314.toChar())) {
                        if (questionCache.contains(9314.toChar()))
                            hintmode = true
                    }
                    if (Text[t + 1].contains(9315.toChar())) {
                        if (questionCache.contains(9315.toChar()))
                            hintmode = true
                    }
                    if (Text[t + 1].contains(9316.toChar())) {
                        if (questionCache.contains(9316.toChar()))
                            hintmode = true
                    }
                    continue
                } else {
                    if (question.size == 0) {
                        println("${num}번 문제. ${title} || ${questionCache}")
                        try {
                            question.add(
                                questionCache.substring(
                                    questionCache.indexOf(9312.toChar()),
                                    questionCache.indexOf(9313.toChar())
                                )
                            )
                            question.add(
                                questionCache.substring(
                                    questionCache.indexOf(9313.toChar()),
                                    questionCache.indexOf(9314.toChar())
                                )
                            )
                            question.add(
                                questionCache.substring(
                                    questionCache.indexOf(9314.toChar()),
                                    questionCache.indexOf(9315.toChar())
                                )
                            )
                            if (questionCache.contains(9316.toChar())) {
                                question.add(
                                    questionCache.substring(
                                        questionCache.indexOf(9315.toChar()),
                                        questionCache.indexOf(9316.toChar())
                                    )
                                )
                                question.add(questionCache.substring(questionCache.indexOf(9316.toChar())))
                            } else {
                                question.add(questionCache.substring(questionCache.indexOf(9315.toChar())))
                            }
                        } catch (err: StringIndexOutOfBoundsException) {
                            question.clear()
                            println("에러 문항 ::: ${Text[t]}")
                            questionCache = questionCache.replace(
                                (9313.toChar()).toString(),
                                ("\n" + 9313.toChar())
                            )
                            questionCache = questionCache.replace(
                                (9314.toChar()).toString(),
                                ("\n" + 9314.toChar())
                            )
                            questionCache = questionCache.replace(
                                (9315.toChar()).toString(),
                                ("\n" + 9315.toChar())
                            )
                            for (cache in questionCache.split("\n")) {
                                println(cache + " [ OK ] ")
                                question.add(cache)
                            }
                        }
                    }
                    if (Text[t + 1].trim().equals("해설") || Text[t].startsWith("해설") || hint.startsWith(
                            "해설"
                        )
                    ) {
                        hintmode = true
                        if (Text[t + 1].trim().equals("해설"))
                            hint = "해설 " + hint
                        try {
                            if ((Text[t + 2].trim().toIntOrNull() is Int && (Text[t + 3].trim().startsWith(
                                    "정답"
                                ) == false))
                                || (Text[t + 2].trim().startsWith("정답") && Text[t + 1].trim().toIntOrNull() is Int)
                            ) {
                                println("힌트 : " + Text[t])
                                hint += Text[t]
                                hint = hint.replace("해설", "")
                                MainActivity.QuestionList.add(
                                    Quiz(
                                        num,
                                        title,
                                        ArrayList<Int>(),
                                        question,
                                        hint.trim()
                                    )
                                )
                                title = ""
                                num = 0
                                question = ArrayList<String>()
                                questionCache = ""
                                hint = ""
                                hintmode = false
                                continue
                            }
                        } catch (err: IndexOutOfBoundsException) {
                            hint += Text[t]
                            hint += Text[t + 1]
                            hint = hint.replace("해설", "")
                            MainActivity.QuestionList.add(
                                Quiz(
                                    num,
                                    title,
                                    ArrayList<Int>(),
                                    question,
                                    hint
                                )
                            )
                            title = ""
                            num = 0
                            question = ArrayList<String>()
                            questionCache = ""
                            hint = ""
                            hintmode = false
                            continue
                        }
                        hint += Text[t]
                    }
                }
            }
        }
        reader.close()
        var temp: Int;
        MainActivity.nowQuestionIndex = 0;
    }


}
