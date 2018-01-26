package hu.uw.pallergabor.motorboat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BoatSurface extends SurfaceView implements SurfaceHolder.Callback {
	private static final String LOG_TAG = "MotorBoat.BoatSurface";
	private static final int BG_WIDTH = 230;
	private static final int BG_HEIGHT = 640;
		
	float xscale,yscale;
	static boolean leftMotor = false;
	static boolean rightMotor = false;
	
	static int leftArrow[][] = {
		{ 5,60,10,50 },		
		{ 10,50,15,60 },	
		{ 10,50,10,150 }
	};

	static int rightArrow[][] = {
		{ 225,60,220,50 },		
		{ 220,50,215,60 },	
		{ 220,50,220,150 }
	};

	
	public BoatSurface(Context context) {
		super(context);
        getHolder().addCallback(this);
        init();
	}

	public BoatSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
        getHolder().addCallback(this);
        init();
	}

	public BoatSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        getHolder().addCallback(this);
        init();
	}

	
	@Override
	public void onDraw( Canvas c ) {
		drawMotorState( c );
	}
	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		MainActivity.boatSurface = this;
		init();
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

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		MainActivity.boatSurface = null;
	}

	public static void init() {
		leftMotor = false;
		rightMotor = false;
	}

	public static void setMotors( boolean l, boolean r) {
		leftMotor = l;
		rightMotor = r;
	}
	
	private void drawMotorState( Canvas c ) {
		Paint arrowPaint = new Paint();
		arrowPaint.setColor( Color.RED);
		arrowPaint.setStrokeWidth((int)(4.0f*xscale));
		if( leftMotor )
			drawArrow( c,leftArrow,arrowPaint);
		if( rightMotor )
			drawArrow( c,rightArrow,arrowPaint);
		
	}
	
	private void drawArrow( Canvas c,int[][] arrowData, Paint p ) {
		for( int i = 0 ; i < arrowData.length ; ++i ) {
			int v[] = arrowData[i];
			float startX = (float)v[0];
			float startY = (float)v[1];
			float stopX = (float)v[2];
			float stopY = (float)v[3];
			startX *= xscale;
			startY *= yscale;
			stopX *= xscale;
			stopY *= yscale;
			c.drawLine(startX, startY, stopX, stopY, p);
		}
	}
}
