package edu.uncc.threading;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PhotoThread extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_thread);
		
		//currently extends hello world
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_thread, menu);
		return true;
	}

}