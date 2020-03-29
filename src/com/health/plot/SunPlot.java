package com.health.plot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYRegionFormatter;
import com.androidplot.xy.XYSeries;
import com.health.data.PlanetType;
import com.health.data.SolarEclipse;
import com.health.data.Sun;
import com.health.data.SunType;
import com.health.main.R;

public class SunPlot extends DataPlot {
	private SimpleXYSeries sunRiseSeries;
	private SimpleXYSeries sunSetSeries;
	private SimpleXYSeries solarEclipseSeries;
	
	private LineAndPointFormatter sunRiseFormatter;
	private LineAndPointFormatter sunSetFormatter;
	
	private List<String> dayLengthSeries;
	private List<Sun> sunList = new ArrayList<Sun>();
	private List<SolarEclipse> solarEclipseList = new ArrayList<SolarEclipse>();

	public SunPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Sun> sunList, List<SolarEclipse> solarEclipseList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, SunType.SUN_TYPE_MIN_BORDER, SunType.SUN_TYPE_MAX_BORDER, SunType.SUN_TYPE_STEP);
		dataTypeId = SunType.SUN_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_sun;
		setupSunPlot();
		
		// получаем форматтер для серии
		sunRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, new PointLabelFormatter(Color.BLACK, 0, 12), POINT_LABELER_TIME, null, null, null, 3, 5);
		sunSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(sunList, solarEclipseList);
	}
	
	private void setupSunPlot(){
		plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.argb(100, 255, 255, 150));
		plot.setRangeValueFormat(new Format() {

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				Date date = new Date(((Number) obj).longValue());
				return PlotService.hourFormat.format(date, toAppendTo, pos);
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		});

		plot.getLegendWidget().setVisible(true);
		plot.getLegendWidget().setWidth(PixelUtils.dpToPix(220), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().setHeight(PixelUtils.dpToPix(20), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().position(
                60,
                XLayoutStyle.ABSOLUTE_FROM_LEFT,
                -7,
                YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.LEFT_TOP);
		DynamicTableModel t = new DynamicTableModel(3, 1);
		plot.getLegendWidget().setTableModel(t);
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Sun){
				this.sunList = (List<Sun>) data[0];
			}
			if(data.length > 1 && data[1].size() > 0 && data[1].get(0) instanceof SolarEclipse){
				this.solarEclipseList = (List<SolarEclipse>) data[1];
			}
		}
		if(!this.plot.isEmpty()){
			this.plot.clear();
		}
		
		// обновляем серию
		updateSunRiseSeries();
		updateSunSetSeries();
		updateDayLengthSeries();
		updateSolarEclipseSeries();

		// создаем регионы
		List<RectRegion> regions = new ArrayList<RectRegion>();
		List<XYRegionFormatter> regionFormatters = new ArrayList<XYRegionFormatter>();
		if(sunRiseSeries.size() > 0){
    		regions.add(new RectRegion(sunRiseSeries.getX(0), sunRiseSeries.getX(0), Double.NEGATIVE_INFINITY, 0.0, "Восход"));
    		regionFormatters.add(new XYRegionFormatter(Color.YELLOW, 0));
    		regions.add(new RectRegion(sunRiseSeries.getX(0), sunRiseSeries.getX(0), Double.NEGATIVE_INFINITY, 0.0, "Заход"));
    		regionFormatters.add(new XYRegionFormatter(Color.RED, 1));
    		regions.add(new RectRegion(sunRiseSeries.getX(0), sunRiseSeries.getX(0), Double.NEGATIVE_INFINITY, 0.0, "Длина дня"));
    		regionFormatters.add(new XYRegionFormatter(Color.BLACK, 2));
		}
		
    
    	// задаем новую серию
		for (int i = 0; i < sunRiseSeries.size(); ++i) {
			SimpleXYSeries s = new SimpleXYSeries("");
			s.addLast(sunRiseSeries.getX(i), sunRiseSeries.getY(i));
			s.addLast(sunRiseSeries.getX(i), (sunRiseSeries.getY(i).longValue() + sunSetSeries.getY(i).longValue()) / 2);
			s.addLast(sunSetSeries.getX(i), sunSetSeries.getY(i));
			final int j = i;
			LineAndPointFormatter f = getLineAndPointSeriesFormatter(Color.BLACK, Color.BLACK, null, new PointLabelFormatter(Color.BLACK, 0, 5), new PointLabeler() {

				@Override
				public String getLabel(XYSeries arg0, int arg1) {
					String result = "";
					
					if (arg1 == 1) {
						if((arg0.getX(arg1).longValue() - originDate.getTime()) % (plot.getDomainStepValue() * plot.getTicksPerDomainLabel()) == 0) {
							result = dayLengthSeries.get(j);
						}
					}

					return result;
				}
			}, null, null, null, 1, 1);

			plot.addSeries(s, f);
		}

    	plot.addSeries(sunRiseSeries, sunRiseFormatter);
    	plot.addSeries(sunSetSeries, sunSetFormatter);
    	
		for (int i = 0; i < solarEclipseSeries.size(); ++i) {
			SimpleXYSeries s = new SimpleXYSeries("");
			s.addLast(solarEclipseSeries.getX(i), SunType.SUN_TYPE_MIN_BORDER);
			s.addLast(solarEclipseSeries.getX(i), SunType.SUN_TYPE_MAX_BORDER);
			final int j = i;
			PointLabelFormatter plf = new PointLabelFormatter(Color.BLACK,10,10);
			plf.getTextPaint().setTextAlign(Align.LEFT);
			LineAndPointFormatter f = getLineAndPointSeriesFormatter(Color.BLUE, Color.BLUE, null, plf, new PointLabeler() {

				@Override
				public String getLabel(XYSeries arg0, int arg1) {

					SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					
					String result = "";

					if(arg1 == 1){
    					switch(solarEclipseList.get(j).getPhase()){
    					case 1: result += "Частное затмение"; break;
    					case 2: result += "Кольцевое затмение"; break;
    					case 3: result += "Полное затмение"; break;
    					}
    					
    					Double M = new BigDecimal(solarEclipseList.get(j).getMagnitude()).setScale(3, RoundingMode.UP).doubleValue();
    					result += "\nМагнитуда: " + M.toString();
    					result += "\nМаксимум: " + dateFormat.format(solarEclipseList.get(j).getInfoDate());
    
    					if(solarEclipseList.get(j).getContact1() != null){
    						result += "\nПервое касание: " + dateFormat.format(solarEclipseList.get(j).getContact1());
    					}
    					if(solarEclipseList.get(j).getContact2() != null){
    						result += "\nВторое касание: " + dateFormat.format(solarEclipseList.get(j).getContact2());
    					}
    					if(solarEclipseList.get(j).getContact3() != null){
    						result += "\nТретье касание: " + dateFormat.format(solarEclipseList.get(j).getContact3());
    					}
    					if(solarEclipseList.get(j).getContact4() != null){
    						result += "\nЧетвертое касание: " + dateFormat.format(solarEclipseList.get(j).getContact4());
    					}
					}
					return result;
				}
			}, null, null, null, 1, 1);

			plot.addSeries(s, f);
		}
		
		sunRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, new PointLabelFormatter(Color.BLACK, 0, 12), POINT_LABELER_TIME, null, regions, regionFormatters, 3, 5);
    	plot.addSeries(sunRiseSeries, sunRiseFormatter);
    	plot.addSeries(sunSetSeries, sunSetFormatter);
    	
    	plot.redraw();
	}
	
	private void updateSunRiseSeries() {
		sunRiseSeries = new SimpleXYSeries("");
		
		for (Sun s : sunList) {
			if (s.getSunRise() != null && (sunRiseSeries.size() == 0 || s.getInfoDate().getTime() - sunRiseSeries.getX(sunRiseSeries.size() - 1).longValue() >= MIN_TIME_INTERVAL)) {
				long startDayTime = (s.getSunRise().getTime() / 1000 / 60 / 60 - (s.getSunRise().getTime() / 1000 / 60 / 60 % 24)) * 1000 * 60 * 60;
				sunRiseSeries.addLast(s.getInfoDate().getTime(), s.getSunRise().getTime() - startDayTime - s.getSunRise().getTimezoneOffset() * 60 * 1000);
			}
		}
	}

	private void updateSunSetSeries() {
		sunSetSeries = new SimpleXYSeries("");
		
		for (Sun s : sunList) {
			if (s.getSunSet() != null && (sunSetSeries.size() == 0 || s.getInfoDate().getTime() - sunSetSeries.getX(sunSetSeries.size() - 1).longValue() >= MIN_TIME_INTERVAL)) {
				long startDayTime = (s.getSunSet().getTime() / 1000 / 60 / 60 - (s.getSunSet().getTime() / 1000 / 60 / 60 % 24)) * 1000 * 60 * 60;
				sunSetSeries.addLast(s.getInfoDate().getTime(), s.getSunSet().getTime() - startDayTime - s.getSunSet().getTimezoneOffset() * 60 * 1000);
			}
		}
	}
	
	private void updateDayLengthSeries() {
		dayLengthSeries = new ArrayList<String>();
		
		for (Sun s : sunList) {
			if (s.getDayLength() != null) {
				dayLengthSeries.add(s.getDayLength());
			}
		}
	}
	
	private void updateSolarEclipseSeries() {
		solarEclipseSeries = new SimpleXYSeries("");

		for (SolarEclipse se : solarEclipseList) {
			if (se.getInfoDate() != null) {
				solarEclipseSeries.addLast(se.getInfoDate().getTime(), 0);
			}
		}
	}
}
