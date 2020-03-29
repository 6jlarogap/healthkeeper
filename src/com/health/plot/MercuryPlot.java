package com.health.plot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
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
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.Planet;
import com.health.data.PlanetType;
import com.health.main.R;

public class MercuryPlot extends DataPlot {
	private List<SimpleXYSeries> mercuryRiseSerieses;
	private List<SimpleXYSeries> mercurySetSerieses;
	
	private LineAndPointFormatter mercuryRiseFormatter;
	private LineAndPointFormatter mercurySetFormatter;
	
	private List<Planet> mercuryList = new ArrayList<Planet>();
	
	public MercuryPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> mercuryList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.MERCURY_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_mercury;
		setupMercuryPlot();
		// получаем форматтер для серии
		mercuryRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		mercurySetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(mercuryList);
	}
	
	private void setupMercuryPlot(){
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
		plot.getLegendWidget().setWidth(PixelUtils.dpToPix(150), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().setHeight(PixelUtils.dpToPix(20), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().position(
                60,
                XLayoutStyle.ABSOLUTE_FROM_LEFT,
                -7,
                YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.LEFT_TOP);
		DynamicTableModel t = new DynamicTableModel(2, 1);
		plot.getLegendWidget().setTableModel(t);
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Planet){
				mercuryList = (List<Planet>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateMercuryRiseSeries();
		updateMercurySetSeries();
    
    	// задаем новую серию
		if(mercuryRiseSerieses.size() > 0){
			plot.addSeries(mercuryRiseSerieses.get(0), mercuryRiseFormatter);
		}
		if(mercurySetSerieses.size() > 0){
			plot.addSeries(mercurySetSerieses.get(0), mercurySetFormatter);
		}
		
    	for(int i = 1; i < mercuryRiseSerieses.size(); i++){
    		plot.addSeries(mercuryRiseSerieses.get(i), mercuryRiseFormatter);
    	}
    	for(int i = 1; i < mercurySetSerieses.size(); i++){
    		plot.addSeries(mercurySetSerieses.get(i), mercurySetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateMercuryRiseSeries() {
		mercuryRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : mercuryList) {
			if (p.getMercuryRise() != null && (mercuryRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getMercuryRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(mercuryRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					mercuryRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				mercuryRiseSerieses.get(mercuryRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateMercurySetSeries() {
		mercurySetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : mercuryList) {
			if (p.getMercurySet() != null && (mercurySetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getMercurySet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(mercurySetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					mercurySetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				mercurySetSerieses.get(mercurySetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
