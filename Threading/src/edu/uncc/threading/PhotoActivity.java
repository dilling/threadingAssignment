package edu.uncc.threading;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;

public class PhotoActivity extends Activity {
	TableLayout table;
	ProgressDialog progressDialog;
	LayoutParams imageLayoutParams;

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
		table = (TableLayout) findViewById(R.id.table1);
		imageLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		
		findViewById(R.id.exitButton).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
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
				table.addView(imageViewToBeAdded);
				progressDialog.incrementProgressBy(1);
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