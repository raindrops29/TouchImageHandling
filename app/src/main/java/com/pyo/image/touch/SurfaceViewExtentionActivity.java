/*
 *  SurfaceView 확장 예제
 */
package com.pyo.image.touch;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewExtentionActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(new SurfaceViewImageDrawing(this));
	}
	private class SurfaceViewImageDrawing extends SurfaceView implements
	           SurfaceHolder.Callback, Runnable{
	    private SurfaceHolder holder;	
	    private Bitmap imageMove;	
	    private float movePointX = 0;	
	    private float movePointY = 0;	
	
	    private Thread thread = null;	
	    private PointF pImage;	
	    private Point pWindow;	
	    private boolean imageMoveFlag = false;	
	    private float touchPointX = 0;	
	    private float touchPointY = 0;
	    private Paint paint;
	    private boolean threadFlag;
	
	    public SurfaceViewImageDrawing(Context context) {
	        super(context);	
	        
	        // 서피스홀더 객체를 생성
	        holder = getHolder();
	        //홀더에 콜백메소드가 동작 하도록 등록
	        holder.addCallback(this);	
	        setFocusable(true);
	        
	        paint = new Paint();	
	        paint.setAntiAlias(true);	
	        paint.setTextSize(20);	
	        paint.setColor(Color.BLACK);
	        paint.setTypeface(Typeface.DEFAULT_BOLD);
	    }
	    /** 화면이 변경 될 때 */	
	    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	    }
	    /** 생성 될 때 */
	    public void surfaceCreated(SurfaceHolder holder) {	
	        // window 크기
	        pWindow = new Point();
	        pWindow.x = 320;
	        pWindow.y = 480;  
	
	        // 이미지 위치	
	        pImage = new PointF(0, 0);	
	        Resources res = getResources();	
	        Bitmap tempBitmap = BitmapFactory.decodeResource(res, R.drawable.taxiicon);
	   
	        // 표시할 위치
	        movePointX = pWindow.x / 2;	
	        movePointY = pWindow.y / 2;
		         
	        //해당 비트맵을 얻는다
	        imageMove = Bitmap.createScaledBitmap(tempBitmap, 100, 100, true);
	        setClickable(true);
	     
	        thread = new Thread(this);
	        thread.start();	
	    }	
	    public void surfaceDestroyed(SurfaceHolder holder){
	        /*if( thread != null){ 
	    	   thread.interrupt();
	        }*/
	    }	
	    public void run() {	
	        // Canvas 의 사이즈
	        pWindow.x = getWidth();
	        pWindow.y = getHeight();
	        Canvas surfaceCanvas = null;	
	        while (!threadFlag) {	            
	            try {
	            	surfaceCanvas = holder.lockCanvas(null);
	                synchronized (holder){
	                    doSurfaceViewDrawing(surfaceCanvas);
	                    Thread.sleep(50);
	                }
	            }catch(InterruptedException e){
	                //Thread.currentThread().interrupt();
	            	
	            }finally{
	                if (surfaceCanvas != null){
	                    holder.unlockCanvasAndPost(surfaceCanvas);
	                }
	            }
	        }
	    }	
	    private void doSurfaceViewDrawing(Canvas canvas){	
	        pImage.x = movePointX;	
	        pImage.y = movePointY;
	        
	        if( canvas != null){
	        	canvas.drawColor(Color.LTGRAY);		        canvas.drawBitmap(imageMove, movePointX - 40, movePointY - 30, null);
		        canvas.drawText("터치 가능 여부 : " + imageMoveFlag, 0, 50, paint);	
		        canvas.drawText("이미지 위치  : X= " + pImage.x + ", Y=" + pImage.y, 0, 80, paint);
		        canvas.drawText("터치 포인트  : X= " + touchPointX +", Y=" + touchPointY, 0, 110, paint);
	        }	        
	    }
	    @Override	
	    public boolean onTouchEvent(MotionEvent event){
	
	        final float currentPointX = event.getX();	
	        final float currentPointY = event.getY();
	
	        touchPointX = currentPointX;	
	        touchPointY = currentPointY;
	
	        switch (event.getAction()){	
	           case MotionEvent.ACTION_MOVE:	
	             if (imageMoveFlag){	//터치 되어 있다면 이동 시킨다
	            	 movePointX = currentPointX;	
	            	 movePointY = currentPointY;	
	            }
	            break;
	        case MotionEvent.ACTION_UP:	
	        	imageMoveFlag = false;
	            break;
	        case MotionEvent.ACTION_DOWN:
	        	 //현재 이미지에 터치 되어 있는지 확인 한다(마진 30,30)
	        	if ((pImage.x - 30 < currentPointX) && (currentPointX < pImage.x + 30)){	
		            if ((pImage.y - 30 < currentPointY) && ( currentPointY < pImage.y + 30)){
		            	//터치 됨
		            	imageMoveFlag = true;
		            }
		        }
	            break;
	        }
	        return true;
	    }
	}
}