package com.example.drivelicense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.drivelicense.placelist.Place
import com.example.drivelicense.placelist.PlaceAdapter
import com.example.drivelicense.scorecard.ScoreCard
import com.example.drivelicense.scorecard.ScoreCardAdapter
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_place.*
import kotlinx.android.synthetic.main.activity_recent_quiz.*
import java.io.InputStreamReader
import java.lang.Exception

class PlaceActivity : AppCompatActivity() {

    val list: ArrayList<Place> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        try {
            var reader = CSVReader(InputStreamReader(assets.open("place.csv"), "euc-kr"))

            var nextLine: Array<String>
            while (reader.readNext().also {


                    nextLine = it
                } != null) {
                if (nextLine[5].startsWith("EXM"))
                    continue
                list.add(Place(nextLine[1], nextLine[5]))
            }
        } catch (err: Exception) {

        }


        val mAdapter = PlaceAdapter(this, list)
        placelist.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        placelist.layoutManager = lm;
        placelist.setHasFixedSize(true)

    }
}
