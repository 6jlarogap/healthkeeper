package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYRegionFormatter;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.data.WeatherType;
import com.health.main.R;

public class WindPlot extends DataPlot {
	private SimpleXYSeries windSpeedHourlySeries;
	private SimpleXYSeries windSpeedDailySeries;

	private List<SimpleXYSeries> windSpeedHourlyChangeSerieses = new ArrayList<SimpleXYSeries>();
	private List<SimpleXYSeries> windSpeedDailyChangeSerieses = new ArrayList<SimpleXYSeries>();
	
	private LineAndPointFormatter windSpeedHourlyFormatter;
	private LineAndPointFormatter windSpeedDailyFormatter;
	private LineAndPointFormatter windSpeedHourlyChangeFormatter;
	private LineAndPointFormatter windSpeedDailyChangeFormatter;
	
	private List<Weather> weatherHourlyList = new ArrayList<Weather>();
	private List<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();

	private List<RectRegion> regionsHourly = new ArrayList<RectRegion>();
	private List<RectRegion> regionsDaily = new ArrayList<RectRegion>();
	
	private List<XYRegionFormatter> regionHourlyFormatters = new ArrayList<XYRegionFormatter>();
	private List<XYRegionFormatter> regionDailyFormatters = new ArrayList<XYRegionFormatter>();
	
	public WindPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Weather> weatherHourlyList, List<WeatherDaily> weatherDailyList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(windSpeedHourlySeries);
		
		dataTypeId = WeatherType.WINDSPEED_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_wind;
		isHaveHistogramm = true;
		isHaveHourlyData = true;
		isHaveDailyData = true;
		isShowHourlyData = true;
		
		setupWindPlot();
		initFormatters();

		updateData(weatherHourlyList, weatherDailyList);
	}

	private void initFormatters(){
    	windSpeedHourlyFormatter = getLineAndPointSeriesFormatter(WeatherType.WINDSPEED_TYPE_COLOR, WeatherType.WINDSPEED_TYPE_COLOR, null, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK,
		        null, null, null, 3, 5);
    	windSpeedDailyFormatter = getStepSeriesFormatter(WeatherType.WINDSPEED_TYPE_COLOR, WeatherType.WINDSPEED_TYPE_COLOR, null, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	windSpeedHourlyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
    	windSpeedDailyChangeFormatter = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, 1, 2);
	}
	
	private void setupWindPlot(){
		plot.setIndentY(1, 1);
		plot.getGraphWidget().setRangeValueFormat(PlotService.numPositiveFormat);
		plot.AddOnChangeDomainBoundaryListener(new OnChangeDomainBoundaryListener(){
			@Override
			public void OnChangeDomainBoundary(){
				updateRangeStep();
			}
		});
		
		plot.getLegendWidget().setVisible(true);
		plot.getLegendWidget().setWidth(PixelUtils.dpToPix(250), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().setHeight(PixelUtils.dpToPix(30), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().position(
                60,
                XLayoutStyle.ABSOLUTE_FROM_LEFT,
                -12,
                YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.LEFT_TOP);
		DynamicTableModel t = new DynamicTableModel(8, 1);
		plot.getLegendWidget().setTableModel(t);
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
		updateWindSerieses();
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

	private void updateWindSerieses() {
		updateWindSpeedHourlySeries();
		updateWindSpeedDailySeries();
		updateWindHourlyRegions();
		updateWindDailyRegions();
		updateWindSpeedHourlyChangeSerieses();
		updateWindSpeedDailyChangeSerieses();
	}
	
	private void updateWindSpeedHourlySeries(){
		ownSerieses.remove(windSpeedHourlySeries);
		windSpeedHourlySeries = new SimpleXYSeries("");
		ownSerieses.add(windSpeedHourlySeries);

		for (Weather w : weatherHourlyList) {
			if (w.getWindSpeed() != null) {
				windSpeedHourlySeries.addLast(w.getInfoDate().getTime(), w.getWindSpeed());
			}
		}
	}
	
	private void updateWindSpeedDailySeries(){
		windSpeedDailySeries = new SimpleXYSeries("");

		for (WeatherDaily wd : weatherDailyList) {
			if (wd.getWindSpeed() != null) {
				windSpeedDailySeries.addLast(wd.getInfoDate().getTime(), wd.getWindSpeed());
			}
		}
	}
	
	private void updateWindHourlyRegions(){
		clearHourlyRegions();
		
		regionsHourly.clear();
		regionHourlyFormatters.clear();
		
		for (int i = 0; i < weatherHourlyList.size() - 1; i++) {
			regionsHourly.add(new RectRegion(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i + 1).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, ""));
			regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(weatherHourlyList.get(i).getWindDeg().intValue())));
		}
		
		if(weatherHourlyList.size() > 0){
			regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "С"));
			regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(0), 0));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "С-В"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(45), 1));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "В"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(90), 2));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "Ю-В"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(135), 3));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "Ю"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(180), 4));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "Ю-З"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(225), 5));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "З"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(270), 6));
    		regionsHourly.add(new RectRegion(weatherHourlyList.get(0).getInfoDate().getTime(), weatherHourlyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "С-З"));
    		regionHourlyFormatters.add(new XYRegionFormatter(getColorForWindDeg(315), 7));
		}
		
		setupHourlyRegions();
	}
	
	private void updateWindDailyRegions(){
		clearDailyRegions();
		
		regionsDaily.clear();
		regionDailyFormatters.clear();
		
		for (int i = 0; i < weatherDailyList.size() - 1; i++) {
			regionsDaily.add(new RectRegion(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i + 1).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, ""));
			regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(weatherDailyList.get(i).getWindDeg().intValue())));
		}
		
		if(weatherDailyList.size() > 0){
			regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "С"));
			regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(0), 0));
			regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "С-В"));
			regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(45), 1));
    		regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "В"));
    		regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(90), 2));
    		regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "Ю-В"));
    		regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(135), 3));
    		regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "Ю"));
    		regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(180), 4));
    		regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "Ю-З"));
    		regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(225), 5));
    		regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "З"));
    		regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(270), 6));
    		regionsDaily.add(new RectRegion(weatherDailyList.get(0).getInfoDate().getTime(), weatherDailyList.get(0).getInfoDate().getTime(), Double.NEGATIVE_INFINITY, 0.0, "С-З"));
    		regionDailyFormatters.add(new XYRegionFormatter(getColorForWindDeg(315), 7));
		}
		
		setupDailyRegions();
	}
	
	private void updateWindSpeedHourlyChangeSerieses(){
		windSpeedHourlyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherHourlyList.size(); i++){
    			if(weatherHourlyList.get(i).getWindSpeed() != null && weatherHourlyList.get(i - 1).getWindSpeed() != null && !weatherHourlyList.get(i).getWindSpeed().equals(weatherHourlyList.get(i - 1).getWindSpeed())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getWindSpeed());
    				s.addLast(weatherHourlyList.get(i).getInfoDate().getTime(), weatherHourlyList.get(i).getWindSpeed() + weatherHourlyList.get(i).getWindSpeed() - weatherHourlyList.get(i - 1).getWindSpeed());
    				windSpeedHourlyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	private void updateWindSpeedDailyChangeSerieses(){
		windSpeedDailyChangeSerieses.clear();
		
		if(weatherHourlyList.size() >= 2){
    		for(int i = 1; i < weatherDailyList.size(); i++){
    			if(weatherDailyList.get(i).getWindSpeed() != null && weatherDailyList.get(i - 1).getWindSpeed() != null && !weatherDailyList.get(i).getWindSpeed().equals(weatherDailyList.get(i - 1).getWindSpeed())){
    				SimpleXYSeries s = new SimpleXYSeries("");
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getWindSpeed());
    				s.addLast(weatherDailyList.get(i).getInfoDate().getTime(), weatherDailyList.get(i).getWindSpeed() + weatherDailyList.get(i).getWindSpeed() - weatherDailyList.get(i - 1).getWindSpeed());
    				windSpeedDailyChangeSerieses.add(s);
    			}
    		}
		}
	}
	
	
	private int getColorForWindDeg(int degrees) {
		int alpha = 100;
		int red = 0;
		int green = 0;
		int blue = 0;

		if (degrees >= 0 && degrees < 90) {
			blue = 255 - degrees * 255 / 90;
			red = degrees * 255 / 90;
			green = degrees * 255 / 90;
		} else if (degrees >= 90 && degrees < 180) {
			blue = 0;
			red = 255;
			green = 255 - (degrees % 90) * 255 / 90;
		} else if (degrees >= 180 && degrees < 270) {
			blue = 0;
			red = 255 - (degrees % 90) * 255 / 90;
			green = (degrees % 90) * 255 / 90;
		} else if (degrees >= 270 && degrees < 360) {
			blue = (degrees % 90) * 255 / 90;
			red = 0;
			green = 255 - (degrees % 90) * 255 / 90;
		}

		return Color.argb(alpha, red, green, blue);
	}
		
	private void setupHourlyRegions(){
		if (regionsHourly != null && regionHourlyFormatters != null && regionsHourly.size() == regionHourlyFormatters.size()){
			for (int i = 0; i < regionsHourly.size(); ++i){
				windSpeedHourlyFormatter.addRegion(regionsHourly.get(i), regionHourlyFormatters.get(i));
			}
		}
	}
	
	private void setupDailyRegions(){
		if (regionsDaily != null && regionDailyFormatters != null && regionsDaily.size() == regionDailyFormatters.size()){
			for (int i = 0; i < regionsDaily.size(); ++i){
				windSpeedDailyFormatter.addRegion(regionsDaily.get(i), regionDailyFormatters.get(i));
			}
		}
	}
	
	private void clearHourlyRegions(){
		for(RectRegion r : regionsHourly){
			windSpeedHourlyFormatter.removeRegion(r);
		}
	}
	
	private void clearDailyRegions(){
		for(RectRegion r : regionsDaily){
			windSpeedDailyFormatter.removeRegion(r);
		}
	}

	private void addHourlySeries(){
    	plot.addSeries(windSpeedHourlySeries, windSpeedHourlyFormatter);
	}
	
	private void removeHourlySeries(){
		plot.removeSeries(windSpeedHourlySeries);
	}
	
	private void addDailySeries(){
		plot.addSeries(windSpeedDailySeries, windSpeedDailyFormatter);
	}
	
	private void removeDailySeries(){
		plot.removeSeries(windSpeedDailySeries);
	}
	
	private void addHourlyChangeSerieses(){
    	for(SimpleXYSeries s : windSpeedHourlyChangeSerieses){
        	plot.addSeries(s, windSpeedHourlyChangeFormatter);
    	}
	}
	
	private void removeHourlyChangeSerieses(){
    	for(SimpleXYSeries s : windSpeedHourlyChangeSerieses){
        	plot.removeSeries(s);
    	}
	}
	
	private void addDailyChangeSerieses(){
    	for(SimpleXYSeries s : windSpeedDailyChangeSerieses){
        	plot.addSeries(s, windSpeedDailyChangeFormatter);
    	}
	}
	
	private void removeDailyChangeSerieses(){
    	for(SimpleXYSeries s : windSpeedDailyChangeSerieses){
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
