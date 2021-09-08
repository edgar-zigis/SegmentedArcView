# SegmentedArcView

Unique progress view with rich customisation options! You can set any number of segments, each individual segment
can be customised with gradients and there is even a segment animation option!
##### Minimum target SDK: 17. RTL SUPPORTED.

![alt text](https://github.com/edgar-zigis/SegmentedArcView/blob/master/preview.png?raw=true)

### Gradle
Make sure you have jitpack.io included in your gradle repositories.

```gradle
maven { url "https://jitpack.io" }
```
```gradle
implementation 'com.github.edgar-zigis:segmentedarcview:1.1.1'
```
### Usage
``` xml
<com.zigis.segmentedarcview.SegmentedArcView
    android:id="@+id/progressView"
    android:layout_width="300dp"
    android:layout_height="300dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:sav_title="@string/outstanding_credit"  //  -> custom title text
    app:sav_value="150 €"  //  -> custom value text
    app:sav_titleTypefaceSize="20dp"  //  -> custom title text size
    app:sav_valueTypefaceSize="55dp"  //  -> custom value text size
    app:sav_titleFont="@font/tt_norms_pro_bold"  //  -> custom title font
    app:sav_valueFont="@font/tt_norms_pro_medium"  //  -> custom value font
    app:sav_titleTextColor="@android:color/darker_gray"  //  -> custom title text color
    app:sav_valueTextColor="@android:color/black"  //  -> custom value text color
    app:sav_titleVerticalOffset="20dp"  //  -> offset vertical position of the title
    app:sav_valueVerticalOffset="-10dp"  //  -> offset vertical position of the value
    app:sav_startAngle="150"  //  -> arc start angle in degrees
    app:sav_sweepAngle="240"  //  -> arc sweep angle in degrees
    app:sav_segmentSeparationAngle="9"  //  -> arc separator width in degrees
    app:sav_segmentThickness="8dp"  //  -> individual segment thickness
    app:sav_useCustomSweepAngles="true" />  //  -> enable variable segment sweep angles (note, segment sweeAngles need be set too!)
```
Some of the parameters however can be only set programmatically
``` kotlin
//  if blinking is enabled, then settings can be passed through here
progressView.blinkAnimationSettings = BlinkAnimationSettings(
    minAlpha = 0.4F,
    maxAlpha = 1F,
    duration = 2000L
)

//  you can set any number of segments you like
//  ArcSegment has gradientStart, gradientEnd and animation option
progressView.segments = listOf(
    ArcSegment(Color.parseColor("#eb3f25"), Color.parseColor("#eb3f25")),
    ArcSegment(Color.parseColor("#eb3f25"), Color.parseColor("#eb3f25")),
    ArcSegment(Color.parseColor("#e7ddba"), Color.parseColor("#efc956"), animate = true),
    ArcSegment(Color.parseColor("#e5e5e5"), Color.parseColor("#d3d3d3"))
)
//  if you want to set custom sweep angles, you need to set useCustomSweepAngles param to true
//  via xml or programatically and set angles to each segment you have:
ArcSegment(Color.parseColor("#eb3f25"), Color.parseColor("#eb3f25"), sweepAngle = 30f)
```
