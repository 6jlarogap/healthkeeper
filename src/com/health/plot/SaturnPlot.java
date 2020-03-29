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

public class SaturnPlot extends DataPlot {
	private List<SimpleXYSeries> saturnRiseSerieses;
	private List<SimpleXYSeries> saturnSetSerieses;
	
	private LineAndPointFormatter saturnRiseFormatter;
	private LineAndPointFormatter saturnSetFormatter;
	
	private List<Planet> saturnList = new ArrayList<Planet>();
	
	public SaturnPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> saturnList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.SATURN_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_saturn;
		setupSaturnPlot();
		// получаем форматтер для серии
		saturnRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		saturnSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(saturnList);
	}
	
	private void setupSaturnPlot(){
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
				saturnList = (List<Planet>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateSaturnRiseSeries();
		updateSaturnSetSeries();
    
    	// задаем новую серию
		if(saturnRiseSerieses.size() > 0){
			plot.addSeries(saturnRiseSerieses.get(0), saturnRiseFormatter);
		}
		if(saturnSetSerieses.size() > 0){
			plot.addSeries(saturnSetSerieses.get(0), saturnSetFormatter);
		}
		
    	for(int i = 1; i < saturnRiseSerieses.size(); i++){
    		plot.addSeries(saturnRiseSerieses.get(i), saturnRiseFormatter);
    	}
    	for(int i = 1; i < saturnSetSerieses.size(); i++){
    		plot.addSeries(saturnSetSerieses.get(i), saturnSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateSaturnRiseSeries() {
		saturnRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : saturnList) {
			if (p.getSaturnRise() != null && (saturnRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getSaturnRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(saturnRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					saturnRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				saturnRiseSerieses.get(saturnRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateSaturnSetSeries() {
		saturnSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : saturnList) {
			if (p.getSaturnSet() != null && (saturnSetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getSaturnSet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(saturnSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					saturnSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				saturnSetSerieses.get(saturnSetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
