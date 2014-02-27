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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PhotoActivity extends Activity {
	TableLayout table;
	TableRow row;
	ProgressDialog progressDialog;
	LayoutParams imageLayoutParams;
	int count =1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		ArrayList<String> urls = new ArrayList<String>();
		urls.add(getResources().getString(R.string.commencement_main_image));
		urls.add(getResources().getString(R.string.football_main_image));
		urls.add(getResources().getString(R.string.ifest_main_image));
		urls.add(getResources().getString(R.string.uncc_main_image));

		progressDialog = new ProgressDialog(this);
		progressDialog.setMax(urls.size());
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Loading..");
		table = (TableLayout) findViewById(R.id.table1);
		imageLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		
		findViewById(R.id.exitButton).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
        		//overridePendingTransition(R.animator.slide, R.animator.slide_out); 

			}
			
		});
		
		new AsyncTask<String, Bitmap, Void>() {
			
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

				ImageView imageViewToBeAdded = new ImageView(getBaseContext());
				imageViewToBeAdded.setLayoutParams(imageLayoutParams);
				imageViewToBeAdded.setPadding(5, 5, 5, 5);
				imageViewToBeAdded.setImageBitmap(values[0]);
				
				//trying different things
//				for(int i = 0; i<2; i++){
//					
//					row = new TableRow(PhotoActivity.this);
//					row.setLayoutParams(new TableRow.LayoutParams(50, 50));
//
//					for (int j = 0; j<2; j++){
//						ImageView imageViewToBeAdded = new ImageView(getBaseContext());
//						imageViewToBeAdded.setLayoutParams(imageLayoutParams);
//						imageViewToBeAdded.setPadding(5, 5, 5, 5);
//						imageViewToBeAdded.setImageBitmap(values[0]);
//						row.addView(imageViewToBeAdded);
//					}
//					table.addView(row);
//					
//				}

				
				//row.addView(imageViewToBeAdded, new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				//table.addView(row, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				table.addView(imageViewToBeAdded);
				progressDialog.incrementProgressBy(1);
				count++;
			}

			@Override
			protected Void doInBackground(String... params) {
				String[] imgUrls = params;
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
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_thread, menu);
		return true;
	}

}