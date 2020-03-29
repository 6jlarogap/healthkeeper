package com.health.plot;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.health.data.Complaint;

public class FeelingStatPlot extends DataPlot {
	SimpleXYSeries feelingStatSeries;
	LineAndPointFormatter feelingStatFormatter;
	List<Complaint> bodyComplaintList;

	public FeelingStatPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, double yAxisMin, double yAxisMax, double yAxisStep, List<Complaint> bodyComplaintList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, yAxisMin, yAxisMax, yAxisStep);		
		setupHumidityPlot();
		
		// получаем форматтер для серии
		feelingStatFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, Color.RED, new PointLabelFormatter(
		        Color.BLACK), new PointLabeler(){
    		@Override
            public String getLabel(XYSeries arg0, int arg1) {
    			String result = "";
    			
    			if((arg0.getY(arg1).longValue() > -1 && arg0.getY(arg1 - 1).longValue() == -1) && (arg0.getX(arg1).longValue() - originDate.getTime()) % (plot.getDomainStepValue()) == 0) {
    				result = PlotService.numFormat.format(arg0.getY(arg1));
    			}

    	        return result;
            }
		}, null, null, null, 1, 1);
		updateData(bodyComplaintList);
	}
	
	private void setupHumidityPlot() {
		plot.addZoomPanListener(new ZoomPanListener() {

			@Override
			public void zoomPanApplied(ZoomPanEvent e) {
				updateData(bodyComplaintList);
			}

			@Override
			public void zoomPanReset() {
			}

		});
	}

	public void updateData(List<?> ... data){
		if(data != null){
			this.bodyComplaintList = (List<Complaint>) data[0];
		}
		if(!this.plot.isEmpty()){
			this.plot.clear();
		}
		
		// обновляем серию
		updateFeelingStatSeries();
    
    	// задаем новую серию
    	plot.addSeries(feelingStatSeries, feelingStatFormatter);
    	
    	plot.redraw();
	}
	
	private void updateFeelingStatSeries() {		
		feelingStatSeries = new SimpleXYSeries("");
		SimpleXYSeries tempSeries = new SimpleXYSeries("");
		
		if(bodyComplaintList != null){
    		for (Complaint item : bodyComplaintList) {
    			tempSeries.addLast(item.getStartDate().getTime(), item.getCount());
    		}
		}
		
		for (int i = 0; i < tempSeries.size(); i++){
			feelingStatSeries.addLast(tempSeries.getX(i), -1);
			
			feelingStatSeries.addLast(tempSeries.getX(i), tempSeries.getY(i));
			
			if(i != tempSeries.size() - 1){
				feelingStatSeries.addLast((tempSeries.getX(i).longValue() + 24L * PlotService.HOUR) < tempSeries.getX(i + 1).longValue() ? (tempSeries.getX(i).longValue() + 24L * PlotService.HOUR) : tempSeries.getX(i + 1), tempSeries.getY(i));
				feelingStatSeries.addLast((tempSeries.getX(i).longValue() + 24L * PlotService.HOUR) < tempSeries.getX(i + 1).longValue() ? (tempSeries.getX(i).longValue() + 24L * PlotService.HOUR) : tempSeries.getX(i + 1), -1);
			} else {
				feelingStatSeries.addLast(tempSeries.getX(i).longValue() + 24L * PlotService.HOUR, tempSeries.getY(i));
				feelingStatSeries.addLast(tempSeries.getX(i).longValue() + 24L * PlotService.HOUR, -1);
			}
		}
	}
	
}
