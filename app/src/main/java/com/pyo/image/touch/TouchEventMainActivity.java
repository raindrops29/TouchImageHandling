/*
 *  터치 이벤트 및 서피스뷰 관련 예제
 */
package com.pyo.image.touch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.TreeMap;

public class TouchEventMainActivity extends ListActivity {
    private TreeMap<String,Intent> actions = new TreeMap<String,Intent>(); 
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		prepareActivityList();

		String[] keys = actions.keySet().toArray(
				new String[actions.keySet().size()]);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, keys));
	}	
	private void addInsertItem(String actionName, Class<?> className){
		actions.put(actionName, new Intent(this, className));
	}
    private void prepareActivityList() {
    	addInsertItem("1.간단한터치이벤트", SimpleTouchEventActivity.class);
    	addInsertItem("2.멀티터치예제",MultiTouchActivity.class);
    	addInsertItem("3.곡선그리기", TouchEventCurveActivity.class);
    	addInsertItem("4.서피스뷰관련예제",SurfaceViewExtentionActivity.class);
    }
    @Override
	protected void onListItemClick(ListView lv, View v, int position, long id){
		String key = (String) lv.getItemAtPosition(position);
		startActivity(actions.get(key));
	}
}