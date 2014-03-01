package edu.uncc.threading;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;

public class PhotoActivity extends Activity {
	
	//Object Setup
	GridView grid;
	ProgressDialog progressDialog;
	LayoutParams imageLayoutParams;
	ArrayList<String> urls;
	String[] imgUrls;
	TableLayout table;
		//TableRow row;
	static final int PICS_PER_ROW = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		//Setup ArrayList of URLs to Parse
		urls = new ArrayList<String>();
		urls.add(getResources().getString(R.string.commencement_main_image));
		urls.add(getResources().getString(R.string.football_main_image));
		urls.add(getResources().getString(R.string.ifest_main_image));
		urls.add(getResources().getString(R.string.uncc_main_image));

		progressDialog = new ProgressDialog(this);
		progressDialog.setMax(urls.size());
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Loading..");
	
		table = (TableLayout) findViewById(R.id.table1);
		//row = (TableRow) findViewById(R.id.tableRow1);
		imageLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
		
		//Exit Button Setup
		findViewById(R.id.exitButton).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
        		//overridePendingTransition(R.animator.slide, R.animator.slide_out); 

			}
			
		});
		
		//Begin Image Downloading and Displaying
		new AsyncTask<String, Bitmap, Void>() {
			
			TableRow tableRow = new TableRow(getBaseContext());
			
			@Override
			protected void onPostExecute(Void result) {
				progressDialog.dismiss();
			}

			@Override
			protected void onPreExecute() {
				progressDialog.show();
			}

			@Override
			protected void onProgressUpdate(Bitmap... values) {
				if (values[0] == null) {
					values[0] = ((BitmapDrawable) getResources().getDrawable(
							R.drawable.not_found)).getBitmap();
				}
				
				//ImageView imageViewToBeAdded = (ImageView) findViewById(R.id.imageView1);
				ImageView imageViewToBeAdded = new ImageView(getBaseContext());
				imageViewToBeAdded.setLayoutParams(imageLayoutParams);
				imageViewToBeAdded.setPadding(5, 5, 5, 5);
				imageViewToBeAdded.setImageBitmap(values[0]);
				Log.d("David", Integer.toString(tableRow.getVirtualChildCount()));
				tableRow.addView(imageViewToBeAdded);
				if(tableRow.getVirtualChildCount()==PICS_PER_ROW) {
					table.addView(tableRow);
					tableRow = new TableRow(getBaseContext());
				}
				progressDialog.incrementProgressBy(1);
			}//end of onProgressUpdate

			@Override
			protected Void doInBackground(String... params) {
				imgUrls = params;
				Bitmap image = null;
				for (String imgUrl : imgUrls) {
					try {
						URL url = new URL(imgUrl);
						image = BitmapFactory.decodeStream(url.openStream());
						publishProgress(image);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

		}.execute(urls.toArray(new String[0]));
		
	}//end of onCreate
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_thread, menu);
		return true;
	}

}