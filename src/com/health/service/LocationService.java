package com.health.service;

import java.util.Date;

import com.health.data.User;
import com.health.db.UserDB;
import com.health.main.R;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
	
	public static final String EXTRA_STATUS = "EXTRA_STATUS";
	public static final String EXTRA_LAT = "LAT";
	public static final String EXTRA_LNG = "LNG";
	public static final String EXTRA_DISTANCE = "DISTANCE";

	public static final String TAG = LocationService.class.getName();
	
	public static final String CALLBACK_INTENT = "CALLBACK_INTENT";

	public static final long LIMIT_DISTANCE_IN_METERS = 5000;

	public static final long INTERVAL_TIME_IN_HOUR = 6;

	private LocationManager locationManager;

	private User mCurrentUser = null;

	private Date mPrevNotificationDate = null;

	private int mDistance;
	
	private Location mLocation = null;

	public static final int MSG_GPS_GET = 1;
	
	public static final int STATUS_OK = 1;
	public static final int STATUS_UNDEFINED = 2;

	/*class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			startOperation(msg);
		}
	}*/

	LooperThread mLooperThread;

	class LooperThread extends Thread {
		
		public Handler mHandler;

		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case MSG_GPS_GET:
						getCurrentGPS();
						break;
					default:
						super.handleMessage(msg);
					}
				}
			};
			synchronized (this) {
				this.notifyAll();
            }
			Looper.loop();
		}
	}
	
	private void getCurrentGPS(){
		Intent intent = new Intent(LocationService.CALLBACK_INTENT);
		if(mLocation != null){			
			intent.putExtra(EXTRA_STATUS, STATUS_OK);
			intent.putExtra(EXTRA_LAT, mLocation.getLatitude());
			intent.putExtra(EXTRA_LNG, mLocation.getLongitude());
			intent.putExtra(EXTRA_DISTANCE, mDistance);
		} else {
			intent.putExtra(EXTRA_STATUS, STATUS_UNDEFINED);			
		}
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
	}

	public void startOperation(Message msg) {
		if (mLooperThread.mHandler != null) {
			Message newMsg = mLooperThread.mHandler.obtainMessage(msg.what, msg.arg1, msg.arg2, msg.obj);
			mLooperThread.mHandler.sendMessage(newMsg);
		}
	}

	//final Messenger mMessenger = new Messenger(new IncomingHandler());
	Messenger mMessenger = null;

	@Override
	public IBinder onBind(Intent intent) {		
		return mMessenger.getBinder();
	}

	public LocationService() {
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mCurrentUser = UserDB.getCurrentUser();
		mLooperThread = new LooperThread();
		mLooperThread.start();
		synchronized (mLooperThread) {
			try {
		        mLooperThread.wait();
	        } catch (InterruptedException e) {	        
		        e.printStackTrace();
	        }
			mMessenger = new Messenger(mLooperThread.mHandler);
        }
		Log.i(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, mListener);
		}
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, mListener);
		}		
		Log.i(TAG, "onStartCommand");
		return START_STICKY;
	}

	private LocationListener mListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			Date nowDate = new Date();
			if (location == null) {
				return;
			}
			mLocation = location;
			float[] results = new float[1];
			Location.distanceBetween(location.getLatitude(), location.getLongitude(), mCurrentUser.getCity().getLat(), mCurrentUser.getCity().getLng(), results);
			mDistance = (int) results[0];
			getCurrentGPS();
			if (results[0] > LIMIT_DISTANCE_IN_METERS && (mPrevNotificationDate == null || (nowDate.getTime() - mPrevNotificationDate.getTime()) > INTERVAL_TIME_IN_HOUR * 60 * 60 * 1000)) {
				// Notification for user
				String message = String.format("Информация о погоде может быть искажена, т.к. вы находитесь на большом расстоянии от вашего города(%f км.)", results[0] / 1000);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext()).setContentTitle(getString(R.string.app_name)).setContentText(message)
				        .setSmallIcon(R.drawable.heart).setStyle(new NotificationCompat.BigTextStyle().bigText(message));
				int mNotificationId = 1;
				NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotifyMgr.notify(mNotificationId, mBuilder.build());
				mPrevNotificationDate = new Date();
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}

	};

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		mLooperThread.mHandler.getLooper().quit();
		super.onDestroy();
	}

	public static boolean isConnectingToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

}
