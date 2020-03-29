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

public class VenusPlot extends DataPlot {
	private List<SimpleXYSeries> venusRiseSerieses;
	private List<SimpleXYSeries> venusSetSerieses;
	
	private LineAndPointFormatter venusRiseFormatter;
	private LineAndPointFormatter venusSetFormatter;
	
	private List<Planet> venusList = new ArrayList<Planet>();
	
	public VenusPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> venusList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.VENUS_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_venus;
		setupVenusPlot();
		// получаем форматтер для серии
		venusRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		venusSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(venusList);
	}
	
	private void setupVenusPlot(){
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
				this.venusList = (List<Planet>) data[0];
			}
		}
		if(!this.plot.isEmpty()){
			this.plot.clear();
		}
		
		// обновляем серию
		updateVenusRiseSeries();
		updateVenusSetSeries();
    
    	// задаем новую серию
		if(venusRiseSerieses.size() > 0){
			plot.addSeries(venusRiseSerieses.get(0), venusRiseFormatter);
		}
		if(venusSetSerieses.size() > 0){
			plot.addSeries(venusSetSerieses.get(0), venusSetFormatter);
		}
		
    	for(int i = 1; i < venusRiseSerieses.size(); i++){
    		plot.addSeries(venusRiseSerieses.get(i), venusRiseFormatter);
    	}
    	for(int i = 1; i < venusSetSerieses.size(); i++){
    		plot.addSeries(venusSetSerieses.get(i), venusSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateVenusRiseSeries() {
		venusRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : venusList) {
			if (p.getVenusRise() != null && (venusRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getVenusRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(venusRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					venusRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				venusRiseSerieses.get(venusRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateVenusSetSeries() {
		venusSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : venusList) {
			if (p.getVenusSet() != null && (venusSetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getVenusSet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(venusSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					venusSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				venusSetSerieses.get(venusSetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
