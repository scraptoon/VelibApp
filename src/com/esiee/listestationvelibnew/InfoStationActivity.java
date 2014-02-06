package com.esiee.listestationvelibnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

public class InfoStationActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent myIntent = getIntent();
		Double longitude = myIntent.getDoubleExtra("longitude", 0);
		Double latitude = myIntent.getDoubleExtra("latitude", 0);
		int available = myIntent.getIntExtra("available", 0);
		int free = myIntent.getIntExtra("free", 0);

		// à compléter
		// Toast.makeText(InfoStationActivity.this, "coco", Toast.LENGTH_LONG);
		Toast.makeText(InfoStationActivity.this,
				"latitude:" + latitude + " , longitude: " + longitude,
				Toast.LENGTH_SHORT).show();
		Toast.makeText(InfoStationActivity.this,
				"<" + available + " , " + free + ">", Toast.LENGTH_SHORT).show();

		finish();
	}

}