package com.zigis.segmentedarcview.custom

data class ArcSegment(
    val gradientStart: Int,
    val gradientEnd: Int,
    val animate: Boolean = false
)