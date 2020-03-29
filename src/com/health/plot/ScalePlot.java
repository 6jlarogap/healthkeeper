package com.health.plot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.androidplot.xy.BoundaryMode;

import android.content.Context;
import android.graphics.Color;

public class ScalePlot extends FactorPlot implements IScalePlot{

	private static final double TIME_FORMAT_BORDER = 24L * 60L * 60L * 1000L;
	
	private static final String TIME_CHART_FORMAT_1 = "dd-MM-yyyy HH:mm";
	private static final String TIME_CHART_FORMAT_2 = "dd-MM-yyyy";
	
	private SimpleDateFormat domainLabelFormat_DateTime = new SimpleDateFormat(TIME_CHART_FORMAT_1);
	private SimpleDateFormat domainLabelFormat_Date = new SimpleDateFormat(TIME_CHART_FORMAT_2);

	private SimpleDateFormat domainLabelFormat;
	private float domainLabelOrientation;
	
	public ScalePlot(Context context, Date dateFrom, Date dateTo, Date marked){
		super(context, dateFrom, dateTo, marked);
		configurePlot();
	}
	
	protected void configurePlot(){
		super.configurePlot();
		
		plot.getGraphWidget().setDomainLabelOrientation(domainLabelOrientation);
		
		plot.setDomainValueFormat(new Format() {

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

				long timestamp = ((Number) obj).longValue();
				Date date = new Date(timestamp);
				return domainLabelFormat.format(date, toAppendTo, pos);
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;

			}
		});
		
		plot.getGraphWidget().setMarginBottom(10);
		plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
		plot.getGraphWidget().getRangeLabelPaint().setAlpha(0);
	}
	
	protected void updateDomainLabelFormat() {
		long interval = plotDateTo.getTime() - plotDateFrom.getTime();
		if (interval > TIME_FORMAT_BORDER) {
			domainLabelFormat = domainLabelFormat_Date;
		} else {
			domainLabelFormat = domainLabelFormat_DateTime;
		}
	}
	
	protected void updateDomainLabelOrientation() {
		long interval = plotDateTo.getTime() - plotDateFrom.getTime();
		float orientation = 0;

		if (interval > TIME_FORMAT_BORDER) {
			orientation = 0;
		} else if (interval > 16 * 60 * 60 * 1000) {
			orientation = -10;
		}

		domainLabelOrientation = orientation;
	}

	@Override
	public void updateUI(Date dtFrom, Date dtTo, long domainStep, int ticksPerDomainLabel){
		plotDateFrom = dtFrom;
		plotDateTo = dtTo;
		plot.setDomainLowerBoundary(dtFrom.getTime(), BoundaryMode.FIXED);
		plot.setDomainUpperBoundary(dtTo.getTime(), BoundaryMode.FIXED);
		yAxisMarker.setValue(plotDateFrom.getTime());
		
		updateDomainLabelOrientation();
		updateDomainLabelFormat();
		
		boolean isNeedChangeDomainLabelOrientation = (plot.getGraphWidget().getDomainLabelOrientation() != domainLabelOrientation);
		boolean isNeedChangeDomainStep = (plot.getDomainStepValue() != domainStep);
		boolean isNeedChangeTicksPerDomainLabel = (plot.getTicksPerDomainLabel() != ticksPerDomainLabel);
		
		if (isNeedChangeDomainLabelOrientation) {
			plot.getGraphWidget().setDomainLabelOrientation(domainLabelOrientation);
		}
		if (isNeedChangeDomainStep) {
			plot.setDomainStepValue(domainStep);
		}
		if (isNeedChangeTicksPerDomainLabel){
			plot.setTicksPerDomainLabel(ticksPerDomainLabel);
		}
		
		plot.redraw();
	}
}
