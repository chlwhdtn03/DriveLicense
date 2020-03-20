package com.chlwhdtn.drivelicense

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chlwhdtn.drivelicense.dialog.LoadingDialog
import com.chlwhdtn.drivelicense.custom.CustomToast
import com.chlwhdtn.drivelicense.dialog.LicenseFindDialog
import com.chlwhdtn.drivelicense.menucard.Card
import com.chlwhdtn.drivelicense.menucard.CardAdapter
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    companion object {
        var omr: HashMap<Int, ArrayList<Int>> = HashMap<Int, ArrayList<Int>>()
        var QuestionList : ArrayList<Quiz> = ArrayList<Quiz>()
        var nowQuestionIndex: Int = 0;
    }

    var maxQuizAmount = 40
    var loadingDialogDialog: LoadingDialog? = null
    var cardlist: ArrayList<Card> = ArrayList<Card>()
    lateinit var cardAdapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AlertDialog.Builder(this)
            .setMessage(
                "▶아직 많이 부족한 앱으로, 좋은 앱이 될 수 있도록 노력하고 있습니다.\n" +
                        "▶공단에서 공개한 2020 문제은행에서 문제가 나옵니다.\n" +
                        "▶문제가 너무 어렵거나 출제 범위에서 벗어난 문제가 나올 수 있습니다.\n" +
                        "▶그림 문제는 아직 준비중입니다.\n" +
                        "▶문제 자체를 풀 수 없는 문제가 발생할 수 있습니다.\n" +
                        "▶문제가 있다고 생각되는 부분은 리뷰에 작성해주세요. (피드백 적극 수용)\n"
            )
            .setCancelable(true)
            .setTitle("알림 ( _ _  )")
            .setPositiveButton("닫기", { dia, id ->

            })

            .create()
            .show()


        cardlist.run {

            add(
                Card(
                    "필기시험 시험보기",
                    "문제를 랜덤으로 풀어봅니다",
                    "#FFAA00",
                    View.OnClickListener {
                        loadingDialogDialog =
                            LoadingDialog(
                                this@MainActivity,
                                "문제를 불러옵니다..."
                            )
                        loadingDialogDialog?.show()
                        Thread(Runnable {
                            loadQuiz(false)
                        }).start()
                    })
            )

            add(
                Card(
                    "필기시험 연습하기",
                    "힌트와 함께 문제를\n랜덤으로 풀어봅니다",
                    "#BB00BB",
                    View.OnClickListener {
                        loadingDialogDialog =
                            LoadingDialog(
                                this@MainActivity,
                                "문제를 불러옵니다..."
                            )
                        loadingDialogDialog?.show()
                        Thread(Runnable {
                            loadQuiz(true)
                        }).start()
                    })
            )

            add(
                Card(
                    "문제 확인하기",
                    "자신이 저장한 문제들을\n확인하고 풀어봅니다",
                    "#00C0C0",
                    View.OnClickListener {
                        CustomToast.makeToast(this@MainActivity, "준비중입니다...")
                    })
            )

            add(
                Card(
                    "면허 찾기",
                    "나에게 맞는 면허 찾기 ",
                    "#A7A7A7",
                    View.OnClickListener {
                        LicenseFindDialog(this@MainActivity)
                            .show()
                    })
            )

            add(
                Card(
                    "시험장 리스트 보기",
                    "운전면허 시험장 보기",
                    "#AAAAFF",
                    View.OnClickListener {
                        startActivity(
                            Intent(this@MainActivity, PlaceActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_SINGLE_TOP
                            )
                        )
                    })
            )

        }


        versionInfo.setOnClickListener {
            CustomToast.makeToast(this, "[2020년 2월 10일 시행]\n1종 2종 학과시험 문제은행")
        }

        viewpdf.setOnClickListener {
//            CustomToast.makeToast(this, "준비중입니다")
//            startActivity(Intent(this, ViewPDF::class.java))
            startActivity(Intent(this, QuizActivity::class.java).also {
                it.putExtra("VIEW", 1)
                it.putExtra("isHint", true)
                it.putExtra("isAnswer", true)
            })
        }

        recent_quiz.setOnClickListener {
            startActivity(Intent(this, RecentQuiz::class.java))
        }

        cardAdapter = CardAdapter(cardlist, this)
        main_viewpager.adapter = cardAdapter
//        main_viewpager.setPageTransformer(
//            true,
//            ZoomOutPageTransformer()
//        )
        cardAdapter.notifyDataSetChanged()
    }

    override fun onResume() { // 액티비티가 맨 앞으로 올때
        super.onResume()
        var sf : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        myPoint.text = String.format("%,d 점", sf.getInt("HighScore", 0))
    }

    fun loadQuiz(isHintEnable: Boolean) {

        omr.clear()
        QuestionList.clear()

        var random = Random
        var cache = 0
        var Text : List<String>
        var reader: PdfReader = PdfReader(assets.open("2020.pdf"))
        var pages: ArrayList<Int> = ArrayList()
        var n =  2

        // 2~14페이지 법? 관련 (3장에서 랜덤 뽑기)
        for (i in 0..2) {
            cache = random.nextInt(2, 15)
            while(pages.contains(cache)) {
                cache = random.nextInt(2, 15)
            }
            println("랜덤 : ${cache}")
            pages.add(cache)
        }
        // 15~26페이지 (3장에서 랜덤 뽑기)
        for (i in 0..2) {
            cache = random.nextInt(15, 27)
            while(pages.contains(cache)) {
                cache = random.nextInt(15, 27)
            }
            println("랜덤 : ${cache}")
            pages.add(cache)
        }
        //27~97페이지 (5장에서 뽑기)
        for(i in 0..4) {
            cache = random.nextInt(27, 98)
            while(pages.contains(cache)) {
                cache = random.nextInt(27, 98)
            }
            println("랜덤 : ${cache}")
            pages.add(cache)
        }
        //98~203페이지 (5장에서 뽑기)
        for (i in 0..4) {
            cache = random.nextInt(98, 204)
            while(pages.contains(cache)) {
                cache = random.nextInt(98, 204)
            }
            println("랜덤 : ${cache}")
            pages.add(cache)
        }
        //204~239페이지 (3장 뽑기)
        for (i in 0..2) {
            cache = random.nextInt(204, 240)
            while (pages.contains(cache)) {
                cache = random.nextInt(204, 240)
            }
            println("랜덤 : ${cache}")
            pages.add(cache)
        }

        loadingDialogDialog?.progress?.max = pages.size

        var title: String = ""
        var num: Int = 0
        var questionCache: String = ""
        var question: ArrayList<String> = ArrayList<String>()
        var hint: String = ""
        var hintmode: Boolean = false
        for(i in pages) {
            loadingDialogDialog?.progress?.setProgress(
                loadingDialogDialog?.progress?.progress!! + 1,
                true
            )
            Text = PdfTextExtractor.getTextFromPage(reader, i)
                .replace("문제은행","")
                .replace("1종, 2종 학과시험 문제은행","")
                .replace("1종, 2종 학과시험 ", "")
                .trim()
                .split("\n")
            println(Text)
            for(t in 0..(Text.size-1)) {

                try {
                    if (Text[t + 1].trim().startsWith("정답") || Text[t].trim().startsWith("정답")) {
                        continue
                    }
                } catch(err: IndexOutOfBoundsException) {
                    if (Text[t].trim().startsWith("정답")) {
                        var arr = Text[t].trim().replace("정답", "").split("""\b""".toRegex())

                        for(index in 1..(arr.size-1) step 2) {

                            println("${arr[index]}번 정답 :: ${arr[index+1]}")

                            try {
                                QuestionList[QuestionList.size - ((arr.size - index) / 2)].answer.add(
                                    arr[index + 1].replace(". ", "")
                                        .replace(9312.toChar().toString(), "1")
                                        .replace(9313.toChar().toString(), "2")
                                        .replace(9314.toChar().toString(), "3")
                                        .replace(9315.toChar().toString(), "4")
                                        .replace(9316.toChar().toString(), "5").trim().toInt()
                                )
                            } catch(err: NumberFormatException) {
                                var temp = arr[index + 1].replace(". ", "")
                                    .replace(9312.toChar().toString(), "1")
                                    .replace(9313.toChar().toString(), "2")
                                    .replace(9314.toChar().toString(), "3")
                                    .replace(9315.toChar().toString(), "4")
                                    .replace(9316.toChar().toString(), "5").trim().split(",")
                                for(answers in temp) {
                                    QuestionList[QuestionList.size - ((arr.size - index) / 2)].answer.add(answers.trim().toInt());
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
                    if(num == 0) {
                        num = Text[t].trim().toInt()
                        continue
                    }
                    // 9312부터 1번 동그라미
                    if(hintmode == false && (Text[t].trim().startsWith("해설") == false && (Text[t].contains(9312.toChar()) || Text[t].contains(9313.toChar()) || Text[t].contains(9314.toChar()) || Text[t].contains(9315.toChar()) || Text[t].contains(9316.toChar())
                        || Text[t+1].contains(9312.toChar()) || Text[t+1].contains(9313.toChar()) || Text[t+1].contains(9314.toChar()) || Text[t+1].contains(9315.toChar()) || Text[t+1].contains(9316.toChar())
                                || Text[t+2].contains(9312.toChar()) || Text[t+2].contains(9313.toChar()) || Text[t+2].contains(9314.toChar()) || Text[t+2].contains(9315.toChar()) || Text[t+2].contains(9316.toChar())))
                        || (Text[t+1].trim().startsWith("해설") && (Text[t+1].trim().equals("해설") == false)) || Text[t+2].trim().equals("해설")) {

                        questionCache += Text[t].trim()
                        if(Text[t+1].contains(9312.toChar())) {
                            if(questionCache.contains(9312.toChar()))
                                hintmode = true
                        }
                        if(Text[t+1].contains(9313.toChar())) {
                            if(questionCache.contains(9313.toChar()))
                                hintmode = true
                        }
                        if(Text[t+1].contains(9314.toChar())) {
                            if(questionCache.contains(9314.toChar()))
                                hintmode = true
                        }
                        if(Text[t+1].contains(9315.toChar())) {
                            if(questionCache.contains(9315.toChar()))
                                hintmode = true
                        }
                        if(Text[t+1].contains(9316.toChar())) {
                            if(questionCache.contains(9316.toChar()))
                                hintmode = true
                        }
                        continue
                    } else {
                        if(question.size == 0) {
                            println("${num}번 문제. ${title} || ${questionCache}")
                            try {
                                question.add(questionCache.substring(questionCache.indexOf(9312.toChar()), questionCache.indexOf(9313.toChar())))
                                question.add(questionCache.substring(questionCache.indexOf(9313.toChar()), questionCache.indexOf(9314.toChar())))
                                question.add(questionCache.substring(questionCache.indexOf(9314.toChar()), questionCache.indexOf(9315.toChar())))
                                if(questionCache.contains(9316.toChar())) {
                                    question.add(questionCache.substring(questionCache.indexOf(9315.toChar()), questionCache.indexOf(9316.toChar())))
                                    question.add(questionCache.substring(questionCache.indexOf(9316.toChar())))
                                } else {
                                    question.add(questionCache.substring(questionCache.indexOf(9315.toChar())))
                                }
                            } catch(err: StringIndexOutOfBoundsException) {
                                question.clear()
                                println("에러 문항 ::: ${Text[t]}")
                                questionCache =questionCache.replace((9313.toChar()).toString(), ("\n" + 9313.toChar()))
                                questionCache =questionCache.replace((9314.toChar()).toString(), ("\n" + 9314.toChar()))
                                questionCache =questionCache.replace((9315.toChar()).toString(), ("\n" + 9315.toChar()))
                                for(cache in questionCache.split("\n")) {
                                    println(cache + " [ OK ] ")
                                    question.add(cache)
                                }
                            }
                        }
                        if(Text[t+1].trim().equals("해설") || Text[t].startsWith("해설") || hint.startsWith("해설")) {
                            hintmode = true
                            if(Text[t+1].trim().equals("해설"))
                                hint = "해설 " + hint
                            try {
                                if ((Text[t + 2].trim().toIntOrNull() is Int && (Text[t+3].trim().startsWith("정답") == false))
                                    || (Text[t+2].trim().startsWith("정답") && Text[t + 1].trim().toIntOrNull() is Int)) {
                                    println("힌트 : " + Text[t])
                                    hint += Text[t]
                                    hint = hint.replace("해설", "")
                                    QuestionList.add(Quiz(num, title, ArrayList<Int>(), question, hint.trim()))
                                    title = ""
                                    num = 0
                                    question = ArrayList<String>()
                                    questionCache = ""
                                    hint = ""
                                    hintmode = false
                                    continue
                                }
                            } catch(err: IndexOutOfBoundsException) {
                                hint += Text[t]
                                hint += Text[t+1]
                                hint = hint.replace("해설", "")
                                QuestionList.add(Quiz(num, title, ArrayList<Int>(), question, hint))
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
        }
        reader.close()
        System.out.println("생성된 문제의 개수 : ${QuestionList.size} || 최대 문제 개수 : ${maxQuizAmount}...")
        var temp: Int;
        while(QuestionList.size > maxQuizAmount) {
            QuestionList.removeAt(random.nextInt(QuestionList.size).also { temp = it })
            System.out.println("LIST 내 ${temp} 번 문제 삭제")
        }
        QuestionList.shuffle()
        loadingDialogDialog?.close()
        nowQuestionIndex = 0;
        var intent: Intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("isHint", isHintEnable)
        intent.putExtra("title", QuestionList.get(nowQuestionIndex).title)
        intent.putExtra("num", QuestionList.get(nowQuestionIndex).id)
        intent.putStringArrayListExtra("question", QuestionList.get(nowQuestionIndex).question)
        startActivity(intent)
    }

}
