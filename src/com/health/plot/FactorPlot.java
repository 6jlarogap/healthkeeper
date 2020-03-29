package com.health.plot;

import java.util.Date;

import com.androidplot.Plot.BorderStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.ui.YPositionMetric;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XValueMarker;
import com.androidplot.xy.XYStepMode;

import android.content.Context;
import android.graphics.Color;

public abstract class FactorPlot extends Plot implements IFactorPlot {

	protected Date plotDateFrom;
	protected Date plotDateTo;
	protected Date markedDate = new Date();
	protected Date originDate;

	protected XValueMarker markedDateMarker = new XValueMarker(0, " ", null, Color.BLUE, Color.BLUE);
	protected XValueMarker yAxisMarker = new XValueMarker(0, " ", null, Color.GRAY, Color.GRAY);
	
	FactorPlot(Context context, Date dateFrom, Date dateTo, Date marked) {
	    super(context);

		plotDateFrom = dateFrom;
		plotDateTo = dateTo;
		markedDate = marked;
	    originDate = new Date((markedDate.getTime() / 1000 / 60 / 60 - (markedDate.getTime() / 1000 / 60 / 60 % 24)) * 1000 * 60 * 60 + markedDate.getTimezoneOffset() * 60 * 1000);
	}
	
	protected void configurePlot(){
		plot.setPlotMargins(0, 0, 0, 0);
		plot.setPlotPadding(0, 0, 0, 0);
		plot.getBackgroundPaint().setColor(Color.WHITE);
		plot.setBorderStyle(BorderStyle.ROUNDED, 10f, 10f);
		plot.getBorderPaint().setAlpha(0);

		plot.getGraphWidget().setMargins(10, 10, 0, 0);
		plot.getGraphWidget().setHeight(1f);
		plot.getGraphWidget().setWidth(1f);
		plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
		plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
		plot.getGraphWidget().setRangeValueFormat(PlotService.numFormat);

		plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
		plot.getTitleWidget().setVisible(false);
		plot.getLegendWidget().setVisible(false);
		plot.getGraphWidget().getDomainSubGridLinePaint().setColor(Color.rgb(200, 200, 200));
		plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.rgb(100, 100, 100));
		plot.getGraphWidget().getRangeGridLinePaint().setAlpha(0);

		plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 3L * PlotService.HOUR);
		plot.setTicksPerDomainLabel(8);

		if(markedDate != null){
    		markedDateMarker.setTextPosition(new YPositionMetric(PixelUtils.dpToPix(0), YLayoutStyle.ABSOLUTE_FROM_TOP));
    		markedDateMarker.getTextPaint().setColor(Color.BLUE);
    		markedDateMarker.setValue(markedDate.getTime());
    		plot.addMarker(markedDateMarker);
		}

		yAxisMarker.setTextPosition(new YPositionMetric(PixelUtils.dpToPix(5), YLayoutStyle.ABSOLUTE_FROM_CENTER));
		yAxisMarker.setValue(plotDateFrom.getTime());
		plot.addMarker(yAxisMarker);
		
		plot.setUserDomainOrigin(originDate.getTime());
		plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.rgb(100, 100, 100));
		plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.GRAY);

		plot.setDomainUpperBoundary(plotDateTo.getTime(), BoundaryMode.FIXED);
		plot.setDomainLowerBoundary(plotDateFrom.getTime(), BoundaryMode.FIXED);
	}
	
	
}
