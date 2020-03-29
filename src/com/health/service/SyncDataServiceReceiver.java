package com.health.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SyncDataServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, SyncDataService.class);
		context.startService(service);
	}

}
