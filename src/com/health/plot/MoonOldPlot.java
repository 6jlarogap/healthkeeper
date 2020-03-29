package com.health.plot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.health.data.Moon;
import com.health.data.MoonPhase;
import com.health.data.MoonType;
import com.health.main.R;

public class MoonOldPlot extends DataPlot {
	private SimpleXYSeries moonOldSeries;
	private SimpleXYSeries moonPhaseIdSeries;
	private LineAndPointFormatter moonOldFormatter;
	private List<Moon> moonList = new ArrayList<Moon>();
	private List<MoonPhase> moonPhaseList = new ArrayList<MoonPhase>();
	private long SYNODIC_MONTH = 2491442800L;//2551442800L;
	
	public MoonOldPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, double yAxisMin, double yAxisMax, double yAxisStep, List<Moon> moonList, List<MoonPhase> moonPhaseList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, yAxisMin, yAxisMax, yAxisStep);
		
		dataTypeId = MoonType.MOON_OLD_TYPE;
		
		layoutHelpDialogId = R.layout.dialog_plot_help_moon_old;
		setupMoonPlot();
		// получаем форматтер для серии
		moonOldFormatter = getLineAndPointSeriesFormatter(Color.rgb(255, 127, 0), Color.rgb(255, 127, 0), Color.rgb(255, 127, 0), PlotService.plf, new PointLabeler() {

			private SimpleDateFormat dateFormat = new SimpleDateFormat("dd д. HH ч.");

			@Override
			public String getLabel(XYSeries arg0, int arg1) {
				String result = "";

				if((arg0.getX(arg1).longValue() - originDate.getTime()) % (plot.getDomainStepValue() * plot.getTicksPerDomainLabel()) == 0) {
    				long timestamp = arg0.getY(arg1).longValue();
    				Date date = new Date(timestamp);
    				String day = dateFormat.format(date);
    				result = ((Integer) (Integer.valueOf(day.substring(0, 2)) - 1)).toString() + day.substring(2);
				}
				
				return result;
			}
		}, null, null, null, 3, 5);
		updateData(moonList, moonPhaseList);
	}
	
	private void setupMoonPlot(){
		plot.setRangeValueFormat(new Format() {

			private SimpleDateFormat dateFormat = new SimpleDateFormat("dd");

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

				long timestamp = ((Number) obj).longValue();
				Date date = new Date(timestamp);
				StringBuffer day = dateFormat.format(date, toAppendTo, pos);
				StringBuffer newDay = new StringBuffer(2);
				newDay.append(Integer.valueOf(day.substring(0)) - 1);
				return newDay;
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;

			}
		});
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Moon){
				moonList = (List<Moon>) data[0];
			}
			if(data.length > 0 && data[1].size() > 1 && data[1].get(0) instanceof MoonPhase){
				moonPhaseList = (List<MoonPhase>) data[1];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateMoonOldSeries();
		updateMoonPhaseIdSeries();
    
    	// задаем новую серию
    	plot.addSeries(moonOldSeries, moonOldFormatter);
    	
    	for(int i = 0; i < moonPhaseIdSeries.size(); ++i){
		    int resId = 0;
		    switch(moonPhaseIdSeries.getY(i).intValue()){
		    case 0:
		    	resId = R.drawable.ic_newmoon;
				break;
			case 1:
				resId = R.drawable.ic_firstquarter;
				break;
			case 2:
				resId = R.drawable.ic_fullmoon;
				break;
			case 3:
				resId = R.drawable.ic_lastquarter;
				break;
			default:
				break;
		    }
		    
		    if(resId != 0){
    		    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
    			plot.addBitmap(bitmap, moonPhaseIdSeries.getX(i), 3024000000d);
		    }
		}
    	
    	plot.redraw();
	}
	
	private void updateMoonOldSeries() {
		moonOldSeries = new SimpleXYSeries("");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd д. HH ч.");
		
		for (Moon m : moonList) {
			if (moonOldSeries.size() > 0){
				if(m.getMoonOld().getTime() < moonOldSeries.getY(moonOldSeries.size() - 1).longValue()){
					Date newMoonDate = getNewMoonDate(moonOldSeries.getX(moonOldSeries.size() - 1).longValue());
					if(newMoonDate != null){
    					moonOldSeries.addLast(newMoonDate.getTime(), SYNODIC_MONTH);
    					moonOldSeries.addLast(newMoonDate.getTime(), 0);
					}
				}
			}
			
			if (m.getMoonOld() != null) {
				moonOldSeries.addLast(m.getInfoDate().getTime(), m.getMoonOld().getTime());
			}
		}
	}
	
	private void updateMoonPhaseIdSeries() {
		moonPhaseIdSeries = new SimpleXYSeries("");

		for (MoonPhase mp : moonPhaseList) {
			if (mp.getMoonPhase() != null) {
				moonPhaseIdSeries.addLast(mp.getInfoDate().getTime(), mp.getMoonPhaseId());
			}
		}
	}
	
	private Date getNewMoonDate(long lastDate){
		for (MoonPhase mp : moonPhaseList) {
			if(mp.getMoonPhaseId() == 0 && mp.getInfoDate().getTime() >= lastDate)
				return mp.getInfoDate();
		}
		
		return null;
	}
}
