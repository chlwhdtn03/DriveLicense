package com.example.drivelicense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
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

    var list: ArrayList<Place> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        var reader: CSVReader = CSVReader(InputStreamReader(assets.open("place.csv"), "euc-kr"))
        var nextLine: Array<String>

        list.add(Place("GUIDE", "GUIDE", "GUIDE", false, false))
        try {

            while (reader.readNext().also {

                    nextLine = it
                } != null) {
                if (nextLine[5].startsWith("EXM"))
                    continue
                list.add(Place(nextLine[1], nextLine[5], "0" + nextLine[6], false))
            }

        } catch (err: Exception) {

        }

        reader = CSVReader(InputStreamReader(assets.open("personalPlace.csv"), "euc-kr"))

        try {

            while (reader.readNext().also {
                    nextLine = it
                } != null) {
                if (nextLine[0].startsWith("학원명"))
                    continue
                list.add(Place(nextLine[0], nextLine[1], null, true))
            }
        } catch (err: Exception) {

        }

        reader.close()
        val mAdapter = PlaceAdapter(this, list)
        placelist.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        placelist.layoutManager = lm;
        placelist.setHasFixedSize(true)

        mAdapter.notifyDataSetChanged()
    }
}
