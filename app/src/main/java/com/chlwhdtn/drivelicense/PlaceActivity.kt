package com.chlwhdtn.drivelicense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chlwhdtn.drivelicense.placelist.Place
import com.chlwhdtn.drivelicense.placelist.PlaceAdapter
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_place.*
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
