package com.esiee.listestationvelibnew;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import velib.model.*;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListeStationVelibActivity extends ListActivity {

	private ListeDesStationsVelib stations;
	public static int cmpt;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cmpt = 0;
		setContentView(R.layout.liste); // par défaut cf. le cours
		try {
			URL url = new URL("http://www.velib.paris.fr/service/carto");
			// Paramètre donné à titre indicatif
			new ChargeurListeDesStationsURL().execute(url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				InputStream is = getAssets().open("stations.xml");
				// Paramètre donné à titre indicatif
				new ChargeurListeDesStations().execute(is);
			} catch (Exception e1) {
				Toast.makeText(
						this,
						"execptions : " + e.getMessage() + ", "
								+ e1.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		}

	}

	//
	// ChargeurListeDesStations est une classe interne et hérite de AsyncTask
	//
	// onPreExecute() --> ProgressDialog.show ...
	// doInBackground --> Lecture depuis XML
	// onPostExecute --> Un toast et affectation de la liste
	//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.liste_station_velib, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intento;
		switch (item.getItemId()) {

		case R.id.item1: { // start service
			if (!isServiceStarted()) {
				intento = new Intent(ListeStationVelibActivity.this,
						ServiceStation.class);
				intento.putExtra("numStation", getPreferences(MODE_PRIVATE)
						.getInt("numStation", 0));
				intento.putExtra("periode", getPreferences(MODE_PRIVATE)
						.getInt("periode", 10));
				Toast.makeText(this,"Starting the service", Toast.LENGTH_SHORT)
				.show();
				startService(intento);
			} else {
				Toast.makeText(this,"Service already started", Toast.LENGTH_SHORT)
				.show();
			}
		}
			return true;

		case R.id.item2: { // stop service
			Toast.makeText(this,"Service stopped", Toast.LENGTH_SHORT)
			.show();
			stopService(new Intent(ListeStationVelibActivity.this,ServiceStation.class));
		}
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}

	}

	private class ChargeurListeDesStations extends
			AsyncTask<InputStream, Void, Boolean> {
		private ProgressDialog dialog;
		private Context thiss = ListeStationVelibActivity.this;

		protected void onPreExecute() {
			dialog = ProgressDialog
					.show(thiss, "Patience", "Ca va commencer !");
		}

		@Override
		protected Boolean doInBackground(InputStream... arg0) {

			try {
				stations = new ListeDesStationsVelib();
				stations.chargerDepuisXML(arg0[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// super.onPostExecute(result);
			Toast.makeText(thiss, "Chargement fini", Toast.LENGTH_LONG).show();
			/*
			 * setListAdapter(new ArrayAdapter<String>(thiss,
			 * android.R.layout.simple_list_item_1,
			 * stations.lesNomsDesStations()));
			 */
			setListAdapter(new VelibAdapter(thiss, stations));
			dialog.dismiss();
		}

	}

	private class ChargeurListeDesStationsURL extends
			AsyncTask<URL, Void, Boolean> {
		private ProgressDialog dialog;
		private Context thiss = ListeStationVelibActivity.this;

		protected void onPreExecute() {
			dialog = ProgressDialog
					.show(thiss, "Patience", "Ca va commencer !");
		}

		@Override
		protected Boolean doInBackground(URL... arg0) {

			try {
				stations = new ListeDesStationsVelib();
				stations.chargerDepuisXML(arg0[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// super.onPostExecute(result);
			Toast.makeText(thiss, "Chargement fini", Toast.LENGTH_LONG).show();
			/*
			 * setListAdapter(new ArrayAdapter<String>(thiss,
			 * android.R.layout.simple_list_item_1,
			 * stations.lesNomsDesStations()));
			 */
			setListAdapter(new VelibAdapter(thiss, stations));
			dialog.dismiss();
		}

	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// à compléter en question 1-2
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			new ChargeurInfoStation().execute(stations.lireStation(l
					.getItemAtPosition(position).toString()));

			// Intent intent = new Intent(this, ServiceStation.class);
			// intent.putExtra(
			// "numStation",
			// stations.lireStation(
			// l.getItemAtPosition(position).toString())
			// .getNumber());
			// intent.putExtra("periode", 5);
			// startService(intent);
			getPreferences(MODE_PRIVATE)
					.edit()
					.putInt("numStation",
							stations.lireStation(
									l.getItemAtPosition(position).toString())
									.getNumber()).commit();
			getPreferences(MODE_PRIVATE).edit().putInt("periode", 5).commit();
		} catch (Exception e1) {
			Toast.makeText(this, "COCO : " + e1.getMessage(), Toast.LENGTH_LONG)
					.show();
			e1.printStackTrace();
		}
		// à compléter en question 1-2
	}

	// à compléter en question 1-2

	// ChargeurInfoStation est une classe interne et hérite de AsyncTask
	//
	// onPreExecute() --> ProgressDialog.show ...
	// doInBackground --> Lecture depuis XML
	// onPostExecute --> Un toast et démarrage de l'activité avec les bons
	// paramètres
	//

	private class ChargeurInfoStation extends
			AsyncTask<StationVelib, Void, Void> {
		private ProgressDialog dialog2;
		private Context thiss = ListeStationVelibActivity.this;
		private Double latitude, longitude;
		private InfoStation inf;
		private int available, free;

		protected void onPreExecute() {
			dialog2 = ProgressDialog.show(thiss, "Info Station", "Patientez");
		}

		@Override
		protected Void doInBackground(StationVelib... params) {

			try {
				inf = new InfoStation(params[0]);
				latitude = params[0].getLatitude();
				longitude = params[0].getLongitude();
				available = inf.getAvailable();
				free = inf.getFree();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void results) {
			dialog2.dismiss();

			Intent myIntent = new Intent(ListeStationVelibActivity.this,
					InfoStationActivity.class);
			myIntent.putExtra("latitude", latitude);
			myIntent.putExtra("longitude", longitude);
			myIntent.putExtra("available", available);
			myIntent.putExtra("free", free);
			// Toast.makeText(thiss, "Recyclage " + cmpt,
			// Toast.LENGTH_SHORT).show();

			startActivity(myIntent);

		}
	}

	public boolean isServiceStarted() {
		boolean res = false;
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		List<RunningServiceInfo> services = am
				.getRunningServices(Integer.MAX_VALUE);
		for (RunningServiceInfo r : services) {
			String name = getBaseContext().getPackageName();
			if (name.equals(r.service.getPackageName())) {
				String nameService = getBaseContext().getPackageName()
						+ ".ServiceStation";
				if (nameService.equals(r.service.getClassName())) {
					return true;
				}
			}
		}

		return res;
	}
}