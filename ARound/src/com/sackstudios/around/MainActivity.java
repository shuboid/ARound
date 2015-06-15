package com.sackstudios.around;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class MainActivity extends ActionBarActivity implements LocationListener {

	public String API_KEY = "AIzaSyBVTv1hXh9zyGj23VPur3k-_t0jfDEoiao";
	public String Radius = "1500";
	double lati;
	double longi;
	LocationManager locationManager;

	LinearLayout rl ;
	
	boolean isLocationAvailable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_selection_layout);

		rl = (LinearLayout) findViewById(R.id.ll);
		//rl.setActivated(true);
		rl.setVisibility(View.VISIBLE);
		/*
		 * locationManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE);
		 * 
		 * locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		 * 0, 0, this);
		 */

		if (isConnected()) {

			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// txtLat = (TextView) findViewById(R.id.textview1);
		// txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:"
		// + location.getLongitude());
		if (!isLocationAvailable) {
			lati = location.getLatitude();
			longi = location.getLongitude();
			isLocationAvailable = true;
		}
		// Toast.makeText(getApplicationContext(),
		// ""+location.getLatitude()+"\n"+location.getLongitude(), 1).show();

	}

	public void placeType(View v) {
		if (isLocationAvailable && isConnected()) {
			rl.setVisibility(View.INVISIBLE);
			TextView tv_cat = (TextView) v;
			String cat = tv_cat.getText().toString();
			
			String latitude = "18.621526";
			String longitude = "73.801664";
			Toast.makeText(getApplicationContext(), "" + lati + "\n" + longi, 1)
					.show();

			String req = "https://maps.googleapis.com/maps/api/place/search/json?location="
					+ ""
					+ lati
					+ ","
					+ ""
					+ longi
					+ "&radius=3000&types="
					+ cat + "&key=" + API_KEY;

			String req2 = "https://maps.googleapis.com/maps/api/place/search/json?location="
					+ ""
					+ latitude
					+ ","
					+ ""
					+ longitude
					+ "&radius=3000&types=" + cat + "&key=" + API_KEY;
			Log.d("1111", req);
			Log.d("2222", req2);
			
			GetPlaceAsynTask gpat = new GetPlaceAsynTask();
			gpat.execute(req);
		} else {
			Toast.makeText(getApplicationContext(), "geting location wait", 1)
					.show();
		}
	}

	// police
	class GetPlaceAsynTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d("##", "fail");
			String x = "";
			x += HttpManager.getData(params[0]);

			return x;
		}

		@Override
		protected void onPostExecute(String result) {
			// tv.setText(result);
			// Log.d("%%%%", result);
			rl.setVisibility(View.VISIBLE);
			
			ArrayList<PlaceClass> ps = new ArrayList<PlaceClass>();

			ps = PlacesParcer.parseGoogleParse(result);

			// Toast.makeText(getApplicationContext(), ps.get(0).getLongitude(),
			// 1)
			// .show();
			String xx = "";
			for (PlaceClass placeClass : ps) {
				xx += placeClass.getName() + ";" + placeClass.getLatitude()
						+ ";" + placeClass.getLongitude() + ";" + "#";
			}

			Intent intent = new Intent(MainActivity.this,
					com.raw.arview.ARView.class);
			intent.putExtra("loc", xx);
			intent.putExtra("latitude", lati);
			intent.putExtra("longitude", longi);
			startActivity(intent);

			super.onPostExecute(result);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("Latitude", "disable");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "enable");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "status");
	}

	public boolean isConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) (MainActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		boolean isConnected = networkInfo != null
				&& networkInfo.isConnectedOrConnecting();

		boolean wiw = false;
		
		NetworkInfo nwifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		wiw = nwifi.isConnectedOrConnecting();
		
		return isConnected || wiw;

	}
}
