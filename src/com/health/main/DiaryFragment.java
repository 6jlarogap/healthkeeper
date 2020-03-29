package com.health.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.health.data.BaseDTO;
import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
import com.health.data.CommonFeeling;
import com.health.data.IFeelingTypeInfo;
import com.health.data.User;
import com.health.db.UserDB;
import com.health.dialog.PeriodDateDialogFragment;
import com.health.dialog.SaveFeelingDialogFragment;
import com.health.plot.XYPlotZoomPan;
import com.health.repository.IRepository;
import com.health.settings.Settings;
import com.health.view.BodyFeelingPlotView;
import com.health.view.FactorPlotView;
import com.health.view.PlotView;
import com.health.viewmodel.BodyFeelingTypeInfo;
import com.health.viewmodel.CommonFeelingTypeInfo;

public class DiaryFragment extends Fragment {

	private FactorPlotView factorPlotView;
	private BodyFeelingPlotView bodyFeelingPlotView;
	public static final int PERIOD_DATE_TIME_DIALOG_FRAGMENT = 10002;
	public static final int SAVE_FEELING_DIALOG_FRAGMENT = 10009;
	XYPlotZoomPan plot;
	private Date mPlotDateTo;
	private Date mPlotDateFrom;
	private int currentTab;

    SimpleDateFormat domainLabelFormat;
    
    private TextView tvPatientFullName;
    private TextView tvLocationValue;
    private Button btnChartPeriod;
    private ImageButton ibTitleShow1, ibTitleShow2;
    private boolean isTitleExpanded = true;
    private LinearLayout llPersonInfo;
    
    private RelativeLayout rlChartTimeColletion;
    private RelativeLayout rlMain;
    private View splitterView;
    final int SPLITTER_HEIGHT = 40;
	final int SPLITTER_STEP_PIXEL = 4;
	private int mChartTimeHeight = Integer.MIN_VALUE;
      
    private IRepository mRepository;
    private GregorianCalendar mDateFrom;
    private GregorianCalendar mDateTo;
    public DateFormat mDateFormat = DateFormat.getDateInstance();
    public GregorianCalendar[] mInspectionPeriod;
    
    private User mCurrentUser = null;
            
    private static final int FEELING_INFO_LOADER = 0x01;

	public static final String SAVE_FEELING_DIALOG_FRAGMENT_TAG = "SAVE_FEELING_DIALOG_FRAGMENT_TAG";

	private OnChangeBodyFeelingListener mOnChangeBodyFeelingListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mOnChangeBodyFeelingListener = (OnChangeBodyFeelingListener) activity;
		this.mRepository = ((HealthApplication) getActivity().getApplication()).getRepository();
	}
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);	
    	this.mDateFrom = Settings.getBFShowPeriodFrom();
		this.mDateTo = Settings.getBFShowPeriodTo();
		this.mRepository = ((HealthApplication) getActivity().getApplication()).getRepository();
		this.mCurrentUser = this.mRepository.getCurrentUser();
		this.mInspectionPeriod = new GregorianCalendar[2];
		GregorianCalendar fromGC= new GregorianCalendar();
        fromGC.setTime(this.mCurrentUser.getCreateDate());
		this.mInspectionPeriod[0] = fromGC;
		this.mInspectionPeriod[1] = new GregorianCalendar();	
		
		if(savedInstanceState != null && (Long)savedInstanceState.getLong("mPlotDateTo") != null){
			mPlotDateTo = new Date(savedInstanceState.getLong("mPlotDateTo"));
		} else {
			mPlotDateTo = new Date();
		}

		if(savedInstanceState != null && (Long)savedInstanceState.getLong("mPlotDateFrom") != null){
			mPlotDateFrom = new Date(savedInstanceState.getLong("mPlotDateFrom"));
		} else {
			mPlotDateFrom = new Date(mPlotDateTo.getTime() - 15 * 24 * 60 * 60 * 1000);
		}
		
		if(savedInstanceState != null && (Integer)savedInstanceState.getInt("currentTab") != null){
			currentTab = (Integer)savedInstanceState.getInt("currentTab");
		} else {
			currentTab = 0;
		}
    }

    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		View view = inflater.inflate(R.layout.diary_fragment, null);
		this.ibTitleShow1 = (ImageButton) view.findViewById(R.id.ibTitleShow1);
		this.ibTitleShow2 = (ImageButton) view.findViewById(R.id.ibTitleShow2);
		this.llPersonInfo = (LinearLayout) view.findViewById(R.id.llPersonInfo);
		this.tvPatientFullName = (TextView) view.findViewById(R.id.tvPatientFullName);
		this.tvLocationValue = (TextView) view.findViewById(R.id.tvLocationValue);		
		this.btnChartPeriod = (Button) view.findViewById(R.id.btnChartPeriod);
		
		this.rlChartTimeColletion = (RelativeLayout) view.findViewById(R.id.rlChartTimeColletion);
		this.rlMain = (RelativeLayout) view.findViewById(R.id.rlMain);
		this.splitterView = (View) view.findViewById(R.id.splitterView);
		this.splitterView.setOnTouchListener(new SplitterTouchListener());
		this.rlMain.setOnDragListener(new SplitterDragListener());
		btnChartPeriod.setText(mDateFormat.format(mPlotDateFrom) + " - " + mDateFormat.format(mPlotDateTo));
		btnChartPeriod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PeriodDateDialogFragment newFragment = PeriodDateDialogFragment.newInstance(mDateFrom, mDateTo);
				newFragment.setTargetFragment(DiaryFragment.this, PERIOD_DATE_TIME_DIALOG_FRAGMENT);
				newFragment.show(getFragmentManager(), "perioddialog");
			}
		});
		showTitleButtons();
		this.ibTitleShow1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isTitleExpanded = true;
				showTitleButtons();
			}
		});		
		this.ibTitleShow2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isTitleExpanded = false;				
				showTitleButtons();				
			}
		});
		this.bodyFeelingPlotView = (BodyFeelingPlotView) view.findViewById(R.id.bodyFeelingPlotView);
		this.bodyFeelingPlotView.setPlotDates(this.mPlotDateFrom, this.mPlotDateTo);
		this.bodyFeelingPlotView.setOnChangeDateListener(new PlotView.OnChangeDateListener() {

			@Override
			public void onChangeRangeDate(Date dtFrom, Date dtTo) {
				mPlotDateFrom = dtFrom;
				mPlotDateTo = dtTo;
				factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
				btnChartPeriod.setText(mDateFormat.format(mPlotDateFrom) + " - " + mDateFormat.format(mPlotDateTo));
			}
		});
		this.factorPlotView = (FactorPlotView) view.findViewById(R.id.factorPlotView);
		this.factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
		this.factorPlotView.setCurrentTab(currentTab);		
		this.factorPlotView.setOnChangeDateListener(new PlotView.OnChangeDateListener() {

			@Override
			public void onChangeRangeDate(Date dtFrom, Date dtTo) {
				mPlotDateFrom = dtFrom;
				mPlotDateTo = dtTo;
				bodyFeelingPlotView.setPlotDates(mPlotDateFrom, mPlotDateTo);
				btnChartPeriod.setText(mDateFormat.format(mPlotDateFrom) + " - " + mDateFormat.format(mPlotDateTo));
			}
		});

		this.bodyFeelingPlotView.setOnAddFeelingListener(new BodyFeelingPlotView.OnAddFeelingListener() {
			@Override
			public void onAddFeeling(IFeelingTypeInfo feelingTypeInfo) {
				if(!((MainActivity)getActivity()).checkRegistration()){
					return;
				}
				if(feelingTypeInfo instanceof BodyFeelingTypeInfo){
					BodyFeelingTypeInfo bodyFeelingTypeInfo = (BodyFeelingTypeInfo) feelingTypeInfo;
					if(!((MainActivity)getActivity()).checkSaveBodyFeeling(new Date(), bodyFeelingTypeInfo.getBodyFeelingType(), bodyFeelingTypeInfo.getBodyRegion(),
							bodyFeelingTypeInfo.getCustomBodyFeelingType() != null? bodyFeelingTypeInfo.getCustomBodyFeelingType().getName() : null)){
						return;
					}
				}
				if(feelingTypeInfo instanceof CommonFeelingTypeInfo){
					CommonFeelingTypeInfo commonFeelingTypeInfo = (CommonFeelingTypeInfo) feelingTypeInfo;
					if(!((MainActivity)getActivity()).checkSaveCommonFeeling(new Date(), commonFeelingTypeInfo.getCommonFeelingType(), commonFeelingTypeInfo.getCustomCommonFeelingType())){
						return;
					}
				}
				FragmentManager fragmentManager = getFragmentManager();
				SaveFeelingDialogFragment saveFeelingDialogFragment = SaveFeelingDialogFragment.newInstance(feelingTypeInfo);
				saveFeelingDialogFragment.setTargetFragment(DiaryFragment.this, SAVE_FEELING_DIALOG_FRAGMENT);
				if(fragmentManager.findFragmentByTag(SAVE_FEELING_DIALOG_FRAGMENT_TAG) == null) {
					saveFeelingDialogFragment.show(fragmentManager, SAVE_FEELING_DIALOG_FRAGMENT_TAG);
				}
			}
		});

		return view;
	}

	public void updateFactorInfo(){
		if(factorPlotView != null){
			factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
		}
	}

	public void updateFeelingInfo(){
		if(bodyFeelingPlotView != null){
			bodyFeelingPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();		
		ViewTreeObserver vto = this.rlChartTimeColletion.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if(rlMain.getTag() == null){
					setChartTimeHeight();
					rlMain.setTag(BaseDTO.INT_NULL_VALUE);
				}				
				return true;
			}
		});
	}
	
	private void setChartTimeHeight(){		
		RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)rlChartTimeColletion.getLayoutParams();		
		if(this.mChartTimeHeight < 0){
			this.mChartTimeHeight = params.height; 
		} else {
			params.height = this.mChartTimeHeight;
			rlChartTimeColletion.setLayoutParams(params);			
			rlChartTimeColletion.invalidate();
			splitterView.invalidate();
		}
		this.mChartTimeHeight = params.height;
	}
	
		
	private final class SplitterTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				view.setBackgroundResource(R.drawable.splitter_dark);
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);				
				view.startDrag(data, shadowBuilder, view, 0);				
				return true;
			} else {
				return false;
			}
		}
	}
	
	class SplitterDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {				
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_LOCATION:
				int tagY = Integer.MIN_VALUE;
				if(v.getTag() != null){
					tagY = (Integer) v.getTag();
				}
				if(Math.abs(event.getY() - tagY) > SPLITTER_STEP_PIXEL){
					v.setTag(Math.round(event.getY()));
					View view = (View) event.getLocalState();
					int deltaHeight = Math.round(event.getY() - rlChartTimeColletion.getBottom()) + SPLITTER_HEIGHT/2;										
					mChartTimeHeight =  mChartTimeHeight + deltaHeight;
					setChartTimeHeight();
				}
				break;
			case DragEvent.ACTION_DROP:
				View view = (View) event.getLocalState();			
				view.setBackgroundResource(R.drawable.splitter_light);
				break;
			default:
				break;
			}
			return true;
		}
	}
	
	private void showTitleButtons(){

		if(isTitleExpanded){
			llPersonInfo.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			llPersonInfo.postInvalidate();
			llPersonInfo.requestLayout();
			ibTitleShow1.setVisibility(View.GONE);
			ibTitleShow2.setVisibility(View.VISIBLE);
		} else {
			int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
			llPersonInfo.getLayoutParams().height = heightPx;
			llPersonInfo.postInvalidate();
			llPersonInfo.requestLayout();
			ibTitleShow1.setVisibility(View.VISIBLE);
			ibTitleShow2.setVisibility(View.GONE);
		}
	}


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
    }
    
    @Override
    public void onStart() {
		super.onStart();		
		fillProfile();			
    }  

    public void fillProfile() {
        User user = this.mRepository.getCurrentUser();
        tvPatientFullName.setText(user.getFullName());
        if(user.getCity() != null){
        	this.tvLocationValue.setText(String.format("%s, %s", user.getCity().getName_ru(), user.getCity().getCountry().getName_ru()));
        }
    }	
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == Activity.RESULT_OK){
    		if(requestCode == PERIOD_DATE_TIME_DIALOG_FRAGMENT ) {
                GregorianCalendar calendarFrom = (GregorianCalendar) data.getExtras().get(PeriodDateDialogFragment.DATE_VALUE_FROM);
                GregorianCalendar calendarTo = (GregorianCalendar) data.getExtras().get(PeriodDateDialogFragment.DATE_VALUE_TO);
				this.mPlotDateFrom = calendarFrom.getTime();
        		if(calendarFrom.getTime().getTime() == calendarTo.getTime().getTime()){
					this.mPlotDateTo = new Date(calendarTo.getTime().getTime() + 24 * 60 * 60 * 1000);
        		} else {
					this.mPlotDateTo = calendarTo.getTime();
        		}
				this.btnChartPeriod.setText(this.mDateFormat.format(mPlotDateFrom) + " - " + this.mDateFormat.format(mPlotDateTo));
				this.factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
				this.bodyFeelingPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
            }
			if(requestCode == SAVE_FEELING_DIALOG_FRAGMENT){
				long id =  data.getExtras().getLong(SaveFeelingDialogFragment.EXTRA_ID, -1);
				int typeId =  data.getExtras().getInt(SaveFeelingDialogFragment.EXTRA_TYPE_ID, -1);
				BodyFeeling bodyFeeling = null;
				CommonFeeling commonFeeling = null;
				switch (typeId){
					case SaveFeelingDialogFragment.EXTRA_BODYFEELING_TYPE_ID:
					case SaveFeelingDialogFragment.EXTRA_CUSTOMBODYFEELING_TYPE_ID:
						bodyFeeling = mRepository.getBodyFeelingById(getActivity(), id);
						Toast.makeText(getActivity(), bodyFeeling.getName(), Toast.LENGTH_LONG).show();
						((MainActivity)getActivity()).uploadUserData();
						break;
					case SaveFeelingDialogFragment.EXTRA_COMMONFEELING_TYPE_ID:
					case SaveFeelingDialogFragment.EXTRA_CUSTOMCOMMONFEELING_TYPE_ID:
						commonFeeling = mRepository.getCommonFeelingById(getActivity(), id);
						Toast.makeText(getActivity(), commonFeeling.getName(), Toast.LENGTH_LONG).show();
						((MainActivity)getActivity()).uploadUserData();
						break;

				}
				bodyFeelingPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
			}
    	}
        
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(factorPlotView != null){
        	if(factorPlotView.getPlotDateFrom() != null){
        		outState.putLong("mPlotDateFrom", factorPlotView.getPlotDateFrom().getTime());
        	}
        	if(factorPlotView.getPlotDateTo() != null){
        		outState.putLong("mPlotDateTo", factorPlotView.getPlotDateTo().getTime());
        	}
        	outState.putInt("currentTab", factorPlotView.getCurrentTab()); 
        }
    	
    }
}