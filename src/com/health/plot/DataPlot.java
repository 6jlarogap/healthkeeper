package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.XYRegionFormatter;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.health.data.WeatherType;

public abstract class DataPlot extends FactorPlot implements IDataPlot {

	
	public interface OnChangeRangeDateListener {
		void onChangeRangeDate(Date dtFrom, Date dtTo);
	};

	protected String name;
	protected String unit;
	protected String markedValue = "";
	protected double yAxisMin = -1;
	protected double yAxisMax = -1;
	protected double yAxisStep = -1;
	protected boolean isHaveHistogramm = false;
	protected int layoutHelpDialogId = 0;
	protected int dataTypeId = 0;
	
	protected boolean isHaveHourlyData = false;
	protected boolean isHaveDailyData = false;
	protected boolean isShowHourlyData = false;
	protected boolean isShowDailyData = false;
	protected boolean isShowHourlyChange = false;
	protected boolean isShowDailyChange = false;
	
	protected double histogrammFloorStep = 1;

	protected OnChangeRangeDateListener mOnChangeRangeDateListener;
		
	protected static final String TIME_CHART_FORMAT_1 = "dd-MM-yyyy HH:mm";
	protected static final String TIME_CHART_FORMAT_2 = "dd-MM-yyyy";
	
	protected List<SimpleXYSeries> ownSerieses = new ArrayList<SimpleXYSeries>();
	protected List<SimpleXYSeries> juxtaposeSerieses = new ArrayList<SimpleXYSeries>();
	
	protected IDataPlot juxtaposePlot;

	protected LineAndPointFormatter juxtaposeFormatter;

	protected PointLabeler POINT_LABELER = new PointLabeler(){
		@Override
        public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime()) % (plot.getDomainStepValue() * plot.getTicksPerDomainLabel()) == 0) {
				result = PlotService.numFormat.format(arg0.getY(arg1));
			}
			
	        return result;
        }
	};
	
	protected PointLabeler POINT_LABELER_FOR_DOMAIN_TICK = new PointLabeler(){
		@Override
        public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime()) % plot.getDomainStepValue() == 0) {
				result = PlotService.numFormat.format(arg0.getY(arg1));
			}
			
	        return result;
        }
	};
	
	protected PointLabeler POINT_LABELER_TIME = new PointLabeler() {
		@Override
		public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime()) % (plot.getDomainStepValue() * plot.getTicksPerDomainLabel()) == 0) {
				long timestamp = arg0.getY(arg1).longValue();
				Date date = new Date(timestamp);
				result = PlotService.timeFormat.format(date);
			}

			return result;
		}
	};

	protected PointLabeler POINT_LABELER_TIMEOFFSET = new PointLabeler(){
		@Override
        public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime() + originDate.getTimezoneOffset() * 60 * 1000) % (plot.getDomainStepValue() * plot.getTicksPerDomainLabel()) == 0) {
				result = PlotService.numFormat.format(arg0.getY(arg1));
			}
			
	        return result;
        }
	};

	protected PointLabeler POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK = new PointLabeler(){
		@Override
        public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime() + originDate.getTimezoneOffset() * 60 * 1000) % plot.getDomainStepValue() == 0) {
				result = PlotService.numFormat.format(arg0.getY(arg1));
			}
			
	        return result;
        }
	};

	protected PointLabeler POINT_LABELER_TIMEOFFSET_EXP = new PointLabeler(){
		@Override
        public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime() + originDate.getTimezoneOffset() * 60 * 1000) % (plot.getDomainStepValue() * plot.getTicksPerDomainLabel()) == 0) {
				result = PlotService.expFormat.format(arg0.getY(arg1));
			}
			
	        return result;
        }
	};

	protected PointLabeler POINT_LABELER_TIMEOFFSET_EXP_FOR_DOMAIN_TICK = new PointLabeler(){
		@Override
        public String getLabel(XYSeries arg0, int arg1) {
			String result = "";
			
			if((arg0.getX(arg1).longValue() - originDate.getTime() + originDate.getTimezoneOffset() * 60 * 1000) % plot.getDomainStepValue() == 0) {
				result = PlotService.expFormat.format(arg0.getY(arg1));
			}
			
	        return result;
        }
	};

	protected static long MIN_TIME_INTERVAL = 3L * 60L * 60L * 1000L;
	protected static final long VALUE_LIMIT = 100;
	
	public DataPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit){
		this(context, plotDateFrom, plotDateTo, markedDate, name, unit, -1, -1, -1);
	}
	
	public DataPlot(Context context, Date dateFrom, Date dateTo, Date marked, String nm, String un, double yMin, double yMax, double yStep){
		super(context, dateFrom, dateTo, marked);
		
		name = nm;
		unit = un;
		yAxisMin = yMin;
		yAxisMax = yMax;
		yAxisStep = yStep;

		juxtaposeFormatter = getLineAndPointSeriesFormatter(WeatherType.PRESSURE_TYPE_COLOR, WeatherType.PRESSURE_TYPE_COLOR, WeatherType.PRESSURE_TYPE_COLOR, null, null, null, null, null, 1, 2);

		configurePlot();
	}
	
	protected void configurePlot() {
		super.configurePlot();
		
		plot.getGraphWidget().getDomainLabelPaint().setAlpha(0);
		plot.setRangeLabel(unit);

		plot.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
		plot.getRangeLabelWidget().setVisible(true);
		
		plot.setZoomEnabled(false);
		
		if(yAxisMax != -1){
			plot.setRangeUpperBoundary(yAxisMax, BoundaryMode.FIXED);
		}
		if(yAxisMin != -1){
			plot.setRangeLowerBoundary(yAxisMin, BoundaryMode.FIXED);
		}
		if(yAxisStep != -1){
			plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, yAxisStep);
		} else {
			updateRangeStep();
		}
	}
	
	static protected LineAndPointFormatter getLineAndPointSeriesFormatter(Integer lineColor, Integer vertexColor, Integer fillColor, PointLabelFormatter plf, PointLabeler pl, Shader shader,
        List<RectRegion> regions, List<XYRegionFormatter> regionFormatters, float lineWidth, float vertexWidth) {
		LineAndPointFormatter seriesFormatter = new LineAndPointFormatter(lineColor, vertexColor, fillColor, plf);

		seriesFormatter.getLinePaint().setStrokeWidth(PixelUtils.dpToPix(lineWidth));
		seriesFormatter.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(vertexWidth));
		seriesFormatter.setPointLabeler(pl);

		if (shader != null) {
			seriesFormatter.setFillPaint(new Paint());
			seriesFormatter.getFillPaint().setShader(shader);
		}

		if (seriesFormatter.getFillPaint() != null)
			seriesFormatter.getFillPaint().setAlpha(100);

		if (regions != null && regionFormatters != null && regions.size() == regionFormatters.size())
			for (int i = 0; i < regions.size(); ++i)
				seriesFormatter.addRegion(regions.get(i), regionFormatters.get(i));

		return seriesFormatter;
	}
	
	protected StepFormatter getStepSeriesFormatter(Integer lineColor, Integer vertexColor, Integer fillColor, PointLabelFormatter plf, PointLabeler pl, Shader shader, List<RectRegion> regions,
        List<XYRegionFormatter> regionFormatters, float lineWidth, float vertexWidth) {
		StepFormatter seriesFormatter = new StepFormatter(lineColor, fillColor);
		seriesFormatter.getVertexPaint().setColor(vertexColor);
		seriesFormatter.setPointLabelFormatter(plf);

		seriesFormatter.getLinePaint().setStrokeWidth(PixelUtils.dpToPix(lineWidth));
		seriesFormatter.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(vertexWidth));

		if (shader != null) {
			seriesFormatter.setFillPaint(new Paint());
			seriesFormatter.getFillPaint().setShader(shader);
		}

		if (seriesFormatter.getFillPaint() != null)
			seriesFormatter.getFillPaint().setAlpha(100);

		if (regions != null && regionFormatters != null && regions.size() == regionFormatters.size())
			for (int i = 0; i < regions.size(); ++i)
				seriesFormatter.addRegion(regions.get(i), regionFormatters.get(i));

		seriesFormatter.setPointLabeler(pl);

		return seriesFormatter;
	}
	
	static protected MyBarFormatter getBarSeriesFormatter(Integer lineColor, Integer vertexColor, Integer fillColor, PointLabelFormatter plf, PointLabeler pl, Shader shader, List<RectRegion> regions,
        List<XYRegionFormatter> regionFormatters, float lineWidth, float vertexWidth) {
		MyBarFormatter seriesFormatter = new MyBarFormatter(lineColor, fillColor);
		seriesFormatter.setPointLabelFormatter(plf);

		seriesFormatter.getLinePaint().setStrokeWidth(PixelUtils.dpToPix(1));
		seriesFormatter.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(1));

		if (shader != null) {
			seriesFormatter.setFillPaint(new Paint());
			seriesFormatter.getFillPaint().setShader(shader);
		}

		if (seriesFormatter.getFillPaint() != null)
			seriesFormatter.getFillPaint().setAlpha(100);

		if (regions != null && regionFormatters != null && regions.size() == regionFormatters.size())
			for (int i = 0; i < regions.size(); ++i)
				seriesFormatter.addRegion(regions.get(i), regionFormatters.get(i));

		seriesFormatter.setPointLabeler(pl);

		return seriesFormatter;
	}
	
	public XYPlotZoomPan getPlot(){
		return plot;
	}
	
	public String getName(){
		return name;
	}
	
	public String getUnit(){
		return unit;
	}
	
	protected void updateMinTimeInterval() {
		long interval = plotDateTo.getTime() - plotDateFrom.getTime();
		long minTimeInterval = interval / VALUE_LIMIT;

		MIN_TIME_INTERVAL = minTimeInterval;
	}
	
	public void setOnChangeRangeDateListener(OnChangeRangeDateListener onChangeRangeDateListener) {
		this.mOnChangeRangeDateListener = onChangeRangeDateListener;
	}
	
	@Override
	public void updateUI(Date dtFrom, Date dtTo, long domainStep, int ticksPerDomainLabel){
		plotDateFrom = dtFrom;
		plotDateTo = dtTo;
		plot.setDomainLowerBoundary(dtFrom.getTime(), BoundaryMode.FIXED);
		plot.setDomainUpperBoundary(dtTo.getTime(), BoundaryMode.FIXED);
		yAxisMarker.setValue(plotDateFrom.getTime());
		
		updateMinTimeInterval();
		
		boolean isNeedChangeDomainStep = (plot.getDomainStepValue() != domainStep);
		boolean isNeedChangeTicksPerDomainLabel = (plot.getTicksPerDomainLabel() != ticksPerDomainLabel);
		
		if (isNeedChangeDomainStep) {
			plot.setDomainStepValue(domainStep);
		}
		if (isNeedChangeTicksPerDomainLabel){
			plot.setTicksPerDomainLabel(ticksPerDomainLabel);
		}
		
		plot.redraw();
	}
	
	public void setMarkedDate(Date dt){
		markedDateMarker.setValue(dt.getTime());
		plot.redraw();
	}
	
	public boolean isHaveHistogramm(){
		return isHaveHistogramm;
	}
	
	public int getLayoutHelpDialogId(){
		return layoutHelpDialogId;
	}

	public void setMarkedValue(String val){
		markedDateMarker.setText(val);
	}
	
	public int getDataTypeId(){
		return dataTypeId;
	}
	
	public double getHistogrammFloorStep(){
		return histogrammFloorStep;
	}
	
	public void setHistogrammFloorStep(double hfs){
		histogrammFloorStep = hfs;
	}
	
	protected void updateRangeStep(){ 
		plot.calculateMinMaxVals();
		double interval = plot.getCalculatedMaxY().doubleValue() - plot.getCalculatedMinY().doubleValue();
		double rangeStep = 0.0;
		
		if(interval >= 1.0){
			rangeStep = Math.ceil(interval / 9d);
		} else {
			rangeStep = interval / 9d;
		}
		
		if(rangeStep != plot.getRangeStepValue() && rangeStep != 0.0){
			plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, rangeStep);
		}
		
	}
	
	@Override
	public boolean isHaveHourlyData(){
		return isHaveHourlyData;
	}
	
	@Override
	public boolean isHaveDailyData(){
		return isHaveDailyData;
	}
	
	@Override
	public void showHourlyData(boolean isShow){
		
	}
	
	@Override
	public boolean isShowHourlyData(){
		return isShowHourlyData;
	}
	
	@Override
	public void showDailyData(boolean isShow){
		
	}
	
	@Override
	public boolean isShowDailyData(){
		return isShowDailyData;
	}
	
	@Override
	public void showHourlyChange(boolean isShow){
		
	}
	
	@Override
	public boolean isShowHourlyChange(){
		return isShowHourlyChange;
	}
	
	@Override
	public void showDailyChange(boolean isShow){
		
	}
	
	@Override
	public boolean isShowDailyChange(){
		return isShowDailyChange;
	}

	protected void updateJuxtaposeSeries(){
		juxtaposeSerieses.clear();
		plot.calculateMinMaxVals();
		juxtaposePlot.getPlot().calculateMinMaxVals();
		double interval1 = (plot.getCalculatedMaxY().doubleValue() - plot.getIndentMaxY().doubleValue()) - (plot.getCalculatedMinY().doubleValue() + plot.getIndentMinY().doubleValue());
		double interval2 = (juxtaposePlot.getPlot().getCalculatedMaxY().doubleValue() - juxtaposePlot.getPlot().getIndentMaxY().doubleValue()) - (juxtaposePlot.getPlot().getCalculatedMinY().doubleValue() + juxtaposePlot.getPlot().getIndentMinY().doubleValue());

		for(SimpleXYSeries s : juxtaposePlot.getOwnSerieses()){
        	SimpleXYSeries ns = new SimpleXYSeries("");
    		for(int i = 0; i < s.size(); i++){
    			double y = plot.getCalculatedMinY().doubleValue() + ((s.getY(i).doubleValue() - (juxtaposePlot.getPlot().getCalculatedMinY().doubleValue() + juxtaposePlot.getPlot().getIndentMinY().doubleValue())) / interval2) * interval1;
    			ns.addLast(s.getX(i), y);
    		}
    		juxtaposeSerieses.add(ns);
		}
		/*
        for(XYSeriesRenderer renderer : juxtaposePlot.getRendererList()) {
        	SimpleXYSeries js = (SimpleXYSeries) renderer.getSeriesAndFormatterList().getSeries(0);
        	SimpleXYSeries ns = new SimpleXYSeries("");
    		for(int i = 0; i < js.size(); i++){
    			double y = plot.getCalculatedMinY().doubleValue() + ((js.getY(i).doubleValue() - (juxtaposePlot.getCalculatedMinY().doubleValue() + juxtaposePlot.getIndentMinY().doubleValue())) / interval2) * interval1;
    			ns.addLast(js.getX(i), y);
    		}
    		juxtaposeSerieses.add(ns);
        }
		/*
		for(int i = 0; i < js.size(); i++){
			double y = plot.getCalculatedMinY().doubleValue() + ((js.getY(i).doubleValue() - minBorder) / interval2) * interval1;
			juxtaposeSeries.addLast(js.getX(i), y);
		}
		*/
	}

	
	protected void addJuxtaposeSeries(){
        for(SimpleXYSeries s : juxtaposeSerieses) {
        	plot.addSeries(s, juxtaposeFormatter);
        }
	}

	@Override
    public void setJuxtaposeSeries(IDataPlot jp){
		if(isHaveJuxtapose()){
			removeJuxtaposeSeries();
		}
		juxtaposePlot = jp;
		updateJuxtaposeSeries();
		addJuxtaposeSeries();
		plot.redraw();
	};
	
	@Override
    public void removeJuxtaposeSeries(){
        for(SimpleXYSeries s : juxtaposeSerieses) {
        	plot.removeSeries(s);
        }
		plot.redraw();
		juxtaposeSerieses.clear();
		juxtaposePlot = null;
	};

	@Override
    public boolean isHaveJuxtapose(){
		return juxtaposePlot != null;
	};

	@Override
	public List<SimpleXYSeries> getOwnSerieses(){
		return ownSerieses;
	};

    public void setJuxtapose(boolean isJuxtapose){
    	if(isJuxtapose){
        	plot.getBackgroundPaint().setAlpha(0);
    		plot.getGraphWidget().getBackgroundPaint().setAlpha(0);
    		plot.getGraphWidget().getGridBackgroundPaint().setAlpha(0);
    		plot.getGraphWidget().getRangeLabelPaint().setAlpha(0);
    	} else {
        	plot.getBackgroundPaint().setAlpha(255);
    		plot.getGraphWidget().getBackgroundPaint().setAlpha(255);
    		plot.getGraphWidget().getGridBackgroundPaint().setAlpha(255);
    		plot.getGraphWidget().getRangeLabelPaint().setAlpha(255);
    	}
    }
}
