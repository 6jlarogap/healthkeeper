package com.health.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.health.correlation.Correlation;
import com.health.data.HelioPhysics;
import com.health.data.IFeeling;
import com.health.data.IFeelingTypeInfo;
import com.health.data.Particle;
import com.health.data.WeatherDaily;
import com.health.dialog.CorrResultDialogFragment;
import com.health.dialog.PlotHelpDialogFragment;
import com.health.loader.FeelingTimeInfo;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.plot.BodyFeelingPlot;
import com.health.repository.IRepository;
import com.health.repository.IRepository.DATA_AMOUNT_TYPE;

public class BodyFeelingPlotView extends PlotView implements View.OnTouchListener {

	LinearLayout llScale;
	private Context mContext;
	private final Handler uiHandler = new Handler();
	private IRepository mRepository;
	private Date mMarkedDate = new Date();
	private List<BodyFeelingPlot> plots = new ArrayList<BodyFeelingPlot>();
	FeelingTimeInfo result = new FeelingTimeInfo();
	protected OnTouchListener mOnTouchListener;

	public interface OnAddFeelingListener{
		void onAddFeeling(IFeelingTypeInfo feelingTypeInfo);
	}

	private OnAddFeelingListener mOnAddFeelingListener = null;

	public void setOnAddFeelingListener(OnAddFeelingListener onAddFeelingListener){
		this.mOnAddFeelingListener = onAddFeelingListener;
	}


	public BodyFeelingPlotView(Context context) {
		this(context, null);
	}

	public BodyFeelingPlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BodyFeelingPlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

		((Activity) getContext()).getLayoutInflater().inflate(R.layout.body_feeling_list, this, true);

		mRepository = ((HealthApplication) ((Activity) getContext()).getApplication()).getRepository();
		updateData();
		llScale = (LinearLayout) findViewById(R.id.llScale);
		llScale.addView(scalePlot.getPlot());

		initZoom((LinearLayout) findViewById(R.id.llZoom));
		updatePlots();

	}
	
	private String correlation(IFeelingTypeInfo key){
		double mm_Hg = 101325.d / 760.d;
		String result = key.getName() + "\n";
		
		FeelingTimeInfo feelings = new FeelingTimeInfo();
		feelings.FeelingTimeInfo = mRepository.getFeelingGroups(new Date(0), new Date());
		

		for (IFeelingTypeInfo k : feelings.FeelingTimeInfo.keySet()) {
			if(k.getName().equals(key.getName())){
    			List<IFeeling> lf = feelings.FeelingTimeInfo.get(k);
    			Date dtFrom = lf.get(0).getStartDate();
    			Date dtTo = lf.get(lf.size() - 1).getStartDate();
    			dtFrom.setSeconds(0);
    			dtFrom.setMinutes(0);
    			dtFrom.setHours(0);
    			dtTo.setSeconds(0);
    			dtTo.setMinutes(0);
    			dtTo.setHours(23);
    			List<WeatherDaily> listWeatherDaily = mRepository.getWeatherDailyList(dtFrom, dtTo, DATA_AMOUNT_TYPE.DAILY);
				List<HelioPhysics> listHelioPhysics = mRepository.getHelioPhysicsList(dtFrom, dtTo, DATA_AMOUNT_TYPE.ALL);
				List<Particle> listParticle = mRepository.getParticleList(dtFrom, dtTo, DATA_AMOUNT_TYPE.ALL);
    
    			Integer count = listWeatherDaily.size();
    			
    			if(count > 1){
        			Double[] pressure = new Double[count];
        			Double[] maxTemperature = new Double[count];
        			Double[] minTemperature = new Double[count];
        			Double[] humidity = new Double[count];
        			Double[] clouds = new Double[count];
        			Double[] precipitation = new Double[count];
        			Double[] windSpeed = new Double[count];
        			Double[] windDeg = new Double[count];
        			Boolean[] feeling = new Boolean[count];
        
        			for(int i = 0, j = 0; i < count; i++){
        				pressure[i] = (double) ((int) ((listWeatherDaily.get(i).getPressure() * 100) / mm_Hg));
        				maxTemperature[i] = listWeatherDaily.get(i).getMaxTemperature();
        				minTemperature[i] = listWeatherDaily.get(i).getMinTemperature();
        				humidity[i] = listWeatherDaily.get(i).getHumidity();
        				clouds[i] = listWeatherDaily.get(i).getClouds();
        				precipitation[i] = listWeatherDaily.get(i).getPrecipitation() != null ? listWeatherDaily.get(i).getPrecipitation() : 0;
        				windSpeed[i] = listWeatherDaily.get(i).getWindSpeed();
        				windDeg[i] = listWeatherDaily.get(i).getWindDeg();

						if(j < lf.size() &&
								lf.get(j).getStartDate().getYear() == listWeatherDaily.get(i).getInfoDate().getYear() &&
								lf.get(j).getStartDate().getMonth() == listWeatherDaily.get(i).getInfoDate().getMonth() &&
								lf.get(j).getStartDate().getDate() == listWeatherDaily.get(i).getInfoDate().getDate()){
							feeling[i] = true;
							j++;
						} else {
							feeling[i] = false;
						}
        			}
        			
        			Double Rpb = Correlation.PointBiserialCorrelationCoefficient(pressure, feeling);
        			result += "Давление: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(maxTemperature, feeling);
        			result += "Максимальная температура: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(minTemperature, feeling);
        			result += "Минимальная температура: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(humidity, feeling);
        			result += "Влажность: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(clouds, feeling);
        			result += "Облачность: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(precipitation, feeling);
        			result += "Осадки: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(windSpeed, feeling);
        			result += "Скорость ветра: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        			
        			Rpb = Correlation.PointBiserialCorrelationCoefficient(windDeg, feeling);
        			result += "Направление ветра: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
        		}

				count = listHelioPhysics.size();

				if(count > 1) {
					Double[] F10_7 = new Double[count];
					Double[] sunspotNumber = new Double[count];
					Double[] sunspotArea = new Double[count];
					Double[] newRegion = new Double[count];
					Double[] flares1 = new Double[count];
					Double[] flares2 = new Double[count];
					Double[] flares3 = new Double[count];
					Double[] flaresS = new Double[count];
					Double[] flaresC = new Double[count];
					Double[] flaresM = new Double[count];
					Double[] flaresX = new Double[count];
					Double[] xbkgd = new Double[count];
					Boolean[] feeling = new Boolean[count];

					for(int i = 0, j = 0; i < count; i++){
						F10_7[i] = (double) listHelioPhysics.get(i).getF10_7();
						sunspotNumber[i] = (double) listHelioPhysics.get(i).getF10_7();
						sunspotArea[i] = (double) listHelioPhysics.get(i).getSunspotArea();
						newRegion[i] = (double) listHelioPhysics.get(i).getSunspotNumber();
						flares1[i] = (double) listHelioPhysics.get(i).getFlares1();
						flares2[i] = (double) listHelioPhysics.get(i).getFlares2();
						flares3[i] = (double) listHelioPhysics.get(i).getFlares3();
						flaresS[i] = (double) listHelioPhysics.get(i).getFlaresS();
						flaresC[i] = (double) listHelioPhysics.get(i).getFlaresC();
						flaresM[i] = (double) listHelioPhysics.get(i).getFlaresM();
						flaresX[i] = (double) listHelioPhysics.get(i).getFlaresX();
						//xbkgd[i] = (double) listHelioPhysics.get(i).getXbkgd();

						if(j < lf.size() &&
								lf.get(j).getStartDate().getYear() == listWeatherDaily.get(i).getInfoDate().getYear() &&
								lf.get(j).getStartDate().getMonth() == listWeatherDaily.get(i).getInfoDate().getMonth() &&
								lf.get(j).getStartDate().getDate() == listWeatherDaily.get(i).getInfoDate().getDate()){
							feeling[i] = true;
							j++;
						} else {
							feeling[i] = false;
						}
					}

					Double Rpb = Correlation.PointBiserialCorrelationCoefficient(F10_7, feeling);
					result += "Поток солнечной радиации: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(sunspotNumber, feeling);
					result += "Количество солнечных пятен: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(sunspotArea, feeling);
					result += "Площадь солнечных пятен: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(newRegion, feeling);
					result += "Количество новых пятен: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flares1, feeling);
					result += "Вспышки класса 1: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flares2, feeling);
					result += "Вспышки класса 2: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flares3, feeling);
					result += "Вспышки класса 3: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flaresS, feeling);
					result += "Вспышки класса S: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flaresC, feeling);
					result += "Вспышки класса C: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flaresM, feeling);
					result += "Вспышки класса M: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(flaresX, feeling);
					result += "Вспышки класса X: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
				}

				count = listParticle.size();

				if(count > 1) {
					Double[] proton1MeV = new Double[count];
					Double[] proton10MeV = new Double[count];
					Double[] proton100MeV = new Double[count];
					Double[] electron08MeV = new Double[count];
					Double[] electron2MeV = new Double[count];
					Boolean[] feeling = new Boolean[count];

					for(int i = 0, j = 0; i < count; i++){
						proton1MeV[i] = (double) listParticle.get(i).getProton1MeV();
						proton10MeV[i] = (double) listParticle.get(i).getProton10MeV();
						proton100MeV[i] = (double) listParticle.get(i).getProton100MeV();
						electron08MeV[i] = (double) listParticle.get(i).getElectron08MeV();
						electron2MeV[i] = (double) listParticle.get(i).getElectron2MeV();

						if(j < lf.size() &&
								lf.get(j).getStartDate().getYear() == listWeatherDaily.get(i).getInfoDate().getYear() &&
								lf.get(j).getStartDate().getMonth() == listWeatherDaily.get(i).getInfoDate().getMonth() &&
								lf.get(j).getStartDate().getDate() == listWeatherDaily.get(i).getInfoDate().getDate()){
							feeling[i] = true;
							j++;
						} else {
							feeling[i] = false;
						}
					}

					Double Rpb = Correlation.PointBiserialCorrelationCoefficient(proton1MeV, feeling);
					result += "Протоны 1 МэВ: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(proton10MeV, feeling);
					result += "Протоны 10 МэВ: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(proton100MeV, feeling);
					result += "Протоны 100 МэВ: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(electron08MeV, feeling);
					result += "Электроны 0.8 МэВ: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";

					Rpb = Correlation.PointBiserialCorrelationCoefficient(electron2MeV, feeling);
					result += "Электроны 2 МэВ: r = " + Rpb + "; " + Correlation.verificationPointBiserialCorrelationCoefficient(Rpb, count) + ";\n";
				}
			}
		}
		return result;
	}

	private void updateData() {
		result.FeelingTimeInfo = mRepository.getFeelingGroups(mDbDateFrom, mDbDateTo);
	}

	public void updateTotalInfo(Date dtFrom, Date dtTo) {
		setDateRange(dtFrom, dtTo, false);
		updateDBDates();
		mExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				updateData();

				uiHandler.post(new Runnable() {

					@Override
					public void run() {
						updatePlots();
					}
				});
			}
		});
	}

	public void setPlotDates(Date dateFrom, Date dateTo) {
		setDateRange(dateFrom, dateTo, false);
		if (isNeedChangeDBDates()) {
			updateDBDates();
			updateTotalInfo(dateFrom, dateTo);
		}
	}

	public void setOnTouchListener(OnTouchListener listener) {
		mOnTouchListener = listener;
	}

	public Date getPlotDateFrom() {
		return mPlotDateFrom;
	}

	public Date getPlotDateTo() {
		return mPlotDateTo;
	}

	protected void onTouchListener(View v, MotionEvent event) {
		if (mOnTouchListener != null) {
			mOnTouchListener.onTouch(v, event);
		}
	}

	@Override
	public void setDateRange(Date dtFrom, Date dtTo, boolean isRaiseDateRangeEvent) {
		super.setDateRange(dtFrom, dtTo, isRaiseDateRangeEvent);
		updatePlots();
		for (BodyFeelingPlot p : plots) {
			if (p.getPlot().getVisibility() == View.VISIBLE) {
				p.updateUI(dtFrom, dtTo, domainStep, ticksPerDomainLabel);
			}
		}
		this.mDataAmountType = getAmountDataType(dtFrom, dtTo);
	}

	@Override
	protected void onChangeRangeDate(Date dtFrom, Date dtTo) {
		super.onChangeRangeDate(dtFrom, dtTo);
		mPlotDateFrom = dtFrom;
		mPlotDateTo = dtTo;
		if (isNeedChangeDBDates()) {
			updateDBDates();
			mExecutorService.submit(new Runnable() {
				@Override
				public void run() {
					updateData();

					uiHandler.post(new Runnable() {

						@Override
						public void run() {
							updatePlots();
						}
					});
				}
			});
		} else {
			updatePlots();
		}
	}

	private void updatePlots() {
		LayoutInflater inflater = (LayoutInflater) ((Activity) getContext()).getLayoutInflater();
		LinearLayout ll = (LinearLayout) findViewById(R.id.llPlotList);

		ll.removeAllViews();
		plots.clear();

		for (IFeelingTypeInfo key : result.FeelingTimeInfo.keySet()) {
			if (isHaveFeelingOnRangeDate(result.FeelingTimeInfo.get(key))) {
				BodyFeelingPlot bodyFeelingPlot = new BodyFeelingPlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate, key.getName(), "", 0, 0, 20, key,
				        result.FeelingTimeInfo.get(key));

				plots.add(bodyFeelingPlot);

				View rowView;
				LinearLayout llPlot;
				RelativeLayout llName;
				TextView tvName;

				rowView = inflater.inflate(R.layout.body_feeling_list_item, null, false);
				tvName = (TextView) rowView.findViewById(R.id.tvPlotName);
				llPlot = (LinearLayout) rowView.findViewById(R.id.llPlot);
				llName = (RelativeLayout) rowView.findViewById(R.id.llName);
				Button btnAdd = (Button) rowView.findViewById(R.id.btnAdd);
				Button btnCor = (Button) rowView.findViewById(R.id.btnCor);

				tvName.setText(key.getName());
				llPlot.addView(bodyFeelingPlot.getPlot());
				llName.setBackgroundColor(key.getColor());
				ll.addView(rowView);

				if(btnAdd != null){
					btnAdd.setTag(key);
					btnAdd.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (v.getTag() != null) {
								IFeelingTypeInfo feelingTypeInfo = (IFeelingTypeInfo) v.getTag();
								if (mOnAddFeelingListener != null) {
									mOnAddFeelingListener.onAddFeeling(feelingTypeInfo);
								}
							}

						}
					});
				}

				if(btnCor != null){
					btnCor.setTag(key);
					btnCor.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (v.getTag() != null) {
								String c = correlation((IFeelingTypeInfo) v.getTag());
								CorrResultDialogFragment newFragment = CorrResultDialogFragment.newInstance(c);
		    	                newFragment.show(((Activity)mContext).getFragmentManager(), "corrresultdialog");
							}
						}
					});
				}
			}
		}
	}

	private boolean isHaveFeelingOnRangeDate(List<IFeeling> feelingList) {
		if(feelingList != null){
    		for (IFeeling feeling : feelingList) {
    			if (feeling.getStartDate().getTime() >= mPlotDateFrom.getTime() && feeling.getStartDate().getTime() <= mPlotDateTo.getTime())
    				return true;
    		}
		}
		return false;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		super.onTouch(view, event);
		return true;
	}
}
