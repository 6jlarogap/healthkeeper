package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.data.WeatherType;
import com.health.main.R;

public class PressurePlot extends DataPlot {
	private SimpleXYSeries pressureHourlySeries;
	private SimpleXYSeries pressureDailySeries;

	private List<SimpleXYSeries> pressureHourlyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> pressureDailyChangeSerieses = new ArrayList<SimpleXYSeries>();
	
	private LineAndPointFormatter pressureHourlyFormatter;
	private LineAndPointFormatter pressureDailyFormatter;
	private LineAndPointFormatter pressureHourlyChangeFormatter;
	private LineAndPointFormatter pressureDailyChangeFormatter;

	private List<Weather> weatherHourlyList = new ArrayList<Weather>();
	private List<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();
	
	public static final double mm_Hg = 101325.d / 760.d;
	
	public PressurePlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Weather> weatherHourlyList, List<WeatherDaily> weatherDailyList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(pressureHourlySeries);
		
		dataTypeId = WeatherType.PRESSURE_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_pressure;
		isHaveHistogramm = true;
		isHaveHourlyData = true;
		isHaveDailyData = true;
		isShowHourlyData = true;
		histogrammFloorStep = 5;
		
		initFormatters();
		setupPressurePlot();
		updateData(weatherHourlyList, weatherDailyList);
	}
	
	private void initFormatters(){
		pressureHourlyFormatter = getLineAndPointSeriesFormatter(WeatherType.PRESSURE_TYPE_COLOR, WeatherType.PRESSURE_TYPE_COLOR, WeatherType.PRESSURE_TYPE_COLOR, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		pressureDailyFormatter = getStepSeriesFormatter(WeatherType.PRESSURE_TYPE_COLOR, WeatherType.PRESSURE_TYPE_COLOR, WeatherType.PRESSURE_TYPE_COLOR, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		pressureHourlyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
		pressureDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
	}
	
	private void setupPressurePlot(){
		plot.setIndentY(2, 2);
		plot.AddOnChangeDomainBoundaryListener(new OnChangeDomainBoundaryListener(){
			@Override
			public void OnChangeDomainBoundary(){
				updateRangeStep();
			}
		});
	}
	
	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Weather){
				weatherHourlyList = (List<Weather>) data[0];
			}
			if(data.length > 1 && data[1].size() > 0 && data[1].get(0) instanceof WeatherDaily){
				weatherDailyList = (List<WeatherDaily>) data[1];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		
		// обновляем серии
		updatePressureSerieses();
		if(isShowDailyChange){
			addDailyChangeSerieses();
		}
		if(isShowDailyData){
			addDailySeries();
		}
		if(isShowHourlyChange){
			addHourlyChangeSerieses();
		}
		if(isShowHourlyData){
			addHourlySeries();
		}
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
    	
    	plot.redraw();
	}
	
	private void updatePressureSerieses() {
		updatePressureHourlySeries();
		updatePressureDailySeries();
		updatePressureHourlyChangeSerieses();
		updatePressureDailyChangeSerieses();
	}
	
	private void updatePressureHourlySeries(){
		ownSerieses.remove(pressureHourlySeries);
		pressureHourlySeries = new SimpleXYSeries("");
		ownSerieses.add(pressureHourlySeries);
		
		for (Weather w : weatherHourlyList) {
			if (w.getPressure() != null) {
				pressureHourlySeries.addLast(w.getInfoDate().getTime(), (double) ((int) ((w.getPressure() * 100) / mm_Hg)));
			}
		}
	}
	
	private void updatePressureDailySeries(){
		pressureDailySeries = new SimpleXYSeries("");
		
		for (WeatherDaily wd : weatherDailyList) {
			if (wd.getPressure() != null) {
				pressureDailySeries.addLast(wd.getInfoDate().getTime(), (double) ((int) ((wd.getPressure() * 100) / mm_Hg)));
			}
		}
		
	}
	
	private void updatePressureHourlyChangeSerieses(){
		pressureHourlyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherHourlyList.size(); i++){
    			if(weatherHourlyList.get(i).getPressure() != null && weatherHourlyList.get(i - 1).getPressure() != null && ((double) ((int) ((weatherHourlyList.get(i).getPressure() * 100) / mm_Hg))) != ((double) ((int) ((weatherHourlyList.get(i - 1).getPressure() * 100) / mm_Hg)))){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), (double) ((int) ((weatherHourlyList.get(i).getPressure() * 100) / mm_Hg)));
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), ((double) ((int) ((weatherHourlyList.get(i).getPressure() * 100) / mm_Hg))) + ((double) ((int) ((weatherHourlyList.get(i).getPressure() * 100) / mm_Hg))) - ((double) ((int) ((weatherHourlyList.get(i - 1).getPressure() * 100) / mm_Hg))));
    				pressureHourlyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void updatePressureDailyChangeSerieses(){
		pressureDailyChangeSerieses.clear();
		
		if(weatherDailyList.size() >= 2){
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getPressure() != null && weatherDailyList.get(i - 1).getPressure() != null && ((double) ((int) ((weatherDailyList.get(i).getPressure() * 100) / mm_Hg))) != ((double) ((int) ((weatherDailyList.get(i - 1).getPressure() * 100) / mm_Hg)))){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), (double) ((int) ((weatherDailyList.get(i).getPressure() * 100) / mm_Hg)));
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), ((double) ((int) ((weatherDailyList.get(i).getPressure() * 100) / mm_Hg))) + ((double) ((int) ((weatherDailyList.get(i).getPressure() * 100) / mm_Hg))) - ((double) ((int) ((weatherDailyList.get(i - 1).getPressure() * 100) / mm_Hg))));
    				pressureDailyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void addHourlySeries(){
    	plot.addSeries(pressureHourlySeries, pressureHourlyFormatter);
	}
	
	private void removeHourlySeries(){
		plot.removeSeries(pressureHourlySeries);
	}
	
	private void addDailySeries(){
		plot.addSeries(pressureDailySeries, pressureDailyFormatter);
	}
	
	private void removeDailySeries(){
		plot.removeSeries(pressureDailySeries);
	}
	
	private void addHourlyChangeSerieses(){
    	for(SimpleXYSeries s : pressureHourlyChangeSerieses){
        	plot.addSeries(s, pressureHourlyChangeFormatter);
    	}
	}
	
	private void removeHourlyChangeSerieses(){
    	for(SimpleXYSeries s : pressureHourlyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}
	
	private void addDailyChangeSerieses(){
    	for(SimpleXYSeries s : pressureDailyChangeSerieses){
        	plot.addSeries(s, pressureDailyChangeFormatter);
    	}
	}
	
	private void removeDailyChangeSerieses(){
    	for(SimpleXYSeries s : pressureDailyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}

	@Override
	public void showHourlyData(boolean isShow){
		if(isShow && !isShowHourlyData){
			addHourlySeries();
		} else {
			removeHourlySeries();
		}

		updateRangeStep();
		plot.redraw();
		
		isShowHourlyData = isShow;
	}

	@Override
	public void showDailyData(boolean isShow){
		if(isShow && !isShowDailyData){
			addDailySeries();
		} else {
			removeDailySeries();
		}

		updateRangeStep();
		plot.redraw();
		
		isShowDailyData = isShow;
	}

	@Override
	public void showHourlyChange(boolean isShow){
		if(isShow && !isShowHourlyChange){
			addHourlyChangeSerieses();
		} else {
			removeHourlyChangeSerieses();
		}

		updateRangeStep();
		plot.redraw();
		
		isShowHourlyChange = isShow;
	}

	@Override
	public void showDailyChange(boolean isShow){
		if(isShow && !isShowDailyChange){
			addDailyChangeSerieses();
		} else {
			removeDailyChangeSerieses();
		}

		updateRangeStep();
		plot.redraw();
		
		isShowDailyChange = isShow;
	}
}
