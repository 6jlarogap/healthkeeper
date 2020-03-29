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
import com.health.data.Moon;
import com.health.data.MoonType;
import com.health.main.R;

public class MoonPlot extends DataPlot {
	private List<SimpleXYSeries> moonRiseSerieses;
	private List<SimpleXYSeries> moonSetSerieses;
	
	private LineAndPointFormatter moonRiseFormatter;
	private LineAndPointFormatter moonSetFormatter;
	
	private List<Moon> moonList = new ArrayList<Moon>();
	
	public MoonPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Moon> moonList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, MoonType.MOON_TYPE_MIN_BORDER, MoonType.MOON_TYPE_MAX_BORDER, MoonType.MOON_TYPE_STEP);
		
		dataTypeId = MoonType.MOON_TYPE;
		
		layoutHelpDialogId = R.layout.dialog_plot_help_moon;
		setupMoonPlot();
		// получаем форматтер для серии
		moonRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		moonSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(moonList);
	}
	
	private void setupMoonPlot(){
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
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Moon){
				moonList = (List<Moon>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateMoonRiseSeries();
		updateMoonSetSeries();
    
    	// задаем новую серию
		if(moonRiseSerieses.size() > 0){
			plot.addSeries(moonRiseSerieses.get(0), moonRiseFormatter);
		}
		if(moonSetSerieses.size() > 0){
			plot.addSeries(moonSetSerieses.get(0), moonSetFormatter);
		}
		
    	for(int i = 1; i < moonRiseSerieses.size(); i++){
    		plot.addSeries(moonRiseSerieses.get(i), moonRiseFormatter);
    	}
    	for(int i = 1; i < moonSetSerieses.size(); i++){
    		plot.addSeries(moonSetSerieses.get(i), moonSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateMoonRiseSeries() {
		moonRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Moon m : moonList) {
			if (m.getMoonRise() != null && (moonRiseSerieses.size() == 0 || m.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (m.getMoonRise().getTime() - m.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(moonRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					moonRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				moonRiseSerieses.get(moonRiseSerieses.size() - 1).addLast(m.getInfoDate().getTime(), currentY);
				lastX = m.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateMoonSetSeries() {
		moonSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Moon m : moonList) {
			if (m.getMoonSet() != null && (moonSetSerieses.size() == 0 || m.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (m.getMoonSet().getTime() - m.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(moonSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					moonSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				moonSetSerieses.get(moonSetSerieses.size() - 1).addLast(m.getInfoDate().getTime(), currentY);
				lastX = m.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
