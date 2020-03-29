package com.health.loader;

import java.util.GregorianCalendar;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.health.main.HealthApplication;
import com.health.repository.IRepository;
import com.health.util.Utils;

public class FeelingTimeLoader extends AsyncTaskLoader<FeelingTimeInfo> {
	private IRepository mRepository;
	private GregorianCalendar mDateFrom;
	private GregorianCalendar mDateTo;
	
	public FeelingTimeLoader(Context context, GregorianCalendar dtFrom, GregorianCalendar dtTo) {
		super(context);	
		this.mDateFrom = dtFrom;
		this.mDateTo = dtTo;
		this.mRepository = ((HealthApplication) context.getApplicationContext()).getRepository();
	}

	@Override
	public FeelingTimeInfo loadInBackground() {
		FeelingTimeInfo result = new FeelingTimeInfo();
		result.FeelingTimeInfo = mRepository.getFeelingGroups(this.mDateFrom.getTime(), Utils.toMaxRoundDate(this.mDateTo.getTime()).getTime());
		return result;		
	} 
}
