/*
 *  made by PYO.IN.SOO
 *  기본 터치 이벤트 처리
 */
package com.pyo.image.touch;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SimpleTouchEventActivity extends Activity{
	
	//이동 할 거리
    private float motionPositionX;
    private float motionPositionY;
    
    //마지막 터치 좌표
    private float motionLastTouchX;
    private float motionLastTouchY;
	private ImageView imageView;
	
	//이동을 적용 할 변환 행렬 객체
	private Matrix translatedMatrix = new Matrix();

	@Override
   public void onCreate(Bundle savedInstanceState){
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.simple_multi_event_layout);
	   
	   imageView = (ImageView)findViewById(R.id.simpleTouchImageView);
	   //반드시 MATRIX 타입으로 설정 함
	   imageView.setScaleType(ImageView.ScaleType.MATRIX);
	   //현재 매트릭스의 기본값으로 셑팅
	   translatedMatrix.setTranslate(1f, 1f);
	   //매트릭스 객체를 이미지 뷰에 세팅 함
	   imageView.setImageMatrix(translatedMatrix);
	   
	   imageView.setOnTouchListener(simpleTouchListener);
   }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	   return true;
	}
   private View.OnTouchListener simpleTouchListener = new View.OnTouchListener(){
	@Override
	public boolean onTouch(View v, MotionEvent event){
		
		  final int action = event.getAction();
		  switch (action) {
		    case MotionEvent.ACTION_DOWN: {
		    
		        final float x = event.getX();
		        final float y = event.getY();
		        
		        //처음 터치 할 때의 좌표값 설정
		        motionLastTouchX = x;
		        motionLastTouchY = y;
		        break;
		    }case MotionEvent.ACTION_MOVE: {
		        final float x = event.getX();
		        final float y = event.getY();
		  
		        final float distanceX = x - motionLastTouchX;
		        final float distanceY = y - motionLastTouchY;
		        
		        // 이미지가 이동할 거리
		        motionPositionX += distanceX;
		        motionPositionY += distanceY;
		        
		        //마지막 좌표를 다시 기억
		        motionLastTouchX = x;
		        motionLastTouchY = y;
		        
		        //행렬을 이동 시킨다
		        translatedMatrix.setTranslate(motionPositionX, motionPositionY);
	            //해당 이미지에 적용 한다
		        imageView.setImageMatrix(translatedMatrix);
		        break;
		    }
		 }
		  //true를 리턴해야 계속해서 터치이벤트가 발생 함
		  return true;
	  }  
    };
}