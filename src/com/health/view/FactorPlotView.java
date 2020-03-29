package com.health.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;

import com.health.data.GeoPhysicsType;
import com.health.data.MoonType;
import com.health.dialog.PlotHelpDialogFragment;
import com.health.dialog.RangeCountPopupWindow;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.plot.ApPlot;
import com.health.plot.CloudPlot;
import com.health.plot.DataPlot;
import com.health.plot.Electron08MeVPlot;
import com.health.plot.Electron2MeVPlot;
import com.health.plot.F10_7Plot;
import com.health.plot.HumidityPlot;
import com.health.plot.IDataPlot;
import com.health.plot.JupiterPlot;
import com.health.plot.KpPlot;
import com.health.plot.MarsPlot;
import com.health.plot.MercuryPlot;
import com.health.plot.MoonOldPlot;
import com.health.plot.MoonPlot;
import com.health.plot.MoonVisibilityPlot;
import com.health.plot.NeptunePlot;
import com.health.plot.NewRegionsPlot;
import com.health.plot.OpticalFlares1Plot;
import com.health.plot.OpticalFlares2Plot;
import com.health.plot.OpticalFlares3Plot;
import com.health.plot.OpticalFlaresSPlot;
import com.health.plot.PlutoPlot;
import com.health.plot.PrecipitationPlot;
import com.health.plot.PressurePlot;
import com.health.plot.Proton100MeVPlot;
import com.health.plot.Proton10MeVPlot;
import com.health.plot.Proton1MeVPlot;
import com.health.plot.SaturnPlot;
import com.health.plot.SunPlot;
import com.health.plot.SunspotAreaPlot;
import com.health.plot.SunspotNumberPlot;
import com.health.plot.TemperaturePlot;
import com.health.plot.UranusPlot;
import com.health.plot.VenusPlot;
import com.health.plot.WindPlot;
import com.health.plot.XRayFlaresCPlot;
import com.health.plot.XRayFlaresMPlot;
import com.health.plot.XRayFlaresXPlot;
import com.health.plot.XbkgdPlot;
import com.health.repository.IRepository;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class FactorPlotView extends PlotView implements TabHost.TabContentFactory, View.OnTouchListener {
	
	public enum PLOT_TYPE { WEATHER, ASTRONOMY, GEOHELIOPHYSICS, USER };
	
	private TabHost tabHost;
	private final Handler uiHandler = new Handler();

	private Context mContext;
	private IRepository mRepository;
	private FactorDataKeeper fdk;
	private FactorPlotsKeeper fpk;

	private final String TAB_USER = "tab1";
	private final String TAB_WEATHER = "tab2";
	private final String TAB_ASTRONOMY = "tab3";
	private final String TAB_GEO_HELIO_PHYSICS = "tab4";
	private final String TAB_USER_FACTOR = "tab5";

	private LinearLayout llScaleUser, llScaleWeather, llScaleAstronomy, llScaleGeoHelioPhysics, llScaleFactor;

	private LinearLayout llUser, llWeather, llAstronomy, llGeoHelioPhysics, llFactor;

	private ProgressBar pbUser, pbWeather, pbAstronomy, pbGeoHelioPhysics, pbFactor;
	private String tabWidgetLocation = "right";
	private boolean userFactor = false;

	public FactorPlotView(Context context) {
		this(context, null);
	}

	public FactorPlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FactorPlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		
		for (int i = 0; i < attrs.getAttributeCount(); i++) {
			if (attrs.getAttributeName(i).equals("tabWidgetLocation")) {
				tabWidgetLocation = attrs.getAttributeValue(i);
			} else if(attrs.getAttributeName(i).equals("userFactor")){
				if(attrs.getAttributeValue(i).equals("true")){
					userFactor = true;
				}
			}
		}

		if (tabWidgetLocation.equals("right")) {
			((Activity) getContext()).getLayoutInflater().inflate(R.layout.plot_tab_right_host, this, true);
		} else if (tabWidgetLocation.equals("top")) {
			((Activity) getContext()).getLayoutInflater().inflate(R.layout.plot_tab_top_host, this, true);
		}

		mRepository = ((HealthApplication) ((Activity) getContext()).getApplication()).getRepository();
		fdk = new FactorDataKeeper(mRepository);
		fpk = new FactorPlotsKeeper((Activity)getContext(), mRepository, fdk, mPlotDateFrom, mPlotDateTo, mMarkedDate);
		
		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(TAB_USER).setIndicator(createIndicatorView(tabHost, getResources().getString(R.string.tab_user_title), getResources().getDrawable(R.drawable.ic_user)))
				.setContent(this));
		tabHost.addTab(tabHost.newTabSpec(TAB_WEATHER)
				.setIndicator(createIndicatorView(tabHost, getResources().getString(R.string.tab_weather_title), getResources().getDrawable(R.drawable.ic_weather))).setContent(this));
		tabHost.addTab(tabHost.newTabSpec(TAB_ASTRONOMY).setIndicator(createIndicatorView(tabHost, getResources().getString(R.string.tab_astronomy_title), getResources().getDrawable(R.drawable.ic_astronomy)))
		        .setContent(this));
		tabHost.addTab(tabHost.newTabSpec(TAB_GEO_HELIO_PHYSICS)
		        .setIndicator(createIndicatorView(tabHost, getResources().getString(R.string.tab_geophysics_title), getResources().getDrawable(R.drawable.ic_geophysics))).setContent(this));
		if(userFactor){
			tabHost.addTab(tabHost.newTabSpec(TAB_USER_FACTOR)
					.setIndicator(createIndicatorView(tabHost, getResources().getString(R.string.tab_user_factor), getResources().getDrawable(R.drawable.ic_userfactor))).setContent(this));
		}
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(TAB_USER)) {
					updateUserInfo(mPlotDateFrom, mPlotDateTo);
				} else if (tabId.equals(TAB_WEATHER)) {
					updateWeatherInfo(mPlotDateFrom, mPlotDateTo);
				} else if (tabId.equals(TAB_ASTRONOMY)) {
					updateAstronomyInfo(mPlotDateFrom, mPlotDateTo);
				} else if (tabId.equals(TAB_GEO_HELIO_PHYSICS)) {
					updateGeoHelioPhysicsInfo(mPlotDateFrom, mPlotDateTo);
				} else if (tabId.equals(TAB_USER_FACTOR)) {
					updateFactorInfo(mPlotDateFrom, mPlotDateTo);
				}
				
				setScalePlot();
			}
		});

		updateDomainStep();
	}

	@Override
	protected void onChangeRangeDate(Date dtFrom, Date dtTo) {
		super.onChangeRangeDate(dtFrom, dtTo);
		Log.i("west_onChangeRangeDate", mPlotDateFrom.toString()+ "---" + dtFrom.toString());
		mPlotDateFrom = dtFrom;
		mPlotDateTo = dtTo;
		if(isNeedChangeDBDates()){
			updateDBDates();
			mExecutorService.submit(new Runnable() {
				@Override
				public void run() {
					fdk.updateListWeather(mDbDateFrom, mDbDateTo, mDataAmountType);
					fdk.updateListSun(mDbDateFrom, mDbDateTo, mDataAmountType);
					fdk.updateListSolarEclipse(mDbDateFrom, mDbDateTo);
					fdk.updateListMoon(mDbDateFrom, mDbDateTo, mDataAmountType);
					fdk.updateListMoonPhase(mDbDateFrom, mDbDateTo);
					fdk.updateListGeoPhysics(mDbDateFrom, mDbDateTo);
					fdk.updateListHelioPhysics(mDbDateFrom, mDbDateTo);
					fdk.updateListParticle(mDbDateFrom, mDbDateTo);
					fdk.updateListPlanet(mDbDateFrom, mDbDateTo, mDataAmountType);
					fdk.updateListFactor(mDbDateFrom, mDbDateTo);
					fdk.updateCurrentData(mMarkedDate);
        			
        			uiHandler.post(new Runnable() {

    					@Override
    					public void run() {
    						fpk.updatePlots(PLOT_TYPE.WEATHER);
    						fpk.updatePlots(PLOT_TYPE.ASTRONOMY);
    						fpk.updatePlots(PLOT_TYPE.GEOHELIOPHYSICS);
    						fpk.updatePlots(PLOT_TYPE.USER);
    						fpk.updateJuxtaposePlots();
    						fpk.updateFactorPlots();
    					}
        			});
				}
			});
		}		
	}

	private View createIndicatorView(TabHost tabHost, CharSequence label, Drawable icon) {
		LayoutInflater inflater = (LayoutInflater) ((Activity) getContext()).getLayoutInflater();
		View tabIndicator = null;
		if (tabWidgetLocation.equals("right")) {
			tabIndicator = inflater.inflate(R.layout.tab_vertical_indicator, tabHost.getTabWidget(), false);
		} else if (tabWidgetLocation.equals("top")) {
			tabIndicator = inflater.inflate(R.layout.tab_horizontal_indicator, tabHost.getTabWidget(), false);
		}

		final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
		tv.setText(label);
		final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
		iconView.setImageDrawable(icon);
		return tabIndicator;
	}

	public void setUserProgressBarVisibility(int visibility) {
		if (pbUser != null) {
			pbUser.setVisibility(visibility);
		}
	}

	public void setWeatherProgressBarVisibility(int visibility) {
		if (pbWeather != null) {
			pbWeather.setVisibility(visibility);
		}
	}

	public void setAstronomyProgressBarVisibility(int visibility) {
		if (pbAstronomy != null) {
			pbAstronomy.setVisibility(visibility);
		}
	}

	public void setGeoHelioPhysicsProgressBarVisibility(int visibility) {
		if (pbGeoHelioPhysics != null) {
			pbGeoHelioPhysics.setVisibility(visibility);
		}
	}

	public void setFactorProgressBarVisibility(int visibility) {
		if (pbFactor != null) {
			pbFactor.setVisibility(visibility);
		}
	}

	@Override
	public View createTabContent(String tag) {
		LayoutInflater inflater = (LayoutInflater) ((Activity) getContext()).getLayoutInflater();
		View tabView = inflater.inflate(R.layout.weather_item_tab, null, false);
		initZoom((LinearLayout) tabView.findViewById(R.id.llZoom));		

		if (tag.equals(TAB_USER)) {
			pbUser = (ProgressBar) tabView.findViewById(R.id.progressBar);
			llUser = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleUser = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		if (tag.equals(TAB_WEATHER)) {
			pbWeather = (ProgressBar) tabView.findViewById(R.id.progressBar);
			llWeather = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleWeather = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		if (tag.equals(TAB_ASTRONOMY)) {
			pbAstronomy = (ProgressBar) tabView.findViewById(R.id.progressBar);
			llAstronomy = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleAstronomy = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		if (tag.equals(TAB_GEO_HELIO_PHYSICS)) {
			pbGeoHelioPhysics = (ProgressBar) tabView.findViewById(R.id.progressBar);
			llGeoHelioPhysics = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleGeoHelioPhysics = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		if (tag.equals(TAB_USER_FACTOR)) {
			pbFactor = (ProgressBar) tabView.findViewById(R.id.progressBar);
			llFactor = (LinearLayout) tabView.findViewById(R.id.llPlotList);
			llScaleFactor = (LinearLayout) tabView.findViewById(R.id.llScale);
		}
		return tabView;
	}
	
	@Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouch(view, event);
        if(userFactor){
            for(IDataPlot ufp : fpk.factorPlots){
    
            	Rect r1 = new Rect();
            	Rect r2 = new Rect(); 
            	
            	if(ufp.getPlot().getParent() != null) {
                	((RelativeLayout)ufp.getPlot().getParent()).getGlobalVisibleRect(r1,null);
                	((LinearLayout)view).getGlobalVisibleRect(r2,null);
                	
                	PointF point = new PointF(event.getX() + r2.left, event.getY() + r2.top);
                	
    				if(r1.contains((int)point.x,(int)point.y)) {
    					event.setLocation(event.getX(), event.getY() + r2.top - r1.top);
    					ufp.getPlot().onTouch(view, event);
    				}
            	}
    		}
        }
        return true;
    }

	public void updateUserInfo(Date dtFrom, Date dtTo) {
		setUserProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				fdk.updateListWeather(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListSun(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListSolarEclipse(mDbDateFrom, mDbDateTo);
				fdk.updateListMoon(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListMoonPhase(mDbDateFrom, mDbDateTo);
				fdk.updateListGeoPhysics(mDbDateFrom, mDbDateTo);
				fdk.updateListHelioPhysics(mDbDateFrom, mDbDateTo);
				fdk.updateListParticle(mDbDateFrom, mDbDateTo);
				fdk.updateListPlanet(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListFactor(mDbDateFrom, mDbDateTo);
        
				fpk.updateParamsUser();
        
        		uiHandler.post(new Runnable() {

					@Override
					public void run() {
						fpk.updatePlots(PLOT_TYPE.USER);
						fpk.updateJuxtaposePlots();
                		updateLLUser();                		
                		setUserProgressBarVisibility(View.INVISIBLE);
					}
        		});
			}
		});
		
	}

	public void updateWeatherInfo(Date dtFrom, Date dtTo) {
		setWeatherProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				fdk.updateListWeather(mDbDateFrom, mDbDateTo, mDataAmountType);
				fpk.updateParamsWeather();
        		uiHandler.post(new Runnable() {

					@Override
					public void run() {
						fpk.updatePlots(PLOT_TYPE.WEATHER);
						fpk.updateJuxtaposePlots();
						updateLLWeather();
						setWeatherProgressBarVisibility(View.INVISIBLE);
					}
				});
			}
		});
	}
	
	public void updateAstronomyInfo(Date dtFrom, Date dtTo) {
		setAstronomyProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				fdk.updateListSun(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListSolarEclipse(mDbDateFrom, mDbDateTo);
				fdk.updateListMoon(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListMoonPhase(mDbDateFrom, mDbDateTo);
				fdk.updateListPlanet(mDbDateFrom, mDbDateTo, mDataAmountType);

				fpk.updateParamsSun();
				fpk.updateParamsPlanet();
				fpk.updateParamsMoon();
				
        		uiHandler.post(new Runnable() {

					@Override
					public void run() {
						fpk.updatePlots(PLOT_TYPE.ASTRONOMY);
						fpk.updateJuxtaposePlots();
						updateLLAstronomy();
						setAstronomyProgressBarVisibility(View.INVISIBLE);
					}
        		});
			}
		});		
	}

	public void updateGeoHelioPhysicsInfo(Date dtFrom, Date dtTo) {
		setGeoHelioPhysicsProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				fdk.updateListGeoPhysics(mDbDateFrom, mDbDateTo);
				fdk.updateListHelioPhysics(mDbDateFrom, mDbDateTo);
				fdk.updateListParticle(mDbDateFrom, mDbDateTo);
        		
				fpk.updateParamsGeoPhysics();
				fpk.updateParamsHelioPhysics();
				fpk.updateParamsParticle();
				
        		uiHandler.post(new Runnable() {

					@Override
					public void run() {
						fpk.updatePlots(PLOT_TYPE.GEOHELIOPHYSICS);
						fpk.updateJuxtaposePlots();
						updateLLGeoHelioPhysics();
                		setGeoHelioPhysicsProgressBarVisibility(View.INVISIBLE);
					}
        		});
        	}
        });
	}
	
	public void updateFactorInfo(Date dtFrom, Date dtTo){
		setFactorProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				fdk.updateListFactor(mDbDateFrom, mDbDateTo);
        		uiHandler.post(new Runnable() {

					@Override
					public void run() {
						fpk.updateFactorPlots();        		
                		updateLLFactor();                		
                		setFactorProgressBarVisibility(View.INVISIBLE);
					}
        		});
        	}
        });		
	}

	public void updateTotalInfo(Date dtFrom, Date dtTo) {
		setFactorProgressBarVisibility(View.VISIBLE);
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				fdk.updateCurrentData(mMarkedDate);

				fdk.updateListWeather(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListSun(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListSolarEclipse(mDbDateFrom, mDbDateTo);
				fdk.updateListMoon(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListMoonPhase(mDbDateFrom, mDbDateTo);
				fdk.updateListGeoPhysics(mDbDateFrom, mDbDateTo);
				fdk.updateListHelioPhysics(mDbDateFrom, mDbDateTo);
				fdk.updateListParticle(mDbDateFrom, mDbDateTo);
				fdk.updateListPlanet(mDbDateFrom, mDbDateTo, mDataAmountType);
				fdk.updateListFactor(mDbDateFrom, mDbDateTo);

				fpk.updateParamsWeather();
				fpk.updateParamsSun();
				fpk.updateParamsMoon();
				fpk.updateParamsGeoPhysics();
				fpk.updateParamsHelioPhysics();
				fpk.updateParamsParticle();
				fpk.updateParamsPlanet();
				fpk.updateParamsUser();

				uiHandler.post(new Runnable() {

					@Override
					public void run() {
						fpk.updatePlots(PLOT_TYPE.WEATHER);
						fpk.updatePlots(PLOT_TYPE.ASTRONOMY);
						fpk.updatePlots(PLOT_TYPE.GEOHELIOPHYSICS);
						fpk.updatePlots(PLOT_TYPE.USER);
						fpk.updateJuxtaposePlots();
						fpk.updateFactorPlots();

						switch (tabHost.getCurrentTab()) {
							case 0:
								updateLLUser();
								break;
							case 1:
								updateLLWeather();
								break;
							case 2:
								updateLLAstronomy();
								break;
							case 3:
								updateLLGeoHelioPhysics();
								break;
							case 4:
								updateLLFactor();
								break;
						}
						setScalePlot();

						setFactorProgressBarVisibility(View.INVISIBLE);
					}
        		});
			}
		});
		
	}
	
	private void updateLLUser() {
		llUser.removeAllViews();
		addPlotsToView(fpk.userPlots, llUser);
	}

	private void updateLLWeather() {
		llWeather.removeAllViews();
		addPlotsToView(fpk.weatherPlots, llWeather);
	}

	private void updateLLAstronomy() {
		llAstronomy.removeAllViews();
		addPlotsToView(fpk.astronomyPlots, llAstronomy);
	}

	private void updateLLGeoHelioPhysics() {
		llGeoHelioPhysics.removeAllViews();
		addPlotsToView(fpk.geoHelioPhysicsPlots, llGeoHelioPhysics);
	}

	private void updateLLFactor() {
		llFactor.removeAllViews();
		addPlotsToView(fpk.factorPlots, llFactor);
	}

	private void setScalePlot() {
		if (scalePlot.getPlot().getParent() != null) {
			((LinearLayout) scalePlot.getPlot().getParent()).removeAllViews();
		}
		switch (tabHost.getCurrentTab()) {
		case 0:
			llScaleUser.addView(scalePlot.getPlot());
			break;
		case 1:
			llScaleWeather.addView(scalePlot.getPlot());
			break;
		case 2:
			llScaleAstronomy.addView(scalePlot.getPlot());
			break;
		case 3:
			llScaleGeoHelioPhysics.addView(scalePlot.getPlot());
			break;
		case 4:
			llScaleFactor.addView(scalePlot.getPlot());
			break;
		}
	}

	public Date getPlotDateFrom() {
		return mPlotDateFrom;
	}

	public Date getPlotDateTo() {
		return mPlotDateTo;
	}

	public int getCurrentTab() {
		return tabHost.getCurrentTab();
	}

	

	public void setCurrentTab(int currentTab) {
		tabHost.setCurrentTab(currentTab);
	}
	
	public void setMarkedDate(Date markedDate){		
		mMarkedDate = markedDate;
		for (int i = 0; i < fpk.userPlots.size(); i++) {
			fpk.userPlots.get(i).setMarkedDate(mMarkedDate);
		}
		for (int i = 0; i < fpk.weatherPlots.size(); i++) {
			fpk.weatherPlots.get(i).setMarkedDate(mMarkedDate);
		}
		for (int i = 0; i < fpk.astronomyPlots.size(); i++) {
			fpk.astronomyPlots.get(i).setMarkedDate(mMarkedDate);
		}
		for (int i = 0; i < fpk.geoHelioPhysicsPlots.size(); i++) {
			fpk.geoHelioPhysicsPlots.get(i).setMarkedDate(mMarkedDate);
		}
	}

	private void addPlotsToView(List<IDataPlot> plots, LinearLayout ll) {
		for (final IDataPlot p : plots) {
			final int layoutId = p.getLayoutHelpDialogId();
			
			View rowView;
			TextView tvName;
			TextView tvMarkedValue;
			RelativeLayout llPlot;
			Button btnHelpPlot;
			ImageButton btnRC;
			final Button btnHidePlot;
			LayoutInflater inflater = (LayoutInflater) ((Activity) getContext()).getLayoutInflater();
			String currentValue = getCurrentValue(p);
			
			rowView = inflater.inflate(R.layout.weather_list_item, null, false);
			
			tvName = (TextView) rowView.findViewById(R.id.tvPlotName);
			tvMarkedValue = (TextView) rowView.findViewById(R.id.tvMarkedValue);
			llPlot = (RelativeLayout) rowView.findViewById(R.id.llPlot);
			btnHelpPlot = (Button) rowView.findViewById(R.id.btnHelpPlot);
			btnRC = (ImageButton) rowView.findViewById(R.id.btnRC);
			btnHidePlot = (Button) rowView.findViewById(R.id.btnHidePlot);

			tvName.setText(p.getName());
			tvMarkedValue.setText(currentValue);
			p.setMarkedValue(currentValue);
			if (p.getPlot().getParent() != null) {
				((RelativeLayout) p.getPlot().getParent()).removeAllViews();
			}

			llPlot.addView(p.getPlot());
			
			if(fpk.jp.containsKey(p)){
    			for(IDataPlot pp : fpk.jp.get(p)){
    				llPlot.addView(((DataPlot) pp).getPlot());
    			}
			}
			
			if(layoutId == 0){
				btnHelpPlot.setVisibility(INVISIBLE);
			} else {
			
    			btnHelpPlot.setOnClickListener(new OnClickListener(){
    
    				@Override
                    public void onClick(View v) {
    					PlotHelpDialogFragment newFragment = PlotHelpDialogFragment.newInstance(layoutId);
						if(fdk.currentMoon != null && fdk.currentMoon.getMoonOld() != null){
							Calendar cal = Calendar.getInstance();
							cal.setTime(fdk.currentMoon.getMoonOld());
							int day = cal.get(Calendar.DAY_OF_MONTH);
							Bundle bundle = new Bundle();
							bundle.putInt(PlotHelpDialogFragment.EXTRA_PAGE, day - 1);
							newFragment.setArguments(bundle);
						}
    	                newFragment.show(((Activity)mContext).getFragmentManager(), "perioddialog");
                    }});
			}			
			btnRC.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showPopupMenu(v, p);
				}
			});
			
			if(!p.isHaveHistogramm()){
				btnRC.setVisibility(INVISIBLE);
			}
			
			final ViewGroup vg = this;
			
			btnHidePlot.setOnClickListener(new OnClickListener(){
				@Override
                public void onClick(View v) {
					LinearLayout ll = (LinearLayout)((Button)v).getParent().getParent().getParent();
					
					switch (tabHost.getCurrentTab()) {
					case 0:
						llUser.removeView(ll);
						break;
					case 1:
						llWeather.removeView(ll);
						break;
					case 2:
						llAstronomy.removeView(ll);
						break;
					case 3:
						llGeoHelioPhysics.removeView(ll);
						break;
					case 4:
						llFactor.removeView(ll);
						break;
					}
                }
			});
			
			ll.addView(rowView);
		}
	}
	
	int histYOff = 100;
	int histXOff = 100;
	private void showHistogramm(IDataPlot p, View v){
		Date dTo = new Date(mMarkedDate.getTime());
		Date dFrom = new Date(mMarkedDate.getTime());
		dFrom.setYear(dTo.getYear() - 1);
		
		final RangeCountPopupWindow popupWindow = new RangeCountPopupWindow(dFrom, dTo, mRepository, p, (Activity) getContext());  
        popupWindow.setWidth(400);
        popupWindow.setHeight(300);
        popupWindow.showAtLocation(p.getPlot(), Gravity.NO_GRAVITY, histXOff, histYOff);

    	histXOff += 50;
    	histYOff += 50;
	}
	
	private void showPopupMenu(final View v, final IDataPlot p) {
        final PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.plot_menu);
        
        if(p.isHaveHourlyData()){
            popupMenu.getMenu().findItem(R.id.plot_menu_hourly_data).setChecked(p.isShowHourlyData());
            popupMenu.getMenu().findItem(R.id.plot_menu_hourly_change).setChecked(p.isShowHourlyChange());
        } else {
            popupMenu.getMenu().findItem(R.id.plot_menu_hourly_data).setVisible(false);
            popupMenu.getMenu().findItem(R.id.plot_menu_hourly_change).setVisible(false);
        }
        
        if(p.isHaveDailyData()){
            popupMenu.getMenu().findItem(R.id.plot_menu_daily_data).setChecked(p.isShowDailyData());
            popupMenu.getMenu().findItem(R.id.plot_menu_daily_change).setChecked(p.isShowDailyChange());
        } else {
            popupMenu.getMenu().findItem(R.id.plot_menu_daily_data).setVisible(false);
            popupMenu.getMenu().findItem(R.id.plot_menu_daily_change).setVisible(false);
        }
        
        if(!isHaveJuxtapose(p)){
            popupMenu.getMenu().findItem(R.id.plot_menu_remove_juxtapose).setVisible(false);
        }

        for(IDataPlot dp : fpk.weatherPlots){
        	if(dp.getDataTypeId() != p.getDataTypeId()){
        		popupMenu.getMenu().findItem(R.id.plot_menu_add_juxtapose).getSubMenu().add(R.id.menugroup2, dp.getDataTypeId(), Menu.NONE, dp.getName());
        	}
        }
        for(IDataPlot dp : fpk.astronomyPlots){
        	if(dp.getDataTypeId() != p.getDataTypeId()){
        		popupMenu.getMenu().findItem(R.id.plot_menu_add_juxtapose).getSubMenu().add(R.id.menugroup2, dp.getDataTypeId(), Menu.NONE, dp.getName());
        	}
        }
        for(IDataPlot dp : fpk.geoHelioPhysicsPlots){
        	if(dp.getDataTypeId() != p.getDataTypeId()){
        		popupMenu.getMenu().findItem(R.id.plot_menu_add_juxtapose).getSubMenu().add(R.id.menugroup2, dp.getDataTypeId(), Menu.NONE, dp.getName());
        	}
        }
        
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                    	if(item.getGroupId() == R.id.menugroup2){
                    		IDataPlot juxtaposePlot = null;
                            for(IDataPlot dp : fpk.weatherPlots){
                            	if(dp.getDataTypeId() == item.getItemId()){
                            		juxtaposePlot = dp;
                            	}
                            }
                            if(juxtaposePlot == null){
                                for(IDataPlot dp : fpk.astronomyPlots){
                                	if(dp.getDataTypeId() == item.getItemId()){
                                		juxtaposePlot = dp;
                                	}
                                }
                            }
                            if(juxtaposePlot == null){
                                for(IDataPlot dp : fpk.geoHelioPhysicsPlots){
                                	if(dp.getDataTypeId() == item.getItemId()){
                                		juxtaposePlot = dp;
                                	}
                                }
                            }
                            if(juxtaposePlot != null){
                            	IDataPlot jp = null;
                            	if(juxtaposePlot.getClass() == TemperaturePlot.class){
                            		jp = new TemperaturePlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                            	}
                            	if(juxtaposePlot.getClass() == PressurePlot.class){
                            		jp = new PressurePlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                        		} else if(juxtaposePlot.getClass() == TemperaturePlot.class){
                            		jp = new TemperaturePlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                        		} else if(juxtaposePlot.getClass() == HumidityPlot.class){
                            		jp = new HumidityPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                        		} else if(juxtaposePlot.getClass() == CloudPlot.class){
                            		jp = new CloudPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                        		} else if(juxtaposePlot.getClass() == PrecipitationPlot.class){
                            		jp = new PrecipitationPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                        		} else if(juxtaposePlot.getClass() == WindPlot.class){
                            		jp = new WindPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listWeatherHourly, fdk.listWeatherDaily);
                        		} else if(juxtaposePlot.getClass() == SunPlot.class){
                        			jp = new SunPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listSun, fdk.listSolarEclipse);
                        		} else if(juxtaposePlot.getClass() == MoonVisibilityPlot.class){
                        			jp = new MoonVisibilityPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "",
                					        MoonType.MOON_VISIBILITY_TYPE_MIN_BORDER, MoonType.MOON_VISIBILITY_TYPE_MAX_BORDER, MoonType.MOON_VISIBILITY_TYPE_STEP, fdk.listMoon);
                        		} else if(juxtaposePlot.getClass() == MoonOldPlot.class){
                        			jp = new MoonOldPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "",
                					        MoonType.MOON_OLD_TYPE_MIN_BORDER, MoonType.MOON_OLD_TYPE_MAX_BORDER, MoonType.MOON_OLD_TYPE_STEP, fdk.listMoon, fdk.listMoonPhase);
                        		} else if(juxtaposePlot.getClass() == MoonPlot.class){
                        			jp = new MoonPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listMoon);
                    			} else if(juxtaposePlot.getClass() == MercuryPlot.class){
                    				jp = new MercuryPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                    			} else if(juxtaposePlot.getClass() == VenusPlot.class){
                    				jp = new VenusPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                        		} else if(juxtaposePlot.getClass() == MarsPlot.class){
                    				jp = new MarsPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                        		} else if(juxtaposePlot.getClass() == JupiterPlot.class){
                    				jp = new JupiterPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                        		} else if(juxtaposePlot.getClass() == SaturnPlot.class){
                    				jp = new SaturnPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                        		} else if(juxtaposePlot.getClass() == UranusPlot.class){
                    				jp = new UranusPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                        		} else if(juxtaposePlot.getClass() == NeptunePlot.class){
                    				jp = new NeptunePlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                        		} else if(juxtaposePlot.getClass() == PlutoPlot.class){
                    				jp = new PlutoPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listPlanet);
                            	} else if(juxtaposePlot.getClass() == KpPlot.class){
                            		jp = new KpPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", GeoPhysicsType.KP_TYPE_MIN_BORDER,
            						        GeoPhysicsType.KP_TYPE_MAX_BORDER, GeoPhysicsType.KP_TYPE_STEP, fdk.listGeoPhysics);
                        		} else if(juxtaposePlot.getClass() == ApPlot.class){
            						jp = new ApPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listGeoPhysics);
                        		} else if(juxtaposePlot.getClass() == F10_7Plot.class){
                        			jp = new F10_7Plot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == SunspotNumberPlot.class){
                        			jp = new SunspotNumberPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == SunspotAreaPlot.class){
                        			jp = new SunspotAreaPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == NewRegionsPlot.class){
                        			jp = new NewRegionsPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == XRayFlaresCPlot.class){
                        			jp = new XRayFlaresCPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == XRayFlaresMPlot.class){
                        			jp = new XRayFlaresMPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == XRayFlaresXPlot.class){
                        			jp = new XRayFlaresXPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == OpticalFlares1Plot.class){
                        			jp = new OpticalFlares1Plot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == OpticalFlares2Plot.class){
                        			jp = new OpticalFlares2Plot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == OpticalFlares3Plot.class){
                        			jp = new OpticalFlares3Plot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == OpticalFlaresSPlot.class){
                        			jp = new OpticalFlaresSPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == XbkgdPlot.class){
                        			jp = new XbkgdPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listHelioPhysics);
                        		} else if(juxtaposePlot.getClass() == Proton1MeVPlot.class){
                        			jp = new Proton1MeVPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listParticle);
                        		} else if(juxtaposePlot.getClass() == Proton10MeVPlot.class){
                        			jp = new Proton10MeVPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listParticle);
                        		} else if(juxtaposePlot.getClass() == Proton100MeVPlot.class){
                        			jp = new Proton100MeVPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listParticle);
                        		} else if(juxtaposePlot.getClass() == Electron08MeVPlot.class){
                        			jp = new Electron08MeVPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listParticle);
                        		} else if(juxtaposePlot.getClass() == Electron2MeVPlot.class){
                        			jp = new Electron2MeVPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, "", "", fdk.listParticle);
                        		}
                            	jp.updateUI(mPlotDateFrom, mPlotDateTo, domainStep, ticksPerDomainLabel);
                            	jp.setJuxtapose(true);
                				fpk.juxtaposePlots.add(jp);
                				((RelativeLayout) p.getPlot().getParent()).addView(jp.getPlot());
                				if(fpk.jp.containsKey(p)){
                					fpk.jp.get(p).add(jp);
                				} else {
                					fpk.jp.put(p, new ArrayList<IDataPlot>());
                					fpk.jp.get(p).add(jp);
                				}
                            }
        					return true;
        				} else {
                            switch (item.getItemId()) {
                            case R.id.plot_menu_histogramm:
                            	showHistogramm(p, v);
                                return true;
                            case R.id.plot_menu_add_juxtapose:
            					return true;
                            case R.id.plot_menu_remove_juxtapose:
                            	removeJuxtapose(p);
                            	//p.removeJuxtaposeSeries();
            					return true;
                            case R.id.plot_menu_hourly_data:
                        		item.setChecked(!item.isChecked());
                            	p.showHourlyData(item.isChecked());
                            	return true;
                            case R.id.plot_menu_daily_data:
                        		item.setChecked(!item.isChecked());
                            	p.showDailyData(item.isChecked());
                                return true;
                            case R.id.plot_menu_hourly_change:
                        		item.setChecked(!item.isChecked());
                            	p.showHourlyChange(item.isChecked());
                                return true;
                            case R.id.plot_menu_daily_change:
                        		item.setChecked(!item.isChecked());
                            	p.showDailyChange(item.isChecked());
                                return true;
                            default:
                                return false;
                            }
        				}
                    }
                });

        popupMenu.show();
    }
	
	private boolean isHaveJuxtapose(IDataPlot p){
		if(fpk.jp.containsKey(p)){
			return true;
		} else {
			return false;
		}
	}
	
	private void removeJuxtapose(IDataPlot p){
		for(IDataPlot pp : fpk.jp.get(p)){
			((RelativeLayout) p.getPlot().getParent()).removeView(pp.getPlot());
		}
		fpk.jp.remove(p);
	}
	
	private String getCurrentValue(IDataPlot plot){
		DecimalFormat df = new DecimalFormat("0.0E0");
		String result = "";

		if(plot.getClass() == PressurePlot.class){
			if(fdk.currentWeather.getPressure() != null){
				result = String.valueOf(((int) ((fdk.currentWeather.getPressure() * 100) / PressurePlot.mm_Hg))) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == TemperaturePlot.class){
			if(fdk.currentWeather.getTemperature() != null){
				result = fdk.currentWeather.getTemperature().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == HumidityPlot.class){
			if(fdk.currentWeather.getHumidity() != null){
				result = fdk.currentWeather.getHumidity().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == CloudPlot.class){
			if(fdk.currentWeather.getClouds() != null){
				result = fdk.currentWeather.getClouds().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == PrecipitationPlot.class){
			if(fdk.currentWeather.getPrecipitation() != null){
				result = fdk.currentWeather.getPrecipitation().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == WindPlot.class){
			if(fdk.currentWeather.getWindSpeed() != null){
				result = fdk.currentWeather.getWindSpeed().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == MoonVisibilityPlot.class){
			if(fdk.currentMoon.getMoonVisibility() != null){
				result = fdk.currentMoon.getMoonVisibility().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == MoonOldPlot.class){
			if(fdk.currentMoon.getMoonOld() != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd д. HH ч.");
				Date date = fdk.currentMoon.getMoonOld();
				String day = dateFormat.format(date);
				result = ((Integer) (Integer.valueOf(day.substring(0, 2)) - 1)).toString() + day.substring(2);
			}
		} else if(plot.getClass() == KpPlot.class){
			if(fdk.currentGeoPhysics.getKpId() != null){
				result = ((KpPlot)plot).getKpById(fdk.currentGeoPhysics.getKpId()).toString();
			}
		} else if(plot.getClass() == ApPlot.class){
			if(fdk.currentGeoPhysics.getAp() != null){
				result = fdk.currentGeoPhysics.getAp().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == F10_7Plot.class){
			if(fdk.currentHelioPhysics.getF10_7() != null){
				result = fdk.currentHelioPhysics.getF10_7().toString();
			}
		} else if(plot.getClass() == SunspotNumberPlot.class){
			if(fdk.currentHelioPhysics.getSunspotNumber() != null){
				result = fdk.currentHelioPhysics.getSunspotNumber().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == SunspotAreaPlot.class){
			if(fdk.currentHelioPhysics.getSunspotArea() != null){
				result = fdk.currentHelioPhysics.getSunspotArea().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == NewRegionsPlot.class){
			if(fdk.currentHelioPhysics.getNewRegions() != null){
				result = fdk.currentHelioPhysics.getNewRegions().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == XRayFlaresCPlot.class){
			if(fdk.currentHelioPhysics.getFlaresC() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlaresC()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == XRayFlaresMPlot.class){
			if(fdk.currentHelioPhysics.getFlaresM() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlaresM()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == XRayFlaresXPlot.class){
			if(fdk.currentHelioPhysics.getFlaresX() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlaresX()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == OpticalFlares1Plot.class){
			if(fdk.currentHelioPhysics.getFlares1() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlares1()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == OpticalFlares2Plot.class){
			if(fdk.currentHelioPhysics.getFlares2() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlares2()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == OpticalFlares3Plot.class){
			if(fdk.currentHelioPhysics.getFlares3() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlares3()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == OpticalFlaresSPlot.class){
			if(fdk.currentHelioPhysics.getFlaresS() != null){
				result = String.valueOf(fdk.currentHelioPhysics.getFlaresS()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == XbkgdPlot.class){
			if(fdk.currentHelioPhysics.getXbkgd() != null){
				result = fdk.currentHelioPhysics.getXbkgd().toString() + " " + plot.getUnit();
			}
		} else if(plot.getClass() == Proton1MeVPlot.class){
			if(fdk.currentParticle.getProton1MeV() != null){
				result = df.format(fdk.currentParticle.getProton1MeV()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == Proton10MeVPlot.class){
			if(fdk.currentParticle.getProton10MeV() != null){
				result = df.format(fdk.currentParticle.getProton10MeV()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == Proton100MeVPlot.class){
			if(fdk.currentParticle.getProton100MeV() != null){
				result = df.format(fdk.currentParticle.getProton100MeV()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == Electron08MeVPlot.class){
			if(fdk.currentParticle.getElectron08MeV() != null){
				result = df.format(fdk.currentParticle.getElectron08MeV()) + " " + plot.getUnit();
			}
		} else if(plot.getClass() == Electron2MeVPlot.class){
			if(fdk.currentParticle.getElectron2MeV() != null){
				result = df.format(fdk.currentParticle.getElectron2MeV()) + " " + plot.getUnit();
			}
		}
		return result;
	}

	@Override
	public void setDateRange(Date dtFrom, Date dtTo, boolean isRaiseDateRangeEvent) {
		super.setDateRange(dtFrom, dtTo, isRaiseDateRangeEvent);
		for (int i = 0; i < fpk.userPlots.size(); i++) {
			if(fpk.userPlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
				fpk.userPlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		for (int i = 0; i < fpk.weatherPlots.size(); i++) {
			if(fpk.weatherPlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
				fpk.weatherPlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		for (int i = 0; i < fpk.astronomyPlots.size(); i++) {
			if(fpk.astronomyPlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
				fpk.astronomyPlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		for (int i = 0; i < fpk.geoHelioPhysicsPlots.size(); i++) {
			if(fpk.geoHelioPhysicsPlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
				fpk.geoHelioPhysicsPlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		for (int i = 0; i < fpk.factorPlots.size(); i++) {
			if(fpk.factorPlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
				fpk.factorPlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		for (int i = 0; i < fpk.juxtaposePlots.size(); i++) {
			if(fpk.juxtaposePlots.get(i).getPlot().getVisibility() == View.VISIBLE){    			
				fpk.juxtaposePlots.get(i).updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		this.mDataAmountType = getAmountDataType(dtFrom, dtTo);
	}	
	
}
