package com.health.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.health.data.Complaint;
import com.health.data.IGridGroup;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.plot.FeelingStatPlot;
import com.health.plot.IDataPlot;
import com.health.repository.IRepository;

public class FeelingPlotView extends PlotView implements TabHost.TabContentFactory {
			
	private TabHost tabHost;
	private final Handler uiHandler = new Handler();
			
	private IRepository mRepository;
	private OnTabChangeListener mOnTabChangeListener;
	
	
	

	private LinearLayout llScaleFeelingStat, llScaleFeelingStat2;
	private LinearLayout llFeelingStat, llFeelingStat2;
	private ProgressBar pbFeelingStat, pbFeelingStat2;

	public final static String TAB_1 = "tab1";
	public final static String TAB_2 = "tab2";
	
	

	List<IDataPlot> feelingStatPlots = new ArrayList<IDataPlot>();
	List<IDataPlot> feelingStat2Plots = new ArrayList<IDataPlot>();

	LinkedHashMap<IGridGroup, List<Complaint>> hashMapFeelingStat;
	LinkedHashMap<IGridGroup, List<Complaint>> hashMapFeelingStat2;
	
	public void setOnTabChangeListener(OnTabChangeListener mOnTabChangeListener) {
		this.mOnTabChangeListener = mOnTabChangeListener;
	}
	
	public FeelingPlotView(Context context) {
		this(context, null);
    }

	public FeelingPlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
    }
	
	public FeelingPlotView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    
	    ((Activity)getContext()).getLayoutInflater().inflate(R.layout.plot_tab_right_host, this, true);
		mRepository = ((HealthApplication) ((Activity)getContext()).getApplication()).getRepository();
		
		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(TAB_1).setIndicator(createIndicatorView(tabHost, "Болезненные ощущения", getResources().getDrawable(R.drawable.ic_feelingstat))).setContent(this));
		tabHost.addTab(tabHost.newTabSpec(TAB_2).setIndicator(createIndicatorView(tabHost, "Болезненные ощущения", getResources().getDrawable(R.drawable.ic_my_feelingstat))).setContent(this));
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(TAB_1)) {
					updateFeelingStatInfo(mPlotDateFrom, mPlotDateTo);
				}
				if (tabId.equals(TAB_2)) {
					updateFeelingStat2Info(mPlotDateFrom, mPlotDateTo);
					updateLLFeelingStat2();
				}
				setScalePlot();
				if (mOnTabChangeListener != null) {
					mOnTabChangeListener.onTabChanged(tabId);
				}
			}
		});

	}
	
	public void setVisibilityMyStatistics(int visibility){
		tabHost.getTabWidget().getChildAt(1).setVisibility(visibility);
	}
	
	public void onChangeRangeDate(Date dtFrom, Date dtTo) {
		super.onChangeRangeDate(dtFrom, dtTo);
		mPlotDateFrom = dtFrom;
		mPlotDateTo = dtTo;
		if (isNeedChangeDBDates()) {			
			updateDBDates();
			mExecutorService.submit(new Runnable() {
				@Override
				public void run() {
        			updateHashMapFeelingStat();
        			updateHashMapFeelingStat2();
        			uiHandler.post(new Runnable() {

    					@Override
    					public void run() {
                			updateFeelingStatPlots();
                			updateFeelingStat2Plots();
                
                			updateLLFeelingStat();
                			updateLLFeelingStat2();
    					}
					});
				}
			});
		}
	}
	
	private void updateHashMapFeelingStat(){
		hashMapFeelingStat = mRepository.getBodyComplaints(mDbDateFrom, mDbDateTo);
	}
	
	private void updateHashMapFeelingStat2(){
		hashMapFeelingStat2 = mRepository.getUserBodyComplaints(mDbDateFrom, mDbDateTo);
	}
	
	private View createIndicatorView(TabHost tabHost, CharSequence label, Drawable icon) {
		LayoutInflater inflater = (LayoutInflater) ((Activity)getContext()).getLayoutInflater();
		View tabIndicator = inflater.inflate(R.layout.tab_vertical_indicator, tabHost.getTabWidget(), false);
		final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
		tv.setText(label);
		final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
		iconView.setImageDrawable(icon);
		return tabIndicator;
	}
	
	@Override
	public void setDateRange(Date dtFrom, Date dtTo, boolean isRaiseDateRangeEvent) {
		super.setDateRange(dtFrom, dtTo, isRaiseDateRangeEvent);
		for (int i = 0; i < feelingStatPlots.size(); i++) {
			if(feelingStatPlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
    			feelingStatPlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);    			
    		}
		}
		for (int i = 0; i < feelingStat2Plots.size(); i++) {
			if(feelingStat2Plots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
    			feelingStat2Plots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		this.mDataAmountType = getAmountDataType(dtFrom, dtTo);
	}
	
	private void updateFeelingStatPlots(){
		feelingStatPlots = new ArrayList<IDataPlot>();
		for(IGridGroup key : hashMapFeelingStat.keySet()){
			FeelingStatPlot feelingStatPlot = new FeelingStatPlot((Activity)getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, key.getName(), "Кол-во", 0, 100, 20, hashMapFeelingStat.get(key));
			feelingStatPlots.add(feelingStatPlot);
		}
	}
	
	private void updateFeelingStat2Plots(){
		feelingStat2Plots = new ArrayList<IDataPlot>();
		for(IGridGroup key : hashMapFeelingStat2.keySet()){
			FeelingStatPlot feelingStatPlot = new FeelingStatPlot((Activity)getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, key.getName(), "Кол-во", 0, 30, 5, hashMapFeelingStat2.get(key));
			feelingStat2Plots.add(feelingStatPlot);
		}
	}
	
	public void setFeelingStatProgressBarVisibility(int visibility){
		if(pbFeelingStat != null){
			pbFeelingStat.setVisibility(visibility);
		}
	}
	
	public void setFeelingStat2ProgressBarVisibility(int visibility){
		if(pbFeelingStat2 != null){
			pbFeelingStat2.setVisibility(visibility);
		}
	}
	
	@Override
	public View createTabContent(String tag) {
		LayoutInflater inflater = (LayoutInflater) ((Activity)getContext()).getLayoutInflater();
		View tabView = inflater.inflate(R.layout.weather_item_tab, null, false);
		//this.llZoom = (LinearLayout) tabView.findViewById(R.id.llZoom);
		initZoom((LinearLayout) tabView.findViewById(R.id.llZoom));
		/*llZoom.refreshDrawableState();
		llZoom.bringToFront();
		llZoom.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {	
				scalePlot.getPlot().onTouch(view, event);
				scalePlot.getPlot().calculateMinMaxVals();
				onChangeRangeDate(new Date(scalePlot.getPlot().getCalculatedMinX().longValue()), new Date(scalePlot.getPlot().getCalculatedMaxX().longValue()));
				setDates();				
				onTouchListener(view, event);
				return true;
			}
		});*/

		if (tag.equals(TAB_1)) {
			pbFeelingStat = (ProgressBar) tabView.findViewById(R.id.progressBar);	
			llFeelingStat = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleFeelingStat = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		if (tag.equals(TAB_2)) {
			pbFeelingStat2 = (ProgressBar) tabView.findViewById(R.id.progressBar);	
			llFeelingStat2 = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleFeelingStat2 = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		return tabView;
	}
	
	
	public void updateFeelingStatInfo(Date dtFrom, Date dtTo) {
		setFeelingStatProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				updateHashMapFeelingStat();				
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						updateFeelingStatPlots();
						updateLLFeelingStat();
						setFeelingStatProgressBarVisibility(View.INVISIBLE);
					}
				});
			}
		});
	}
	
	public void updateFeelingStat2Info(Date dtFrom, Date dtTo) {
		setFeelingStat2ProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
            public void run() {	
				updateHashMapFeelingStat2();
				
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						updateFeelingStat2Plots();
						updateLLFeelingStat2();
						setFeelingStat2ProgressBarVisibility(View.INVISIBLE);
					}
				});
			}
		});		
	}
	
	public void updateTotalInfo(Date dtFrom, Date dtTo) {
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
            public void run() {	
        		updateHashMapFeelingStat();
        		updateHashMapFeelingStat2();
        		
        		uiHandler.post(new Runnable() {

					@Override
					public void run() {
		        		updateFeelingStatPlots();
		        		updateFeelingStat2Plots();
		        		
						updateLLFeelingStat();
						updateLLFeelingStat2();
						setScalePlot();
					}
				});
			}
		});
	}
	
	private void setScalePlot() {
		if (scalePlot.getPlot().getParent() != null) {
			((LinearLayout) scalePlot.getPlot().getParent()).removeAllViews();
		}
		switch (tabHost.getCurrentTab()) {
		case 0:
			llScaleFeelingStat.addView(scalePlot.getPlot());
			break;
		case 1:
			llScaleFeelingStat2.addView(scalePlot.getPlot());
			break;
		}
	}

	private void updateLLFeelingStat() {
		if(llFeelingStat != null){
    		llFeelingStat.removeAllViews();
    		addPlotsToView(feelingStatPlots, llFeelingStat);
		}
	}

	private void updateLLFeelingStat2() {
		if(llFeelingStat2 != null){
    		llFeelingStat2.removeAllViews();
    		addPlotsToView(feelingStat2Plots, llFeelingStat2);
		}
	}
	
	public Date getPlotDateFrom()	{
		return mPlotDateFrom;
	}
	
	public Date getPlotDateTo()	{
		return mPlotDateTo;
	}
	
	public int getCurrentTab()	{
		return tabHost.getCurrentTab();
	}
	
	public void setPlotDates(Date dateFrom, Date dateTo){		
		setDateRange(dateFrom, dateTo, false);		
		if (isNeedChangeDBDates()) {
			updateDBDates();
			updateTotalInfo(dateFrom, dateTo);
		}
		
	}
	
	public void setCurrentTab(int currentTab){
		tabHost.setCurrentTab(currentTab);
	}

	private void addPlotsToView(List<IDataPlot> plots, LinearLayout ll) {
		for (IDataPlot p : plots) {
			View rowView;
			TextView tvName;
			TextView tvMarkedValue;
			RelativeLayout linearLayout;
			Button btnHelpPlot;
			ImageButton btnRC;
			Button btnHidePlot;
			LayoutInflater inflater = (LayoutInflater) ((Activity) getContext()).getLayoutInflater();

			rowView = inflater.inflate(R.layout.weather_list_item, null, false);
			tvName = (TextView) rowView.findViewById(R.id.tvPlotName);
			tvMarkedValue = (TextView) rowView.findViewById(R.id.tvMarkedValue);
			linearLayout = (RelativeLayout) rowView.findViewById(R.id.llPlot);
			btnHelpPlot = (Button) rowView.findViewById(R.id.btnHelpPlot);
			btnRC = (ImageButton) rowView.findViewById(R.id.btnRC);
			btnHidePlot = (Button) rowView.findViewById(R.id.btnHidePlot);
			
			btnHelpPlot.setVisibility(View.INVISIBLE);
			btnRC.setVisibility(View.INVISIBLE);
			btnHidePlot.setVisibility(View.INVISIBLE);
			
			tvName.setText(p.getName());
			//tvMarkedValue.setText(p.getMarkedValue());
			if (p.getPlot().getParent() != null) {
				((RelativeLayout) p.getPlot().getParent()).removeAllViews();
			}
			linearLayout.addView(p.getPlot());
			ll.addView(rowView);
		}
	}
}
