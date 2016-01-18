package com.pyo.image.touch;

import android.view.MotionEvent;

public class WrapperMotionEvent{
   protected MotionEvent event;
   protected WrapperMotionEvent(MotionEvent event){
      this.event = event;
   }
   public static  WrapperMotionEvent 
         getMotionEventInstance(MotionEvent event){
     return new WrapperMotionEvent(event);
   }
   public int getAction() {
      return event.getAction();
   }
   public float getX() {
      return event.getX();
   }
   public float getX(int pointerIndex) {
      return event.getX(pointerIndex);
   }
   public float getY() {
      return event.getY();
   }
   public float getY(int pointerIndex) {
      return event.getY(pointerIndex);
   }
   public int getPointerCount() {
	  //현재 화면상에 존재 하는 터치의 갯수를 리턴 함
	  return event.getPointerCount();
   }
   public int getPointerId(int pointerIndex) {
	  //포인터마다 유일한 ID값이 존재하며 이 메소드는 그 
	  //터치포인터의 ID를 리턴해 준다.
	  //MotionEvent.findPointerIndex(ID)는 위의
	  //메소드와 반대
	  return event.getPointerId(pointerIndex);
   }
}