package com.chlwhdtn.drivelicense.util

import com.itextpdf.text.pdf.PRStream
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.parser.ImageRenderInfo
import com.itextpdf.text.pdf.parser.PdfImageObject
import com.itextpdf.text.pdf.parser.RenderListener
import com.itextpdf.text.pdf.parser.TextRenderInfo
import java.io.FileOutputStream
import java.io.IOException


internal class ImageRenderListener(val name: String) : RenderListener {
    var counter = 100000
    override fun beginTextBlock() {}
    override fun renderText(renderInfo: TextRenderInfo) {}
    override fun endTextBlock() {}
    override fun renderImage(renderInfo: ImageRenderInfo) {
        try {
            val image = renderInfo.image ?: return
            val number =
                if (renderInfo.ref != null) renderInfo.ref.number else counter++
            var filename =
                String.format("%s-%s.%s", name, number, image.fileType)
            var os = FileOutputStream(filename)
            os.write(image.imageAsBytes)
            os.flush()
            os.close()
            val imageDictionary = image.dictionary
            val maskStream = imageDictionary.getAsStream(PdfName.SMASK) as PRStream
            if (maskStream != null) {
                val maskImage = PdfImageObject(maskStream)
                filename =
                    String.format("%s-%s-mask.%s", name, number, maskImage.fileType)
                os = FileOutputStream(filename)
                os.write(maskImage.imageAsBytes)
                os.flush()
                os.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}