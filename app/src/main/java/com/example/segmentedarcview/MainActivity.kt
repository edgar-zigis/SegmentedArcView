package com.example.segmentedarcview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.segmentedarcview.custom.ContextWrapper
import com.zigis.segmentedarcview.custom.ArcSegment
import com.zigis.segmentedarcview.custom.BlinkAnimationSettings
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressView.titleTypeFace = Typeface.createFromAsset(assets, "equip_regular.ttf")
        progressView.valueTypeface = Typeface.createFromAsset(assets, "ttnorms_bold.otf")
        progressView.blinkAnimationSettings = BlinkAnimationSettings(
            minAlpha = 0.4F,
            maxAlpha = 1F,
            duration = 2000L
        )
        progressView.segments = listOf(
            ArcSegment(Color.parseColor("#eb3f25"), Color.parseColor("#eb3f25")),
            ArcSegment(Color.parseColor("#eb3f25"), Color.parseColor("#eb3f25")),
            ArcSegment(Color.parseColor("#e7ddba"), Color.parseColor("#efc956"), animate = false),
            ArcSegment(Color.parseColor("#e5e5e5"), Color.parseColor("#d3d3d3"))
        )
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val context = ContextWrapper.wrap(newBase, Locale("en"))    //  for RTL testing use "ar"
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }
}