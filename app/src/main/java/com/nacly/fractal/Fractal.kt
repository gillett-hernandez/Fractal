package com.nacly.fractal

import android.os.Bundle
//import android.support.design.widget.Snackbar
//import android.support.design.widget.NavigationView
//import android.support.v4.view.GravityCompat
//import android.support.v7.app.ActionBarDrawerToggle
//import android.support.v7.app.AppCompatActivity
//import android.view.Menu
//import android.view.MenuItem
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.app_bar_main.*
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.content.Context
import android.view.View
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
import android.support.v4.view.MotionEventCompat
import kotlin.math.roundToInt


//import android.view.Display
//import android.view.WindowManager

fun swap(var x:Int, var y:Int){
    var tmp = x
    x = y
    y = tmp
}

class Fractal : Activity() {
    // Called when the activity is first created.
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val displaySize = Point()
        display.getSize(displaySize)
        setContentView(FractalView(this, displaySize.x, displaySize.y))
    }

    companion object {

        // load our native library
        init {
            System.loadLibrary("fractal")
        }
    }
}

// Custom view for rendering plasma.
//
// Note: suppressing lint warning for ViewConstructor since it is
//       manually set from the activity and not used in any layout.
@SuppressLint("ViewConstructor")
internal class FractalView(context: Context, width: Int, height: Int) : View(context) {
    private val mBitmap: Bitmap
    private val mStartTime: Long
    private var x: Int = 0
    private var y: Int = 0
    private var _width: Int = -1
    private var _height: Int = -1

    // implementend by libplasma.so
    private external fun renderFractal(bitmap: Bitmap, time_ms: Long)

    init {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        mStartTime = System.currentTimeMillis()
    }

    override fun onDraw(canvas: Canvas) {
        renderFractal(mBitmap, System.currentTimeMillis() - mStartTime)
        canvas.drawBitmap(mBitmap, 0f, 0f, null)
        // force a redraw, with a different time-based pattern.
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action = MotionEventCompat.getActionMasked(event)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
//                Log.d(DEBUG_TAG, "Action was DOWN")
                x = event.x.roundToInt()
                y = event.y.roundToInt()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
//                Log.d(DEBUG_TAG, "Action was MOVE")
                return true
            }
            MotionEvent.ACTION_UP -> {
//                Log.d(DEBUG_TAG, "Action was UP")
                var _x = event.x.roundToInt()
                var _y = event.y.roundToInt()
                _width = x-_x
                _height = y-_y
                if (_width < 0) {
                    var tmp = x
                    x = _x
                    _x = tmp
                }
                if (_height < 0) {

                }
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
//                Log.d(DEBUG_TAG, "Action was CANCEL")
                return true
            }
            MotionEvent.ACTION_OUTSIDE -> {
//                Log.d(DEBUG_TAG, "Movement occurred outside bounds " + "of current screen element")
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }

    //    @Override protected void onTouch(Canvas canvas) {
    //        renderPlasma(mBitmap, System.currentTimeMillis() - mStartTime);
    //        canvas.drawBitmap(mBitmap, 0, 0, null);
    //        // force a redraw, with a different time-based pattern.
    //        invalidate();

    //    }
}

