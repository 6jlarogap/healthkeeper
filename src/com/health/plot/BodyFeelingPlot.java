package com.health.plot;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.health.data.IFeeling;
import com.health.data.IFeelingTypeInfo;
import com.health.data.UnitDimension;

public class BodyFeelingPlot extends DataPlot  {
	private SimpleXYSeries bodyFeelingSeries;
	private LineAndPointFormatter bodyFeelingFormatter;
	List<IFeeling> dataList;
	IFeelingTypeInfo feelingTypeInfo;

	public BodyFeelingPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, double yAxisMin, double yAxisMax, double yAxisStep, IFeelingTypeInfo feelingTypeInfo, List<IFeeling> feelingList) {
	    super(context, plotDateFrom, plotDateTo, markedDate, name, unit, yAxisMin, yAxisMax, yAxisStep);
	    this.feelingTypeInfo = feelingTypeInfo;
	    
	    bodyFeelingFormatter = new LineAndPointFormatter(null, feelingTypeInfo.getColor(), null, null);
	    bodyFeelingFormatter.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(7));
		if(feelingTypeInfo.getUnitId() != UnitDimension.BOOLEAN_TYPE){
			bodyFeelingFormatter.setPointLabelFormatter(new PointLabelFormatter(Color.BLACK,13,4));
			bodyFeelingFormatter.setPointLabeler(new PointLabeler(){

        		@Override
                public String getLabel(XYSeries arg0, int arg1) {
        	        return PlotService.numFormat.format(arg0.getY(arg1));
                }
        	});
		}
	    updateData(feelingList);
	    
	    if(feelingTypeInfo.getUnitId() == UnitDimension.BOOLEAN_TYPE){
			plot.setRangeUpperBoundary(1, BoundaryMode.FIXED);
			plot.setRangeLowerBoundary(-1, BoundaryMode.FIXED);
		} else {
			double max = getMaxValue(bodyFeelingSeries);
			double min = getMinValue(bodyFeelingSeries);
			if(min == max){
				plot.setRangeUpperBoundary(max + 1, BoundaryMode.FIXED);
				plot.setRangeLowerBoundary(min - 1, BoundaryMode.FIXED);
			} else {
    			plot.setRangeUpperBoundary(max + (max - min) * 0.30, BoundaryMode.FIXED);
    			plot.setRangeLowerBoundary(min - (max - min) * 0.15, BoundaryMode.FIXED);
			}
		}
		plot.getGraphWidget().getRangeLabelPaint().setAlpha(0);
    }

	@Override
    public void updateData(List<?> ... data) {
		if(data != null){
			this.dataList = (List<IFeeling>) data[0];
		}
		if(!this.plot.isEmpty()){
			this.plot.clear();
		}
		
		// обновляем серию
    	updateBodyFeelingSeries();
    
    	// задаем новую серию
    	plot.addSeries(bodyFeelingSeries, bodyFeelingFormatter);
    	
    	plot.redraw();
	    
    }
	
	private void updateBodyFeelingSeries(){

		bodyFeelingSeries = new SimpleXYSeries("");
		if(dataList != null){
    		if(feelingTypeInfo.getUnitId() == UnitDimension.BOOLEAN_TYPE){
        		for(IFeeling feeling : dataList){
        			bodyFeelingSeries.addLast(feeling.getStartDate().getTime(), 0);
        		}
    		} else if(feelingTypeInfo.getUnitId() == UnitDimension.NUMBER_TYPE){
    			for(IFeeling feeling : dataList){
    				bodyFeelingSeries.addLast(feeling.getStartDate().getTime(), feeling.getValue1());
        		}
    		} else if(feelingTypeInfo.getUnitId() == UnitDimension.NUMBER_NUMBER_TYPE){
    			for(IFeeling feeling : dataList){
    				bodyFeelingSeries.addLast(feeling.getStartDate().getTime(), feeling.getValue1());
    				bodyFeelingSeries.addLast(feeling.getStartDate().getTime(), feeling.getValue2());
        		}
    		}
		}
	}
	
	double getMaxValue(SimpleXYSeries series){
    	double max = Double.MIN_VALUE;
    	
    	for(int i = 0; i < series.size(); i++){
    		if(series.getY(i).doubleValue() > max) {
    			max = series.getY(i).doubleValue();
    		}
    	}
    	
    	return max;
    }
    
    double getMinValue(SimpleXYSeries series){
    	double min = Double.MAX_VALUE;
    	
    	for(int i = 0; i < series.size(); i++){
    		if(series.getY(i).doubleValue() < min) {
    			min = series.getY(i).doubleValue();
    		}
    	}
    	
    	return min;
    }

}
