package com.health.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.health.data.BaseDTO;
import com.health.data.BodyFeeling;
import com.health.data.CommonFeeling;
import com.health.data.DaoSession;
import com.health.data.Factor;
import com.health.db.DB;
import com.health.repository.IRepository;

public class HistoryBodyFeelingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private TextView tvNoData;
	private ListView lvBodyFeelings;
	
	private IRepository mRepository;
	private OnChangeBodyFeelingListener mOnChangeBodyFeelingListener;

	public static final int CONTEXTMENU_EDIT_ID = 1;
	public static final int CONTEXTMENU_DELETE_ID = 2;
	
	private int mFeelingTypeId;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mRepository = ((HealthApplication) activity.getApplication()).getRepository();
		this.mOnChangeBodyFeelingListener = (OnChangeBodyFeelingListener) activity;
	}	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mFeelingTypeId = getActivity().getIntent().getIntExtra(HistoryBodyFeelingActivity.EXTRA_FEELINGTYPE_ID, HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID);
		this.mFeelingTypeId = getActivity().getIntent().getExtras().getInt(HistoryBodyFeelingActivity.EXTRA_FEELINGTYPE_ID, HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID);
		View view = inflater.inflate(R.layout.history_bodyfeeling_fragment, null);
		this.lvBodyFeelings = (ListView) view.findViewById(R.id.lvBodyFeelings);
		this.tvNoData = (TextView) view.findViewById(R.id.tvNoData);
		loadBodyFeelingsInListView();		
		getLoaderManager().initLoader(BODYFEELING_LOADER, getActivity().getIntent().getExtras(), this).forceLoad();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void loadBodyFeelingsInListView() {
		
		if(mFeelingTypeId == HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID){
			String[] uiBindFrom = { "StartDate", "organ", "name" };
			int[] uiBindTo = { R.id.tvDate, R.id.tvOrgan, R.id.tvBodyFeelingName };
			this.mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_bodyfeeling, null, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);			
			this.lvBodyFeelings.setAdapter(this.mAdapter);
		}
		if(mFeelingTypeId == HistoryBodyFeelingActivity.COMMONFEELING_TYPE_ID){
			String[] uiBindFrom = { "StartDate", "groupname", "name" };
			int[] uiBindTo = { R.id.tvDate, R.id.tvGroupName, R.id.tvName };
			this.mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_commonfeeling, null, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			this.lvBodyFeelings.setAdapter(this.mAdapter);
		}
		if(mFeelingTypeId == HistoryBodyFeelingActivity.FACTOR_TYPE_ID){
			String[] uiBindFrom = { "StartDate", "groupname", "name" };
			int[] uiBindTo = { R.id.tvDate, R.id.tvGroupName, R.id.tvName };
			this.mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_factor, null, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			this.lvBodyFeelings.setAdapter(this.mAdapter);
		}
		this.mAdapter.setViewBinder(new DateViewBinder());
		
		this.lvBodyFeelings.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);		
		this.lvBodyFeelings.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {				
				final int checkedCount = lvBodyFeelings.getCheckedItemCount();				
				mode.setTitle(String.format(getString(R.string.history_feeling_delete_title), checkedCount));				
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.delete:
					long[] ids = lvBodyFeelings.getCheckedItemIds();
					for(long id : ids){
						if(mFeelingTypeId == HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID){
			    			BodyFeeling deleteBodyFeeling = mRepository.getBodyFeelingById(getActivity(), (int) id);
			    			mOnChangeBodyFeelingListener.onDeleteBodyFeeling(deleteBodyFeeling);    			
						}
						if(mFeelingTypeId == HistoryBodyFeelingActivity.COMMONFEELING_TYPE_ID){
							CommonFeeling deleteCommonFeeling = mRepository.getCommonFeelingById(getActivity(), (int) id);
							mOnChangeBodyFeelingListener.onDeleteCommonFeeling(deleteCommonFeeling);		
						}
						if(mFeelingTypeId == HistoryBodyFeelingActivity.FACTOR_TYPE_ID){
							Factor deleteFactor = mRepository.getFactorById(getActivity(), (int) id);
							mOnChangeBodyFeelingListener.onDeleteFactor(deleteFactor);		
						}
					}
					mOnChangeBodyFeelingListener.onCompleteChangeUserData();
					getLoaderManager().initLoader(BODYFEELING_LOADER, getActivity().getIntent().getExtras(), HistoryBodyFeelingFragment.this).forceLoad();
					mode.finish();
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.history_feeling_menu, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {				
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {				
				return false;
			}
		});
				
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		switch (v.getId()) {
		case R.id.lvBodyFeelings:
			menu.add(0, CONTEXTMENU_DELETE_ID, 0, getResources().getString(R.string.delete));
			break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();		
		long id = this.mAdapter.getItemId(menuInfo.position);		
		switch (item.getItemId()) {
		case CONTEXTMENU_DELETE_ID:
			if(mFeelingTypeId == HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID){
    			BodyFeeling deleteBodyFeeling = mRepository.getBodyFeelingById(this.getActivity(), (int) id);
    			this.mOnChangeBodyFeelingListener.onDeleteBodyFeeling(deleteBodyFeeling);    			
			}
			if(mFeelingTypeId == HistoryBodyFeelingActivity.COMMONFEELING_TYPE_ID){
				CommonFeeling deleteCommonFeeling = mRepository.getCommonFeelingById(this.getActivity(), (int) id);
				this.mOnChangeBodyFeelingListener.onDeleteCommonFeeling(deleteCommonFeeling);		
			}
			if(mFeelingTypeId == HistoryBodyFeelingActivity.FACTOR_TYPE_ID){
				Factor deleteFactor = mRepository.getFactorById(this.getActivity(), (int) id);
				this.mOnChangeBodyFeelingListener.onDeleteFactor(deleteFactor);
			}
			mOnChangeBodyFeelingListener.onCompleteChangeUserData();
			getLoaderManager().initLoader(BODYFEELING_LOADER, getActivity().getIntent().getExtras(), this).forceLoad();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private static final int BODYFEELING_LOADER = 0x01;

	private SimpleCursorAdapter mAdapter;

	static class OrmLiteLoader extends CursorLoader {
		
		private int mFeelingTypeId;

		public OrmLiteLoader(Context context, int feelingTypeId) {
			super(context);
			this.mFeelingTypeId = feelingTypeId;
		}

		@Override
		public Cursor loadInBackground() {
			DaoSession daoSession = DB.db().newSession();
			Cursor cursor = null;
			if(this.mFeelingTypeId == HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID){
				cursor = daoSession.getDatabase().rawQuery("select b.id as _id, b.id, b.dt as StartDate, region.fullname as organ, (case when type.Name is null THEN ctype.Name ELSE type.Name end) as name from tblbodyfeeling b left join tblbodyfeelingtype type on type.id=b.feelingtypeid left join tblcustombodyfeelingtype ctype on ctype.id=b.customfeelingtypeid left join tblbodyregion region on region.id = b.bodyregionid order by b.dt", null);
			}
			if(this.mFeelingTypeId == HistoryBodyFeelingActivity.COMMONFEELING_TYPE_ID){
				cursor = daoSession.getDatabase().rawQuery("select c.id as _id, c.id, c.dt as StartDate, g.name as groupname,(case when type.Name is null THEN ctype.Name ELSE type.Name end)||'    ' ||(case when u.id = 2 then c.value1 when u.id = 3 then c.value1  || '/' || c.value2  else '' end) as name from tblcommonfeeling c left join tblcommonfeelingtype type on type.id=c.feelingtypeid left join tblcustomcommonfeelingtype ctype on ctype.id=c.customfeelingtypeid inner join tblcommonfeelinggroup g on g.id = type.feelinggroupid or g.id = ctype.feelinggroupid left join tblunitdimension u on u.id=type.unitid or u.id=ctype.unitid order by c.dt", null);
			}
			if(this.mFeelingTypeId == HistoryBodyFeelingActivity.FACTOR_TYPE_ID){				
				cursor = daoSession.getDatabase().rawQuery("select f.id as _id, f.id, f.dt as StartDate, g.name as groupname,(case when type.Name is null THEN ctype.Name ELSE type.Name end)||'    ' ||(case when u.id = 2 then f.value1 when u.id = 3 then f.value1  || '/' || f.value2  else '' end) as name from tblfactor f left join tblfactortype type on type.id=f.factortypeid left join tblcustomfactortype ctype on ctype.id=f.customfactortypeid inner join tblfactorgroup g on g.id = type.factorgroupid or  g.id = ctype.factorgroupid left join tblunitdimension u on u.id=type.unitid or u.id=ctype.unitid order by f.dt", null);
			}			
			return cursor;
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		return new OrmLiteLoader(getActivity().getBaseContext(), bundle.getInt(HistoryBodyFeelingActivity.EXTRA_FEELINGTYPE_ID));
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		if(this.mAdapter.getCount() == 0){
			this.tvNoData.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
	
	private class DateViewBinder implements SimpleCursorAdapter.ViewBinder {
		
		SimpleDateFormat sdfDateTime = new SimpleDateFormat(BaseDTO.DATETIME_FORMAT);
		 
	    @Override
	    public boolean setViewValue(View view, Cursor cursor, int index) {
	    	if (index == cursor.getColumnIndex("StartDate")) {	            
	            long date = cursor.getLong(index);
	            Date dateObj = new Date(date);
	            ((TextView) view).setText(sdfDateTime.format(dateObj));
	            return true;
	        } else {
	            return false;
	        }
	    }
	}

}