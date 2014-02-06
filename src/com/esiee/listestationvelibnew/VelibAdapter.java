package com.esiee.listestationvelibnew;

import velib.model.ListeDesStationsVelib;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VelibAdapter extends BaseAdapter {
	private Context ctxt;
	private ListeDesStationsVelib list;

	public VelibAdapter(Context ctxt, ListeDesStationsVelib list) {
		this.ctxt = ctxt;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.lesNomsDesStations().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.lesNomsDesStations().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	private static class CacheView {
		public TextView nom;
		public ImageView image;
		public TextView lieu;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Log.i("getview: ",arg1+" "+arg2);
		View itemView = arg1;

		if (arg1 == null) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = null;
			inflater = (LayoutInflater) ctxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.velib, arg2, false);
			CacheView cache = new CacheView();
			cache.nom = (TextView) itemView.findViewById(R.id.nomId);
			cache.lieu = (TextView) itemView.findViewById(R.id.lieuId);
			cache.image = (ImageView) itemView.findViewById(R.id.imageId);
			itemView.setTag(cache);
		} else {
			ListeStationVelibActivity.cmpt++;
		}
		CacheView cache = (CacheView) itemView.getTag();
		cache.nom
				.setText(list.lireStation(getItem(arg0).toString()).toString());
		cache.lieu.setText((list.lireStation(getItem(arg0).toString())
				.getFullAddress()));
		Boolean dispo = list.lireStation(getItem(arg0).toString()).getOpen();
		if (dispo)
			cache.image.setImageResource(R.drawable.accepted);
		else
			cache.image.setImageResource(R.drawable.cancel);
		return itemView;
	}

}