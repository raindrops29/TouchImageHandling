/*
 *  사인 후 이미지 저장 하기
 */
package com.pyo.image.touch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TouchEventCurveActivity extends Activity {
   private Button btnSignatureSave;
   private Button btnSignatureInit;
   private FreedomCurveDrawingView freeCurveView;
   private String directory;
	@Override
   public void onCreate(Bundle savedInstanceState){
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.signature_view);
	   
	   freeCurveView = (FreedomCurveDrawingView)findViewById(R.id.drawingCurveView);
	   btnSignatureSave = (Button)findViewById(R.id.btnSignatureSave);
       btnSignatureInit = (Button)findViewById(R.id.btnSignatureInit);
       
       btnSignatureSave.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				if(freeCurveView.touchPoints.size()!=0){
					
					Bitmap pointBitmap ,tempBitmap = null;
					
					int left=280,right=240;//min,max
					int touchPointSize = freeCurveView.touchPoints.size();
					for(int i=0; i < touchPointSize ; i++){
						if(freeCurveView.touchPoints.get(i).pointX > right)
							right=(int)freeCurveView.touchPoints.get(i).pointX;
					}
					for(int j=0;j<touchPointSize ;j++){
						if(freeCurveView.touchPoints.get(j).pointX < left)
							left=(int)freeCurveView.touchPoints.get(j).pointX;
					}
					
					if(freeCurveView.getDrawingCache() != null){
						pointBitmap = freeCurveView.getDrawingCache();	
					    String fileName = String.valueOf(System.currentTimeMillis());
					    FileOutputStream toFile = null;
					    File file = null;
						try{
							tempBitmap=Bitmap.createBitmap(pointBitmap, left, 0, right-left, pointBitmap.getHeight());
                            file = new File(directory, fileName + ".png");
                            toFile = new FileOutputStream(file);							
							tempBitmap.compress(
									CompressFormat.PNG, 100,toFile);
							Toast.makeText(TouchEventCurveActivity.this, "서명이 저장 되었습니다!", 
									Toast.LENGTH_SHORT).show();
					       freeCurveView.touchPoints.removeAll(freeCurveView.touchPoints);
					       freeCurveView.destroyDrawingCache();
				           freeCurveView.invalidate();	
				           btnSignatureSave.setEnabled(false);
					       btnSignatureInit.setEnabled(false);
					       btnSignatureSave.setText("저장 완료");
					       btnSignatureInit.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_banned));
						}catch (IOException e) {
							Log.e("저장 중 예외 발생!",e.toString());
						}finally{		
							if( toFile != null){
								try{
							      toFile.close();
								}catch(IOException ioe){}
							}
						}
					}
					
				}		
			}
		});
     //서명하는곳 초기화
       btnSignatureInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				freeCurveView.touchPoints.removeAll(freeCurveView.touchPoints);
				freeCurveView.destroyDrawingCache();
				freeCurveView.invalidate();
			}
		});
   }
	@Override
	public void onStart(){
		super.onStart();
		directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/signaturesImage/";
		File storeFile = new File(directory);
		if( !storeFile.exists()){
			storeFile.mkdirs();
		}
	}
}
