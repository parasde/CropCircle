package com.parasde.cropcircle

import android.app.Activity
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        crop.setOnClickListener {
            val screenView = findViewById<View>(android.R.id.content)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                highCrop(screenView)
            }else {
                circleCrop(lowerCrop(screenView), cropArea)
            }
        }

        close.setOnClickListener {
            finish()
        }
    }

    private fun lowerCrop(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun highCrop(view: View) {
        val window = (view.context as Activity).window
        if (window != null) {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)
            try {
                PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1], locationOfViewInWindow[0] + view.width, locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        circleCrop(bitmap, cropArea)
                    }
                }, Handler())
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    private fun circleCrop(bitmap: Bitmap, view: View) {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val color = -0xfdfdf3
        val paint = Paint()
        val rect = Rect(view.x.toInt(), view.y.toInt(), view.x.toInt() + view.width, view.y.toInt() + view.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        showResult(result)
    }

    private fun showResult(bitmap: Bitmap) {
        val image = ImageView(this)
        image.setImageBitmap(bitmap)

        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
            .setMessage("CROP RESULT")
            .setView(image)
        builder.create().show()
    }
}
