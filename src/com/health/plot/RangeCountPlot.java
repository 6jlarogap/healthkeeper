package com.health.plot;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;

import com.androidplot.Plot.BorderStyle;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYStepMode;

public class RangeCountPlot implements IPlot {
	private SimpleXYSeries series;
	private LineAndPointFormatter formatter;
	private List<Pair<Double, Integer>> list;
	private XYPlotZoomPan plot;
	private Context mContext;
	private String unit;
	private double histogrammFloorStep;
	private Format domainValueFormat;

	
	public RangeCountPlot(Context context, String unit, List<Pair<Double, Integer>> values, double hs, Format dFormat){
		this.mContext = context;
		this.unit = unit;
		this.histogrammFloorStep = hs;
		domainValueFormat = dFormat;
		setupHumidityPlot();
		// получаем форматтер для серии
    	formatter = DataPlot.getLineAndPointSeriesFormatter(Color.CYAN, Color.CYAN, Color.CYAN, null, null, null, null, null, 1, 1);
		updateData(values);
	}
	
	private void setupHumidityPlot(){
		plot = new XYPlotZoomPan(this.mContext, "plot");
		
		plot.setPlotMargins(0, 0, 0, 0);
		plot.setPlotPadding(0, 0, 0, 15);
		plot.getBackgroundPaint().setColor(Color.WHITE);
		plot.setBorderStyle(BorderStyle.ROUNDED, 10f, 10f);
		plot.getBorderPaint().setAlpha(0);

		plot.getGraphWidget().setMargins(10, 10, 20, 0);
		plot.getGraphWidget().setHeight(1f);
		plot.getGraphWidget().setWidth(1f);
		plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
		//plot.getGraphWidget().getDomainLabelPaint().setAlpha(0);
		plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
		plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
		plot.getGraphWidget().setRangeValueFormat(new DecimalFormat("#####"));
		plot.getGraphWidget().setDomainValueFormat(new DecimalFormat("#####.##"));

		plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
		plot.getTitleWidget().setVisible(false);
		plot.getLegendWidget().setVisible(false);
		plot.getGraphWidget().getDomainSubGridLinePaint().setColor(Color.rgb(200, 200, 200));
		plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.rgb(100, 100, 100));
		plot.getGraphWidget().getRangeGridLinePaint().setAlpha(0);

		plot.setRangeLabel("ед.");
		plot.setDomainLabel(unit);
		plot.getDomainLabelWidget().position(10, XLayoutStyle.ABSOLUTE_FROM_LEFT, 0, YLayoutStyle.ABSOLUTE_FROM_BOTTOM);
		plot.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
		plot.getRangeLabelWidget().setVisible(true);
		plot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
		plot.getDomainLabelWidget().setVisible(true);

		plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
		//plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);

		
		plot.setZoomEnabled(false);
		plot.setZoomVertically(false);
		plot.setZoomHorizontally(false);
		plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.rgb(100, 100, 100));
		plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.GRAY);
		
		plot.setDomainValueFormat(domainValueFormat);
	}
	
	public void setHistogrammFloorStep(double hfs){
		histogrammFloorStep = hfs;
	}

	public void updateData(List<?> ... data){
		if(data != null){
			list = (List<Pair<Double, Integer>>) data[0];
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		if(list != null && list.size() > 0){
    		double minVal = list.get(0).first;
    		double maxVal = list.get(list.size()-1).first + histogrammFloorStep;
    		double interval = maxVal - minVal;
    		double count = interval / histogrammFloorStep;
    		while(count > 10){
    			count /= 2;
    		}
    		double step = Math.floor(interval / count);
    		plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, histogrammFloorStep);
    		plot.setTicksPerDomainLabel((int)(step / histogrammFloorStep));
		} else {
    		plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, histogrammFloorStep);
		}
		// обновляем серию
		updateRCSeries();
    
    	// задаем новую серию
    	plot.addSeries(series, formatter);
    	
    	plot.redraw();
	}
	
	private void updateRCSeries() {
		series = new SimpleXYSeries("");
		SimpleXYSeries tempSeries = new SimpleXYSeries("");

		for (Pair<Double, Integer> s : list) {
			tempSeries.addLast(s.first, s.second);
		}
		
		for (int i = 0; i < tempSeries.size(); i++){
			series.addLast(tempSeries.getX(i), -1);
			
			series.addLast(tempSeries.getX(i), tempSeries.getY(i));
		
			series.addLast(tempSeries.getX(i).longValue() + histogrammFloorStep, tempSeries.getY(i));
			series.addLast(tempSeries.getX(i).longValue() + histogrammFloorStep, -1);
		}
	}
	
	public XYPlotZoomPan getPlot(){
		return plot;
	}
}
