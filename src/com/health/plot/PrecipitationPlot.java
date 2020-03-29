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

public class PrecipitationPlot extends DataPlot {
	private SimpleXYSeries precipitationHourlySeries;
	private SimpleXYSeries precipitationDailySeries;

	private List<SimpleXYSeries> precipitationHourlyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> precipitationDailyChangeSerieses = new ArrayList<SimpleXYSeries>();
	
	private LineAndPointFormatter precipitationHourlyFormatter;
	private LineAndPointFormatter precipitationDailyFormatter;
	private LineAndPointFormatter precipitationHourlyChangeFormatter;
	private LineAndPointFormatter precipitationDailyChangeFormatter;
	
	private List<Weather> weatherHourlyList = new ArrayList<Weather>();
	private List<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();
	
	public PrecipitationPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Weather> weatherHourlyList, List<WeatherDaily> weatherDailyList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(precipitationHourlySeries);
		
		dataTypeId = WeatherType.PRECIPITATION_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_precipitation;
		isHaveHistogramm = true;
		isHaveHourlyData = true;
		isHaveDailyData = true;
		isShowHourlyData = true;
    	
		setupPrecipitationPlot();
		initFormatters();
    	updateData(weatherHourlyList, weatherDailyList);
	}
	
	private void initFormatters(){
    	precipitationHourlyFormatter = getLineAndPointSeriesFormatter(WeatherType.PRECIPITATION_TYPE_COLOR, WeatherType.PRECIPITATION_TYPE_COLOR, WeatherType.PRECIPITATION_TYPE_COLOR,
    			PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	precipitationDailyFormatter = getStepSeriesFormatter(WeatherType.PRECIPITATION_TYPE_COLOR, WeatherType.PRECIPITATION_TYPE_COLOR, WeatherType.PRECIPITATION_TYPE_COLOR,
    			PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	precipitationHourlyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
    	precipitationDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
	}
	
	private void setupPrecipitationPlot(){
		plot.setIndentY(0, 0.5);
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
		updatePrecipitationSerieses();
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
	
	private void updatePrecipitationSerieses() {
		updatePrecipitationHourlySeries();
		updatePrecipitationDailySeries();
		updatePrecipitationHourlyChangeSerieses();
		updatePrecipitationDailyChangeSerieses();
	}
	
	private void updatePrecipitationHourlySeries() {
		ownSerieses.remove(precipitationHourlySeries);
		precipitationHourlySeries = new SimpleXYSeries("");
		ownSerieses.add(precipitationHourlySeries);

		for (Weather w : weatherHourlyList) {
			precipitationHourlySeries.addLast(w.getInfoDate().getTime(), w.getPrecipitation() == null ? 0 : w.getPrecipitation());
		}
	}

	private void updatePrecipitationDailySeries() {
		precipitationDailySeries = new SimpleXYSeries("");

		for (WeatherDaily wd : weatherDailyList) {
				precipitationDailySeries.addLast(wd.getInfoDate().getTime(), wd.getPrecipitation() == null ? 0 : wd.getPrecipitation());
		}
	}
	
	private void updatePrecipitationHourlyChangeSerieses(){
		precipitationHourlyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherHourlyList.size(); i++){
    			if(weatherHourlyList.get(i).getPrecipitation() != null && weatherHourlyList.get(i - 1).getPrecipitation() != null && !weatherHourlyList.get(i).getPrecipitation().equals(weatherHourlyList.get(i - 1).getPrecipitation())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getPrecipitation() == null ? 0 : weatherHourlyList.get(i).getPrecipitation());
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), (weatherHourlyList.get(i).getPrecipitation() == null ? 0 : weatherHourlyList.get(i).getPrecipitation()) + (weatherHourlyList.get(i).getPrecipitation() == null ? 0 : weatherHourlyList.get(i).getPrecipitation()) - (weatherHourlyList.get(i-1).getPrecipitation() == null ? 0 : weatherHourlyList.get(i-1).getPrecipitation()));
    				precipitationHourlyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void updatePrecipitationDailyChangeSerieses(){
		precipitationDailyChangeSerieses.clear();
		
		if(weatherDailyList.size() >= 2){
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getPrecipitation() != null && weatherDailyList.get(i - 1).getPrecipitation() != null && !weatherDailyList.get(i).getPrecipitation().equals(weatherDailyList.get(i - 1).getPrecipitation())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getPrecipitation() == null ? 0 : weatherDailyList.get(i).getPrecipitation());
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), (weatherDailyList.get(i).getPrecipitation() == null ? 0 : weatherDailyList.get(i).getPrecipitation()) + (weatherDailyList.get(i).getPrecipitation() == null ? 0 : weatherDailyList.get(i).getPrecipitation()) - (weatherDailyList.get(i-1).getPrecipitation() == null ? 0 : weatherDailyList.get(i-1).getPrecipitation()));
    				precipitationDailyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void addHourlySeries(){
    	plot.addSeries(precipitationHourlySeries, precipitationHourlyFormatter);
	}
	
	private void removeHourlySeries(){
		plot.removeSeries(precipitationHourlySeries);
	}
	
	private void addDailySeries(){
		plot.addSeries(precipitationDailySeries, precipitationDailyFormatter);
	}
	
	private void removeDailySeries(){
		plot.removeSeries(precipitationDailySeries);
	}
	
	private void addHourlyChangeSerieses(){
    	for(SimpleXYSeries s : precipitationHourlyChangeSerieses){
        	plot.addSeries(s, precipitationHourlyChangeFormatter);
    	}
	}
	
	private void removeHourlyChangeSerieses(){
    	for(SimpleXYSeries s : precipitationHourlyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}
	
	private void addDailyChangeSerieses(){
    	for(SimpleXYSeries s : precipitationDailyChangeSerieses){
        	plot.addSeries(s, precipitationDailyChangeFormatter);
    	}
	}
	
	private void removeDailyChangeSerieses(){
    	for(SimpleXYSeries s : precipitationDailyChangeSerieses){
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
