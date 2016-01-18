/*
 *  멀티 터치 예제 
 *  반드시 안드로이드 단말기에서 테스트 해야 함
 */
package com.pyo.image.touch;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MultiTouchActivity extends Activity{
   private static final String TAG =  "MultiTouchActivity";
   
   //이미지를 Move 또는 Zoom 했을때 적용할 변환 행렬 객체
   private Matrix matrix = new Matrix();
   private Matrix savedMatrix = new Matrix();

   //사용자의 터치 액션 상태값
   private static final int NONE_STATE = 0;
   private static final int DRAG_STATE = 1;
   private static final int ZOOM_STATE = 2;
   
   //사용자의 현재 상태
   private int userMode = NONE_STATE;

   //Zoom 상태시 저장 할 상태 값
   private PointF startPoint = new PointF();
   private PointF midPoint = new PointF();
   
   private float oldDistance = 1f;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.multi_touch_layout);
      ImageView imageView = (ImageView)findViewById(R.id.touchImageView);
      imageView.setOnTouchListener(userTouchImpl);
      
      //현재 매트릭스의 기본값으로 셑팅
      matrix.setTranslate(1f, 1f);
      //매트릭스 객체를 이미지 뷰에 세팅 함
      imageView.setImageMatrix(matrix);
   }
   private View.OnTouchListener userTouchImpl = new  View.OnTouchListener(){
	   @Override
	   public boolean onTouch(View v, MotionEvent rawEvent) {
		   WrapperMotionEvent event = WrapperMotionEvent.getMotionEventInstance(rawEvent);
	       ImageView view = (ImageView) v;
	      //액션 값과 MotionEvent.ACTION_MASK 을 '&' 연산을 하게 되면, 
	      //눌리거나(DOWN) 뗀(UP) 포인터의 인덱스 값을 얻을 수 있음
	      switch (event.getAction() & MotionEvent.ACTION_MASK) {
	       case MotionEvent.ACTION_DOWN: //터치 했을 때
	         //현재 변환 행렬 객체를 저장
	    	 savedMatrix.set(matrix);
	         
	         //PointF의 좌표값을 셑팅
	    	 startPoint.set(event.getX(), event.getY());
	         Log.d(TAG, " mode =DRAG");
	         userMode = DRAG_STATE;
	         break;
	       //첫번째 포인터가 아닌 다른 포인터가 눌러질때 경우 발생
	       case MotionEvent.ACTION_POINTER_DOWN:
	    	 oldDistance = spacingPointers(event);
	         Log.d(TAG, "oldDistance =" + oldDistance);
	         if (oldDistance > 10f) {
	            savedMatrix.set(matrix);
	            middlePoint(midPoint, event);
	            userMode = ZOOM_STATE;
	            Log.d(TAG, "userMode=ZOOM_STATE");
	         }
	         break;
	       case MotionEvent.ACTION_UP:
	       //첫번째 포인터가 아닌 다른 포인터를 UP할 경우 발생
	       case MotionEvent.ACTION_POINTER_UP:
	         userMode = NONE_STATE;
	         Log.d(TAG, "userMode=NONE_STATE");
	         break;
	       case MotionEvent.ACTION_MOVE:
	         if (userMode == DRAG_STATE) {
	            //변환 행렬값 셑팅
	        	matrix.set(savedMatrix);
	            matrix.postTranslate(event.getX() - startPoint.x,
	                                 event.getY() - startPoint.y);
	         }else if (userMode == ZOOM_STATE) {
	            float newDistance = spacingPointers(event);
	            Log.d(TAG, "newDistance=" + newDistance);
	            if (newDistance > 10f) {
	               matrix.set(savedMatrix);              
	               //거리를 계산하여 크기 변환
	               float scale = newDistance/oldDistance;
	               matrix.postScale(scale, scale, midPoint.x, midPoint.y);
	            }
	         }
	         break;
	      }
	      //현재 셑팅된 변환 행력값을 적용하여 이미지를 그림.
	      view.setImageMatrix(matrix);
	      return true; // 핸들링된 이벤트
	   }  
   }; 
   /** 
       첫번째와 두번째의 공간을 알아냄
   */
   private float spacingPointers(WrapperMotionEvent event) {
      //각 포인터의 좌표값을 얻는다.
      float dX = event.getX(0) - event.getX(1);
      float dY = event.getY(0) - event.getY(1);

      //계산하여 정사각형의 값을 계산하여 넘김(제곱근)
      return (float)Math.sqrt(dX *dX + dY *dY);
   }
   /** 
     첫번째와 두번째 공간의 중간치를 계산 하여 PointF에 세팅 함
   */
   private void middlePoint(PointF point, WrapperMotionEvent event){
	  //첫번째 좌표와 두번째 포인터의 좌표값을 얻어와 중간값을 잡는다
      float x = event.getX(0) + event.getX(1);
      float y = event.getY(0) + event.getY(1);
      point.set(x / 2, y / 2);
   }
}