# SegmentedArcView

Unique progress view with rich customisation options! You can set any number of segments, each individual segment
can be customised with gradients and there is even an animation option!
##### Minimum target SDK: 17. RTL SUPPORTED.

![alt text](https://github.com/edgar-zigis/SegmentedArcView/blob/master/preview.png?raw=true)

### Gradle
Make sure you have jitpack.io included in your gradle repositories.

```
maven { url "https://jitpack.io" }
```
```
implementation 'com.github.edgar-zigis:segmentedarcview:1.0.2'
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
    app:title="@string/outstanding_credit"  //  -> custom title text
    app:value="150 €"  //  -> custom value text
    app:titleTypefaceSize="20dp"  //  -> custom title text size
    app:valueTypefaceSize="55dp"  //  -> custom value text size
    app:titleTextColor="@android:color/darker_gray"  //  -> custom title text color
    app:valueTextColor="@android:color/black"  //  -> custom value text color
    app:titleVerticalOffset="20dp"  //  -> offset vertical position of the title
    app:valueVerticalOffset="-10dp"  //  -> offset vertical position of the value
    app:startAngle="150"  //  -> arc start angle in degrees
    app:sweepAngle="240"  //  -> arc sweep angle in degrees
    app:segmentSeparationAngle="9"  //  -> arc separator width in degrees
    app:segmentThickness="8dp" />  //  -> individual segment thickness
```
Some of the parameters however can be only set programmatically
``` kotlin
//  set fonts
progressView.titleTypeFace = Typeface.createFromAsset(assets, "equip_regular.ttf")
progressView.valueTypeface = Typeface.createFromAsset(assets, "ttnorms_bold.otf")

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
```
