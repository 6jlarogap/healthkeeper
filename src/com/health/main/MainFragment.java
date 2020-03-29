package com.health.main;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.health.data.City;
import com.health.data.User;
import com.health.dialog.PeriodDateDialogFragment;
import com.health.util.Utils;
import com.health.view.FactorPlotView;
import com.health.view.FeelingPlotView;
import com.health.view.PlotView;

public class MainFragment extends Fragment{

	private FactorPlotView factorPlotView;
	private FeelingPlotView feelingPlotView;
	private RelativeLayout rlMain;
	private LinearLayout llMain;
	private View splitterView;
	final int SPLITTER_HEIGHT = 40;
	final int SPLITTER_STEP_PIXEL = 4;
	final int FACTOR_PLOT_WEIGHT = 3;
	final int FEELING_STAT_PLOT_WEIGHT = 2;
	private int mFactorPlotHeight = Integer.MIN_VALUE;
	
	
	public static final int PERIOD_DATE_TIME_DIALOG_FRAGMENT = 10002;

    private Button btnChartPeriod;
    private TextView tvStatisticsTitle;
    private TextView tvGPS;

    public DateFormat mDateFormat = DateFormat.getDateInstance();
	private Date mPlotDateTo;
	private Date mPlotDateFrom;
	private int currentTab;
	
	public Date getPlotDateFrom(){
		return mPlotDateFrom;
	}
	
	public Date getPlotDateTo(){
		return mPlotDateTo;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public void updateGPSLocation(double lat, double lng, int distance, City city){
		if(this.tvGPS != null){
			this.tvGPS.setText(String.format("Вы находитесь на расстоянии %d м. от центра города %s", distance, city.getName_ru()));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, null);
		this.btnChartPeriod = (Button) view.findViewById(R.id.btnChartPeriod);
		this.tvGPS = (TextView) view.findViewById(R.id.tvGPS);
		this.rlMain = (RelativeLayout) view.findViewById(R.id.rlMain);
		this.llMain = (LinearLayout) view.findViewById(R.id.llMain);
		this.tvStatisticsTitle = (TextView) view.findViewById(R.id.tvStatisticsTitle);
		this.splitterView = (View) view.findViewById(R.id.splitterView);		
		this.factorPlotView = (FactorPlotView) view.findViewById(R.id.factorPlotView);
		this.factorPlotView.updateTotalInfo(this.mPlotDateFrom, this.mPlotDateTo);
		this.factorPlotView.setCurrentTab(currentTab);
		
		this.factorPlotView.setOnChangeDateListener(new PlotView.OnChangeDateListener() {
			
			@Override
			public void onChangeRangeDate(Date dtFrom, Date dtTo) {				
				mPlotDateFrom = dtFrom;
				mPlotDateTo = dtTo;
				feelingPlotView.setPlotDates(mPlotDateFrom, mPlotDateTo);
        		btnChartPeriod.setText(mDateFormat.format(mPlotDateFrom) + " - " + mDateFormat.format(mPlotDateTo));
			}
		});
		
		this.feelingPlotView = (FeelingPlotView) view.findViewById(R.id.feelingStatPlotView);
		this.feelingPlotView.updateTotalInfo(this.mPlotDateFrom, this.mPlotDateTo);
		this.feelingPlotView.setCurrentTab(currentTab);
		
		this.feelingPlotView.setOnChangeDateListener(new PlotView.OnChangeDateListener() {
			
			@Override
			public void onChangeRangeDate(Date dtFrom, Date dtTo) {
				mPlotDateFrom = dtFrom;
				mPlotDateTo = dtTo;
				factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
        		btnChartPeriod.setText(mDateFormat.format(mPlotDateFrom) + " - " + mDateFormat.format(mPlotDateTo));
			}
		});
		this.feelingPlotView.setOnTabChangeListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(FeelingPlotView.TAB_1)) {
					tvStatisticsTitle.setText(R.string.statistics_title);
				}
				if (tabId.equals(FeelingPlotView.TAB_2)) {
					tvStatisticsTitle.setText(R.string.my_statistics_title);
				}
			}
		});
		
		
		this.btnChartPeriod.setText(this.mDateFormat.format(mPlotDateFrom) + " - " + this.mDateFormat.format(mPlotDateTo));
		this.btnChartPeriod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				GregorianCalendar mDateFrom = new GregorianCalendar();
				mDateFrom.setTime(factorPlotView.getPlotDateFrom());
				GregorianCalendar mDateTo = new GregorianCalendar();
				mDateTo.setTime(factorPlotView.getPlotDateTo());
				PeriodDateDialogFragment newFragment = PeriodDateDialogFragment.newInstance(mDateFrom, mDateTo);                
                newFragment.setTargetFragment(MainFragment.this, PERIOD_DATE_TIME_DIALOG_FRAGMENT);
                newFragment.show(getFragmentManager(), "perioddialog");
			}
		});
		
		this.splitterView.setOnTouchListener(new SplitterTouchListener());
		this.llMain.setOnDragListener(new SplitterDragListener());
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		User user = ((MainActivity)getActivity()).getCurrentUser();
		if(user != null){
			if(user.isAnonim()){
				feelingPlotView.setVisibilityMyStatistics(View.GONE);
			} else {
				feelingPlotView.setVisibilityMyStatistics(View.VISIBLE);
			}
		}
		ViewTreeObserver vto = this.rlMain.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if(rlMain.getTag() == null){
					setInitialHeight();									
					rlMain.setTag(true);					
				}
				setSplitterMargin();
				return true;
			}
		});
	}
	
	private void setInitialHeight(){		
		LinearLayout.LayoutParams params1 =  (LinearLayout.LayoutParams)factorPlotView.getLayoutParams();
		LinearLayout.LayoutParams params2 =  (LinearLayout.LayoutParams)feelingPlotView.getLayoutParams();
		if(this.mFactorPlotHeight < 0){
			int totalHeight = llMain.getHeight() - factorPlotView.getTop();    		
    		params1.height = totalHeight * FACTOR_PLOT_WEIGHT / (FACTOR_PLOT_WEIGHT + FEELING_STAT_PLOT_WEIGHT);
    		params2.height = LinearLayout.LayoutParams.MATCH_PARENT;
		} else {
			params1.height = this.mFactorPlotHeight;
    		params2.height = LinearLayout.LayoutParams.MATCH_PARENT;
		}
		factorPlotView.setLayoutParams(params1);
		feelingPlotView.setLayoutParams(params2);
		factorPlotView.invalidate();
		feelingPlotView.invalidate();
		this.mFactorPlotHeight = params1.height;
	}
	
	private void setSplitterMargin(){
		int semiSpliterHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SPLITTER_HEIGHT / 2, getResources().getDisplayMetrics());
		RelativeLayout.LayoutParams splitterParams = (RelativeLayout.LayoutParams) splitterView.getLayoutParams();
		int deltaY = feelingPlotView.getTop() - factorPlotView.getBottom();
		int topMargin = factorPlotView.getBottom() + deltaY/2 - semiSpliterHeightPx;
		int leftMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);		
		splitterParams.setMargins(leftMargin, topMargin, 0, 0);
		splitterView.setLayoutParams(splitterParams);
		splitterView.setVisibility(View.VISIBLE);
		
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
					LinearLayout.LayoutParams params1 =  (LinearLayout.LayoutParams)factorPlotView.getLayoutParams();					
					int height1 = Math.round(event.getY() - factorPlotView.getTop()) - SPLITTER_HEIGHT/2;					
					params1.height = height1;
					factorPlotView.setLayoutParams(params1);
					factorPlotView.invalidate();
					setSplitterMargin();					
					mFactorPlotHeight = params1.height;
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
	
	public void onDestroyView() {
		super.onDestroyView();
		//mPlotDateFrom = this.factorPlotView.getPlotDateFrom();
		//mPlotDateTo = this.factorPlotView.getPlotDateTo();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null && (Integer)savedInstanceState.getInt("mFactorPlotHeight") != null){
			mFactorPlotHeight = (Integer)savedInstanceState.getInt("mFactorPlotHeight");
		}
		if(savedInstanceState != null && (Long)savedInstanceState.getLong("mPlotDateTo") != null){
			mPlotDateTo = new Date(savedInstanceState.getLong("mPlotDateTo"));
		} else {
			mPlotDateTo = new Date();
		}

		if(savedInstanceState != null && (Long)savedInstanceState.getLong("mPlotDateFrom") != null){
			mPlotDateFrom = new Date(savedInstanceState.getLong("mPlotDateFrom"));
		} else {
			mPlotDateFrom = new Date(mPlotDateTo.getTime() - 2 * 24 * 60 * 60 * 1000);
		}
		
		if(savedInstanceState != null && (Integer)savedInstanceState.getInt("currentTab") != null){
			currentTab = (Integer)savedInstanceState.getInt("currentTab");
		} else {
			currentTab = 0;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void updateTotalInfo() {		
		updateFactorTotalInfo();
		updateFeelingTotalInfo();
	}
	
	public void updateFeelingTotalInfo() {		
		if(this.feelingPlotView != null){
			this.feelingPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
		}
	}
	
	public void updateFactorTotalInfo(){
		if(this.factorPlotView != null){
			this.factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
		}
	}
	
	public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putLong("mPlotDateFrom", factorPlotView.getPlotDateFrom().getTime());
    	outState.putLong("mPlotDateTo", factorPlotView.getPlotDateTo().getTime());
    	outState.putInt("currentTab", factorPlotView.getCurrentTab());
    	outState.putInt("mFactorPlotHeight", this.mFactorPlotHeight);    	
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
				this.btnChartPeriod.setText(this.mDateFormat.format(this.mPlotDateFrom) + " - " + this.mDateFormat.format(this.mPlotDateTo));
				this.factorPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
				this.feelingPlotView.updateTotalInfo(mPlotDateFrom, mPlotDateTo);
            }
    	}
        
    }

}
