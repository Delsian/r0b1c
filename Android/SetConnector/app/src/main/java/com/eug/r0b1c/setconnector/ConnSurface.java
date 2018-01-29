package com.eug.r0b1c.setconnector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class ConnSurface extends SurfaceView implements SurfaceHolder.Callback {
    private static final String LOG_TAG = "r0b1c.ConnSurface";
    private static final int BG_WIDTH = 230;
    private static final int BG_HEIGHT = 640;

    float xscale,yscale;
    static int leftConn = 0;
    static int rightConn = 0;

    public ConnSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        init();
    }

    public ConnSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        init();
    }

    public ConnSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        init();
    }

    @Override
    public void onDraw( Canvas c ) {
        drawConnState( c );
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        MainActivity.connSurface = this;
        init();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        MainActivity.connSurface = null;
    }

    @Override
    public void surfaceChanged(
            SurfaceHolder holder,
            int format,
            int width,
            int height) {
        Log.d( LOG_TAG, "surfaceChanged; width: "+width+" ;height: "+height);
        xscale = (float)width / (float)BG_WIDTH;
        yscale = (float)height / (float)BG_HEIGHT;
        Log.d( LOG_TAG,"xscale: "+xscale+"; yscale: "+yscale);
    }

    public static void init() {
        leftConn = 0;
        rightConn = 0;
    }

    private void drawConnState( Canvas c ) {
        Paint arrowPaint = new Paint();
        arrowPaint.setColor( Color.RED);
        arrowPaint.setStrokeWidth((int)(4.0f*xscale));
    }
}
