package com.health.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.health.data.DaoSession;
import com.health.data.DownloadPeriod;
import com.health.data.DownloadPeriodDao;
import com.health.data.User;

public class DownloadPeriodDB {
	
	public static final int CACHE_LIVETIME_SEC = 15 * 60;
	
	public static List<DownloadPeriod> getDownloadPeriods(Date dtFrom, Date dtTo){
		Date nowDate = new Date();
		List<DownloadPeriod> result = new ArrayList<DownloadPeriod>();
		
		User currentUser = UserDB.getCurrentUser();
		long userId = currentUser.getId();
		long cityId = currentUser.getCityId();
		long dtUnixFrom = dtFrom.getTime()/1000L;
		long dtUnixTo = dtTo.getTime()/1000L;
		if(DownloadPeriod.START_PERIOD_UNIX_TIME > dtUnixFrom){
			dtUnixFrom = DownloadPeriod.START_PERIOD_UNIX_TIME;
		}
		if(DownloadPeriod.END_PERIOD_UNIX_TIME < dtUnixTo){
			dtUnixTo = DownloadPeriod.END_PERIOD_UNIX_TIME;
		}
		dtUnixFrom = DownloadPeriod.START_PERIOD_UNIX_TIME + ((dtUnixFrom - DownloadPeriod.START_PERIOD_UNIX_TIME)/DownloadPeriod.DEFAULT_DOWNLOAD_PERIOD_SEC) * DownloadPeriod.DEFAULT_DOWNLOAD_PERIOD_SEC;
		dtUnixTo = DownloadPeriod.START_PERIOD_UNIX_TIME + ((dtUnixTo - DownloadPeriod.START_PERIOD_UNIX_TIME)/DownloadPeriod.DEFAULT_DOWNLOAD_PERIOD_SEC + 1) * DownloadPeriod.DEFAULT_DOWNLOAD_PERIOD_SEC;
		DaoSession daoSession = DB.db().newSession();
		List<DownloadPeriod> downloadedPeriods = daoSession.getDownloadPeriodDao().queryBuilder().where(DownloadPeriodDao.Properties.CityId.eq(cityId), DownloadPeriodDao.Properties.DateTo.ge(new Date(dtUnixFrom * 1000L)), DownloadPeriodDao.Properties.DateFrom.le(new Date(dtUnixTo * 1000L))).orderAsc(DownloadPeriodDao.Properties.DateFrom).list();
				
		int searchIndex = 0;
		while (dtUnixFrom < dtUnixTo) {
            boolean isAddPeriod = true;
            DownloadPeriod dbPeriod = null;
            for(int i = searchIndex; i < downloadedPeriods.size(); i++){
            	if((downloadedPeriods.get(i).getDateFrom().getTime()/1000L) == dtUnixFrom){
            		if((nowDate.getTime() - downloadedPeriods.get(i).getSyncDate().getTime()) > CACHE_LIVETIME_SEC * 1000L || downloadedPeriods.get(i).getDateTo().after(nowDate)){
            			isAddPeriod = true;
            			dbPeriod = downloadedPeriods.get(i);
            		} else {
            			isAddPeriod = false;
            		}
            		searchIndex = i + 1;
            		break;
            	}
            }
            if(isAddPeriod){
            	if(dbPeriod == null){
            		DownloadPeriod period = new DownloadPeriod();
	            	period.setCityId(cityId);
	            	period.setUserId(userId);
	            	period.setDateFrom(new Date(dtUnixFrom * 1000L));
	            	period.setDateTo(new Date(dtUnixFrom * 1000L + DownloadPeriod.DEFAULT_DOWNLOAD_PERIOD_SEC * 1000L));
	            	period.setSyncDate(null);
	            	period.setTypeId(DownloadPeriod.COMMON_DATA_TYPE);
	            	result.add(period);
            	} else {
            		result.add(dbPeriod);
            	}
            	
            }
            dtUnixFrom += DownloadPeriod.DEFAULT_DOWNLOAD_PERIOD_SEC;
        }		
		return result;
	}
	
	public static DownloadPeriod saveDownloadPeriod(DownloadPeriod downloadPeriod){
		DaoSession daoSession = DB.db().newSession();
		DownloadPeriodDao downloadPeriodDao = daoSession.getDownloadPeriodDao();
		downloadPeriodDao.insertOrReplace(downloadPeriod);
		return downloadPeriod;
	}
	
	
			
	

}
