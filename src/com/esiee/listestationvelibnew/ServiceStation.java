package com.esiee.listestationvelibnew;

import velib.model.InfoStation;
import velib.model.StationVelib;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class ServiceStation extends Service {
	ChargeurInfoStation ch = new ChargeurInfoStation();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		int numStation = intent.getIntExtra("numStation", 0);
		int periode = intent.getIntExtra("periode", 10);

		ch.execute(numStation, periode);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		ch.cancel(true);
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void showNotification(CharSequence contentTitle,
			CharSequence contentText) {
		Notification notification = new Notification(R.drawable.accepted,
				"counter", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, null, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(R.string.hello_world, notification);
	}

	private class ChargeurInfoStation extends AsyncTask<Integer, Void, Void> {
		private InfoStation inf;
		private int available, free;

		@Override
		protected Void doInBackground(Integer... params) {
			while (!isCancelled()) {
				try {
					inf = new InfoStation(params[0]);
					available = inf.getAvailable();
					free = inf.getFree();
					Log.i("Available", "Available : " + available + ", FREE : "
							+ free);
					SystemClock.sleep(1000 * params[1]);
					showNotification("NOTIF", "<" + String.valueOf(available)
							+ "," + String.valueOf(free) + ">");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
	}
}