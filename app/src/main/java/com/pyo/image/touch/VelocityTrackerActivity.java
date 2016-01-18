/*
 *  안드로이드 터치 이벤트 속도(Velocity) 체크 하기
 */
package com.pyo.image.touch;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

public class VelocityTrackerActivity extends Activity{
    
	private VelocityTracker tracker;
	public boolean onTouchEvent(MotionEvent event){
		int  touchAction = event.getAction();
		switch(touchAction){
		   case MotionEvent.ACTION_DOWN :
			     if(tracker != null){
			    	 //다시 터치를 시작 할 때 새로 생성하지 않고 초기화 하여 사용
			    	 tracker.clear();
			     }else{
			    	 // 객체를 구하기 위한 팩토리 메소드 호출
			    	 tracker = VelocityTracker.obtain();
			     }
			     //속도를 체크하기 위해 현재 모션객체를 세팅
			     tracker.addMovement(event);
			     break;
		   case MotionEvent.ACTION_MOVE :
			   //속도를 체크하기 위해 현재 모션객체를 세팅
			    tracker.addMovement(event);
			    //입력된 데이타를 기반으로 속도를 측정
			    //측정 시간 1은 1밀리초를, 1000은 1초를 의미합니다. 
			    //인자값이 1이라면 1밀리초동안의 픽셀단위의 이동 거리를 측정
			    //1000이면 1초 동안의 픽셀 단위의 이동 거리를 측정 함
			    tracker.computeCurrentVelocity(1000);
			    float xAxisVelocity = tracker.getXVelocity();
			    float yAxisVelocity = tracker.getYVelocity();
			    Log.i("VELOCITY", "X축 속도" + xAxisVelocity );
			    Log.i("VELOCITY", "Y축 속도" + yAxisVelocity );
			    break;
		   case MotionEvent.ACTION_CANCEL :
		   case MotionEvent.ACTION_UP :
			   /*  
			   객체를 재사용할 수 있도록 초기화	
			   드래깅 속도 측정은 빈번하게 일어나는 연산
			   OnTouchListener가 VelocityTracker의 인스턴스를 하나씩 점유 낭비.
		       VelocityTracker 객체의 생성은 팩토리 메소드에 위임하고 객체는 사용 후 반환
			   결과적으로 VelocityTracker 객체 하나가 모든 드래깅 동작에 공유되어 사용될 수 있음
		        */
			     
			     tracker.recycle();
			     break;
		}	
		event.recycle();
		return true;
	}
}
