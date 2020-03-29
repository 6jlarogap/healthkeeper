package com.health.loader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.health.data.CommonFeelingGroup;
import com.health.data.CommonFeelingGroupDao;
import com.health.data.CommonFeelingType;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomFactorType;
import com.health.data.DaoSession;
import com.health.data.FactorGroup;
import com.health.data.FactorGroupDao;
import com.health.data.FactorType;
import com.health.data.IGridGroup;
import com.health.data.IGridItem;
import com.health.db.DB;
import com.health.main.HealthApplication;
import com.health.repository.IRepository;


public class BodyFeelingTypeLoader extends AsyncTaskLoader<BodyFeelingTypeData> {
	
	private IRepository mRepository = null;

	public BodyFeelingTypeLoader(Context context) {
		super(context);
		this.mRepository = ((HealthApplication)context.getApplicationContext()).getRepository();
	}

	@Override
	public BodyFeelingTypeData loadInBackground() {
		BodyFeelingTypeData result = new BodyFeelingTypeData();
		initDictionaryDataFromDB(result);
		return result;
	}
	
	private void initDictionaryDataFromDB(BodyFeelingTypeData result) {
        LinkedHashMap<IGridGroup, List<IGridItem>> commonFeelingTypeHashMap = new LinkedHashMap<IGridGroup, List<IGridItem>>();
		List<CommonFeelingGroup> commonFeelingGroups = null;
		DaoSession daoSession = DB.db().newSession();
        
        commonFeelingGroups = daoSession.getCommonFeelingGroupDao().queryBuilder().orderAsc(CommonFeelingGroupDao.Properties.Id).list();
        for (CommonFeelingGroup group : commonFeelingGroups) {
            List<CommonFeelingType> list1 = mRepository.getCommonFeelingTypes(group.getId());
            for(CommonFeelingType commonFeelingType : list1){
            	commonFeelingType.setCommonFeelingGroup(group);
            }
            List<CustomCommonFeelingType> list2 = mRepository.getCustomCommonFeelingTypes(group.getId());
            for(CustomCommonFeelingType customCommonFeelingType : list2){
            	customCommonFeelingType.setCommonFeelingGroup(group);
            }
            List<IGridItem> listCommonFeelingType = new ArrayList<IGridItem>();
            listCommonFeelingType.addAll(list1);
            listCommonFeelingType.addAll(list2);                
            commonFeelingTypeHashMap.put(group, listCommonFeelingType);
        }
        
        
        LinkedHashMap<IGridGroup, List<IGridItem>> factorTypeHashMap = new LinkedHashMap<IGridGroup, List<IGridItem>>();
		List<FactorGroup> factorGroups = null;        
        factorGroups = daoSession.getFactorGroupDao().queryBuilder().orderAsc(FactorGroupDao.Properties.Id).list();
        for (FactorGroup group : factorGroups) {               
            List<FactorType> list1 = mRepository.getFactorTypes(group.getId());
            for(FactorType factorType : list1){
            	factorType.setFactorGroup(group);
            }
            List<CustomFactorType> list2 = mRepository.getCustomFactorTypes(group.getId());
            for(CustomFactorType customFactorType : list2){
            	customFactorType.setFactorGroup(group);
            }
            List<IGridItem> listFactorType = new ArrayList<IGridItem>();
            listFactorType.addAll(list1);
            listFactorType.addAll(list2);                
            factorTypeHashMap.put(group, listFactorType);                
        }
        
        
        
        result.setCommonFeelingGroups(commonFeelingGroups);
        result.setCommonFeelingTypeHashMap(commonFeelingTypeHashMap);
        
        result.setFactorGroups(factorGroups);
        result.setFactorTypeHashMap(factorTypeHashMap);
    }   

}
