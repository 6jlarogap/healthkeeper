package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.FillDirection;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYRegionFormatter;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.data.WeatherType;
import com.health.main.R;

public class TemperaturePlot extends DataPlot {
	private SimpleXYSeries temperatureHourlySeries;
	private SimpleXYSeries maxTemperatureDailySeries;
	private SimpleXYSeries minTemperatureDailySeries;

	private List<SimpleXYSeries> temperatureHourlyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> maxTemperatureDailyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> minTemperatureDailyChangeSerieses = new ArrayList<SimpleXYSeries>();

	private LineAndPointFormatter temperatureHourlyFormatter;
	private LineAndPointFormatter maxTemperatureDailyFormatter;
	private LineAndPointFormatter minTemperatureDailyFormatter;
	private LineAndPointFormatter temperatureHourlyChangeFormatter;
	private LineAndPointFormatter maxTemperatureDailyChangeFormatter;
	private LineAndPointFormatter minTemperatureDailyChangeFormatter;
	
	private List<Weather> weatherHourlyList = new ArrayList<Weather>();
	private List<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();
	
	private List<RectRegion> regions = new ArrayList<RectRegion>();
	private List<XYRegionFormatter> regionFormatters = new ArrayList<XYRegionFormatter>();
	
	public TemperaturePlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Weather> weatherHourlyList, List<WeatherDaily> weatherDailyList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(temperatureHourlySeries);
		
		dataTypeId = WeatherType.TEMPERATURE_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_temperature;
		isHaveHistogramm = true;
		isHaveHourlyData = true;
		isHaveDailyData = true;
		isShowHourlyData = true;
		
		initFormatters();
		setupTemperaturePlot();
		updateData(weatherHourlyList, weatherDailyList);
	}
	
	private void initFormatters(){
		for (int i = -40; i < 40; ++i) {
			regions.add(new RectRegion(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, i, i + 1, "Short"));
			regionFormatters.add(new XYRegionFormatter(getColorForTemperature(i)));
		}
		temperatureHourlyFormatter = getLineAndPointSeriesFormatter(WeatherType.TEMPERATURE_TYPE_COLOR, WeatherType.TEMPERATURE_TYPE_COLOR, null, PlotService.plf,
				POINT_LABELER_FOR_DOMAIN_TICK, null, regions, regionFormatters, 3, 5);
		temperatureHourlyFormatter.setFillDirection(FillDirection.RANGE_ORIGIN);
		
		maxTemperatureDailyFormatter = getStepSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf,
				POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		minTemperatureDailyFormatter = getStepSeriesFormatter(Color.BLUE, Color.BLUE, null, PlotService.plf,
				POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);

		temperatureHourlyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
		maxTemperatureDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
		minTemperatureDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
	}
	
	private int getColorForTemperature(float temperature) {
		int alpha = 100;
		int red = 0;
		int green = 0;
		int blue = 0;

		if (temperature < -40) {
			blue = 255;
		} else if (temperature < 0) {
			blue = 255;
			green = (int) ((temperature + 40) * 255 / 40);
		} else if (temperature > 40) {
			red = 255;
		} else if (temperature >= 0) {
			red = 255;
			green = (int) (255 - (temperature * 255 / 40));
		}

		return Color.argb(alpha, red, green, blue);
	}
	
	private void setupTemperaturePlot(){
		plot.setIndentY(2, 2);
		plot.setUserRangeOrigin(0);
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
		updateTemperatureSeries();

    	// задаем новую серию
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
	
	private void updateTemperatureSeries() {
		updateTemperatureHourlySeries();
		updateTemperatureDailySeries();
		updateTemperatureHourlyChangeSerieses();
		updateTemperatureDailyChangeSerieses();
	}
	
	private void updateTemperatureHourlySeries(){
		ownSerieses.remove(temperatureHourlySeries);
		temperatureHourlySeries = new SimpleXYSeries("");
		ownSerieses.add(temperatureHourlySeries);
		
		for (Weather w : weatherHourlyList) {
			if (w.getTemperature() != null) {
				temperatureHourlySeries.addLast(w.getInfoDate().getTime(), w.getTemperature());
			}
		}
	}
	
	private void updateTemperatureDailySeries(){
		maxTemperatureDailySeries = new SimpleXYSeries("");
		minTemperatureDailySeries = new SimpleXYSeries("");

		for (WeatherDaily wd : weatherDailyList) {
			if (wd.getMaxTemperature() != null) {
				maxTemperatureDailySeries.addLast(wd.getInfoDate().getTime(), wd.getMaxTemperature());
			}
			if (wd.getMinTemperature() != null) {
				minTemperatureDailySeries.addLast(wd.getInfoDate().getTime(), wd.getMinTemperature());
			}
		}
	}
	
	private void updateTemperatureHourlyChangeSerieses(){
		temperatureHourlyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherHourlyList.size(); i++){
    			if(weatherHourlyList.get(i).getTemperature() != null && weatherHourlyList.get(i - 1).getTemperature() != null && !weatherHourlyList.get(i).getTemperature().equals(weatherHourlyList.get(i - 1).getTemperature())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getTemperature());
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getTemperature() + weatherHourlyList.get(i).getTemperature() - weatherHourlyList.get(i - 1).getTemperature());
    				temperatureHourlyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void updateTemperatureDailyChangeSerieses(){
		maxTemperatureDailyChangeSerieses.clear();
		minTemperatureDailyChangeSerieses.clear();
		
		if(weatherDailyList.size() >= 2){
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getMaxTemperature() != null && weatherDailyList.get(i - 1).getMaxTemperature() != null && !weatherDailyList.get(i).getMaxTemperature().equals(weatherDailyList.get(i - 1).getMaxTemperature())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getMaxTemperature());
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getMaxTemperature() + weatherDailyList.get(i).getMaxTemperature() - weatherDailyList.get(i - 1).getMaxTemperature());
    				maxTemperatureDailyChangeSerieses.add(s);
    			}
    		}
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getMinTemperature() != null && weatherDailyList.get(i - 1).getMinTemperature() != null && !weatherDailyList.get(i).getMinTemperature().equals(weatherDailyList.get(i - 1).getMinTemperature())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getMinTemperature());
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getMinTemperature() + weatherDailyList.get(i).getMinTemperature() - weatherDailyList.get(i - 1).getMinTemperature());
    				minTemperatureDailyChangeSerieses.add(s);
    			}
    		}
		}
	}

	private void addHourlySeries(){
    	plot.addSeries(temperatureHourlySeries, temperatureHourlyFormatter);
	}
	
	private void removeHourlySeries(){
		plot.removeSeries(temperatureHourlySeries);
	}
	
	private void addDailySeries(){
		plot.addSeries(maxTemperatureDailySeries, maxTemperatureDailyFormatter);
		plot.addSeries(minTemperatureDailySeries, minTemperatureDailyFormatter);
	}
	
	private void removeDailySeries(){
		plot.removeSeries(maxTemperatureDailySeries);
		plot.removeSeries(minTemperatureDailySeries);
	}
	
	private void addHourlyChangeSerieses(){
    	for(SimpleXYSeries s : temperatureHourlyChangeSerieses){
        	plot.addSeries(s, temperatureHourlyChangeFormatter);
    	}
	}
	
	private void removeHourlyChangeSerieses(){
    	for(SimpleXYSeries s : temperatureHourlyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}
	
	private void addDailyChangeSerieses(){
    	for(SimpleXYSeries s : maxTemperatureDailyChangeSerieses){
        	plot.addSeries(s, maxTemperatureDailyChangeFormatter);
    	}
    	for(SimpleXYSeries s : minTemperatureDailyChangeSerieses){
        	plot.addSeries(s, minTemperatureDailyChangeFormatter);
    	}
	}
	
	private void removeDailyChangeSerieses(){
    	for(SimpleXYSeries s : maxTemperatureDailyChangeSerieses){
        	plot.removeSeries(s);
    	}
    	for(SimpleXYSeries s : minTemperatureDailyChangeSerieses){
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
