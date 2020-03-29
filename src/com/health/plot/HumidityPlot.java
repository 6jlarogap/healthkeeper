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

public class HumidityPlot extends DataPlot {
	private SimpleXYSeries humidityHourlySeries;
	private SimpleXYSeries humidityDailySeries;

	private List<SimpleXYSeries> humidityHourlyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> humidityDailyChangeSerieses = new ArrayList<SimpleXYSeries>();

	private LineAndPointFormatter humidityHourlyFormatter;
	private LineAndPointFormatter humidityDailyFormatter;
	private LineAndPointFormatter humidityHourlyChangeFormatter;
	private LineAndPointFormatter humidityDailyChangeFormatter;

	private List<Weather> weatherHourlyList = new ArrayList<Weather>();
	private List<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();
	
	public HumidityPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Weather> weatherHourlyList, List<WeatherDaily> weatherDailyList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(humidityHourlySeries);
		
		dataTypeId = WeatherType.HUMIDITY_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_humidity;
		isHaveHistogramm = true;
		isHaveHourlyData = true;
		isHaveDailyData = true;
		isShowHourlyData = true;
		histogrammFloorStep = 10;
		
		setupHumidityPlot();
		initFormatters();
		updateData(weatherHourlyList, weatherDailyList);
	}
	
	private void initFormatters(){
		humidityHourlyFormatter = getLineAndPointSeriesFormatter(WeatherType.HUMIDITY_TYPE_COLOR, WeatherType.HUMIDITY_TYPE_COLOR, WeatherType.HUMIDITY_TYPE_COLOR, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		humidityDailyFormatter = getStepSeriesFormatter(WeatherType.HUMIDITY_TYPE_COLOR, WeatherType.HUMIDITY_TYPE_COLOR, WeatherType.HUMIDITY_TYPE_COLOR, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		humidityHourlyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
		humidityDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
	}
	
	private void setupHumidityPlot(){
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
		
		// обновляем серии
		updateHumiditySerieses();
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
	
	private void updateHumiditySerieses() {
		updateHumidityHourlySeries();
		updateHumidityDailySeries();
		updateHumidityHourlyChangeSerieses();
		updateHumidityDailyChangeSerieses();
	}
	
	private void updateHumidityHourlySeries(){
		ownSerieses.remove(humidityHourlySeries);
		humidityHourlySeries = new SimpleXYSeries("");
		ownSerieses.add(humidityHourlySeries);

		for (Weather w : weatherHourlyList) {
			if (w.getHumidity() != null) {
				humidityHourlySeries.addLast(w.getInfoDate().getTime(), w.getHumidity());
			}
		}
	}

	private void updateHumidityDailySeries(){
		humidityDailySeries = new SimpleXYSeries("");

		for (WeatherDaily wd : weatherDailyList) {
			if (wd.getHumidity() != null) {
				humidityDailySeries.addLast(wd.getInfoDate().getTime(), wd.getHumidity());
			}
		}
	}
	
	private void updateHumidityHourlyChangeSerieses(){
		humidityHourlyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherHourlyList.size(); i++){
    			if(weatherHourlyList.get(i).getHumidity() != null && weatherHourlyList.get(i - 1).getHumidity() != null && !weatherHourlyList.get(i).getHumidity().equals(weatherHourlyList.get(i - 1).getHumidity())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getHumidity());
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getHumidity() + weatherHourlyList.get(i).getHumidity() - weatherHourlyList.get(i - 1).getHumidity());
    				humidityHourlyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void updateHumidityDailyChangeSerieses(){
		humidityDailyChangeSerieses.clear();
		
		if(weatherDailyList.size() >= 2){
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getHumidity() != null && weatherDailyList.get(i - 1).getHumidity() != null && !weatherDailyList.get(i).getHumidity().equals(weatherDailyList.get(i - 1).getHumidity())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getHumidity());
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getHumidity() + weatherDailyList.get(i).getHumidity() - weatherDailyList.get(i - 1).getHumidity());
    				humidityDailyChangeSerieses.add(s);
    			}
    		}
		}
	}

	private void addHourlySeries(){
    	plot.addSeries(humidityHourlySeries, humidityHourlyFormatter);
	}
	
	private void removeHourlySeries(){
		plot.removeSeries(humidityHourlySeries);
	}
	
	private void addDailySeries(){
		plot.addSeries(humidityDailySeries, humidityDailyFormatter);
	}
	
	private void removeDailySeries(){
		plot.removeSeries(humidityDailySeries);
	}
	
	private void addHourlyChangeSerieses(){
    	for(SimpleXYSeries s : humidityHourlyChangeSerieses){
        	plot.addSeries(s, humidityHourlyChangeFormatter);
    	}
	}
	
	private void removeHourlyChangeSerieses(){
    	for(SimpleXYSeries s : humidityHourlyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}
	
	private void addDailyChangeSerieses(){
    	for(SimpleXYSeries s : humidityDailyChangeSerieses){
        	plot.addSeries(s, humidityDailyChangeFormatter);
    	}
	}
	
	private void removeDailyChangeSerieses(){
    	for(SimpleXYSeries s : humidityDailyChangeSerieses){
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
