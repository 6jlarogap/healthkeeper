package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader.TileMode;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.data.WeatherType;
import com.health.main.R;

public class CloudPlot extends DataPlot {
	private SimpleXYSeries cloudHourlySeries;
	private SimpleXYSeries cloudDailySeries;

	private List<SimpleXYSeries> cloudHourlyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> cloudDailyChangeSerieses = new ArrayList<SimpleXYSeries>();
	
	private LineAndPointFormatter cloudHourlyFormatter;
	private LineAndPointFormatter cloudDailyFormatter;
	private LineAndPointFormatter cloudHourlyChangeFormatter;
	private LineAndPointFormatter cloudDailyChangeFormatter;
	
	private List<Weather> weatherHourlyList = new ArrayList<Weather>();
	private List<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();
	
	public CloudPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Weather> weatherHourlyList, List<WeatherDaily> weatherDailyList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(cloudHourlySeries);
		
		dataTypeId = WeatherType.CLOUD_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_cloud;
		isHaveHistogramm = true;
		isHaveHourlyData = true;
		isHaveDailyData = true;
		isShowHourlyData = true;
		histogrammFloorStep = 10;
		
		setupCloudPlot();
		initFormatters();
    	updateData(weatherHourlyList, weatherDailyList);
	}

	private void initFormatters(){
    	cloudHourlyFormatter = getLineAndPointSeriesFormatter(WeatherType.CLOUD_TYPE_COLOR, WeatherType.CLOUD_TYPE_COLOR, null, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK,
		        new LinearGradient(0, 0, 0, 100, Color.WHITE, Color.BLUE, TileMode.CLAMP), null, null, 3, 5);
    	cloudDailyFormatter = getStepSeriesFormatter(WeatherType.CLOUD_TYPE_COLOR, WeatherType.CLOUD_TYPE_COLOR, null, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK,
		        new LinearGradient(0, 0, 0, 100, Color.WHITE, Color.BLUE, TileMode.CLAMP), null, null, 3, 5);
    	cloudHourlyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
    	cloudDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
	}
	
	private void setupCloudPlot(){
		plot.setIndentY(2, 2);
		plot.getGraphWidget().setRangeValueFormat(PlotService.numPercentFormat);
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
		
		// обновляем серию
		updateCloudSerieses();
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
	
	private void updateCloudSerieses() {
		updateCloudHourlySeries();
		updateCloudDailySeries();
		updateCloudHourlyChangeSerieses();
		updateCloudDailyChangeSerieses();
	}

	private void updateCloudHourlySeries(){
		ownSerieses.remove(cloudHourlySeries);
		cloudHourlySeries = new SimpleXYSeries("");
		ownSerieses.add(cloudHourlySeries);

		for (Weather w : weatherHourlyList) {
			if (w.getClouds() != null) {
				cloudHourlySeries.addLast(w.getInfoDate().getTime(), w.getClouds());
			}
		}
	}
	
	private void updateCloudDailySeries(){
		cloudDailySeries = new SimpleXYSeries("");

		for (WeatherDaily wd : weatherDailyList) {
			if (wd.getClouds() != null) {
				cloudDailySeries.addLast(wd.getInfoDate().getTime(), wd.getClouds());
			}
		}
	}
	
	private void updateCloudHourlyChangeSerieses(){
		cloudHourlyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherHourlyList.size(); i++){
    			if(weatherHourlyList.get(i).getClouds() != null && weatherHourlyList.get(i - 1).getClouds() != null && !weatherHourlyList.get(i).getClouds().equals(weatherHourlyList.get(i - 1).getClouds())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getClouds());
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getClouds() + weatherHourlyList.get(i).getClouds() - weatherHourlyList.get(i - 1).getClouds());
    				cloudHourlyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void updateCloudDailyChangeSerieses(){
		cloudDailyChangeSerieses.clear();
		
		if(weatherDailyList.size() >= 2){
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getClouds() != null && weatherDailyList.get(i - 1).getClouds() != null && !weatherDailyList.get(i).getClouds().equals(weatherDailyList.get(i - 1).getClouds())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getClouds());
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getClouds() + weatherDailyList.get(i).getClouds() - weatherDailyList.get(i - 1).getClouds());
    				cloudDailyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void addHourlySeries(){
    	plot.addSeries(cloudHourlySeries, cloudHourlyFormatter);
	}
	
	private void removeHourlySeries(){
		plot.removeSeries(cloudHourlySeries);
	}
	
	private void addDailySeries(){
		plot.addSeries(cloudDailySeries, cloudDailyFormatter);
	}
	
	private void removeDailySeries(){
		plot.removeSeries(cloudDailySeries);
	}
	
	private void addHourlyChangeSerieses(){
    	for(SimpleXYSeries s : cloudHourlyChangeSerieses){
        	plot.addSeries(s, cloudHourlyChangeFormatter);
    	}
	}
	
	private void removeHourlyChangeSerieses(){
    	for(SimpleXYSeries s : cloudHourlyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}
	
	private void addDailyChangeSerieses(){
    	for(SimpleXYSeries s : cloudDailyChangeSerieses){
        	plot.addSeries(s, cloudDailyChangeFormatter);
    	}
	}
	
	private void removeDailyChangeSerieses(){
    	for(SimpleXYSeries s : cloudDailyChangeSerieses){
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
