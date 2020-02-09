package com.example.drivelicense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import kotlinx.android.synthetic.main.activity_view_pdf.*

class ViewPDF : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)

        var pdfView: PDFView = pdfview
        pdfView.fromAsset("2020.pdf") .load()

    }
}
