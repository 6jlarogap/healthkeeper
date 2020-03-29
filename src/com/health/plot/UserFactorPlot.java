package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.androidplot.LineRegion;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.TextOrientationType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.health.data.CustomFactorType;
import com.health.data.Factor;
import com.health.data.FactorType;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class UserFactorPlot extends DataPlot {
	HashMap<String,SimpleXYSeries> seriesList = new HashMap<String,SimpleXYSeries>();
	HashMap<String,UserFactorPlotBarFormatter> formatterList = new HashMap<String,UserFactorPlotBarFormatter>();

	private TextLabelWidget selectionWidget;
	
	
	UserFactorPlotBarFormatter selectionFormatter;
	private Pair<Integer, XYSeries> selection;
	List<Factor> dataList = new ArrayList<Factor>();
	List<FactorType> listFactorType = new ArrayList<FactorType>();
	List<CustomFactorType> listCustomFactorType = new ArrayList<CustomFactorType>();
	long factorGroupId;
	long sd = 1;

	public UserFactorPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, double yAxisMin, double yAxisMax, double yAxisStep, List<Factor> dataList, List<FactorType> factorTypes, List<CustomFactorType> customFactorTypes, long fgId, int layoutHelpDialogId) {
	    super(context, plotDateFrom, plotDateTo, markedDate, name, unit, yAxisMin, yAxisMax, yAxisStep);
	    
	    this.layoutHelpDialogId = layoutHelpDialogId;
	    this.dataList = dataList;
	    this.listFactorType = factorTypes;
	    this.listCustomFactorType = customFactorTypes;
	    this.factorGroupId = fgId;
	    
	    if(plotDateTo.getTime() - plotDateFrom.getTime() <= 3 * PlotService.DAY){
	    	sd = 1000 * 60 * 60;
	    } else if(plotDateTo.getTime() - plotDateFrom.getTime() > 3 * PlotService.DAY &&
	    		plotDateTo.getTime() - plotDateFrom.getTime() < 10 * PlotService.DAY){
	    	sd = 1000 * 60 * 60 * 12;
	    } else {
	    	sd = 1000 * 60 * 60 * 24;
	    }
	    
	    selectionWidget = new TextLabelWidget(plot.getLayoutManager(), "",
                new SizeMetrics(
                        PixelUtils.dpToPix(100), SizeLayoutType.ABSOLUTE,
                        PixelUtils.dpToPix(100), SizeLayoutType.ABSOLUTE),
                TextOrientationType.HORIZONTAL);

        selectionWidget.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));
        selectionWidget.getLabelPaint().setColor(Color.BLACK);
        selectionWidget.position(
                0, XLayoutStyle.RELATIVE_TO_CENTER,
                PixelUtils.dpToPix(5), YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.TOP_MIDDLE);
        selectionWidget.pack();
	    
	    initSeries();
	    initFormatters();
	    selectionFormatter = new UserFactorPlotBarFormatter(Color.WHITE, Color.BLACK);
	    
	    double y = 0;
	    if(listFactorType.size() > 0 || listCustomFactorType.size() > 0)
	    {
    	    for(Factor f : this.dataList){
    	    	if((f.getFactorType() != null ? f.getFactorType().getFactorGroupId() : f.getCustomFactorType().getFactorGroupId()) == factorGroupId) {
    	    	    
        	    	y = calcY(f);
        	    	
        	    	
        	    	long factorIndex = 0;
        	    	String factorGroupName = "";
        	    	String factorTypeName = "";
        	    	
        	    	if (f.getFactorType() != null){
        	    		factorIndex = f.getFactorType().getOrdinalNumber();
        	    		factorGroupName = f.getFactorType().getFactorGroup().getName();
        	    		factorTypeName = f.getFactorType().getName();
        	    	} else {
        	    		factorIndex = f.getCustomFactorType().getOrdinalNumber();
        	    		factorGroupName = f.getCustomFactorType().getFactorGroup().getName();
        	    		factorTypeName = f.getCustomFactorType().getName();
        	    	}
        	    	
        	    	if(seriesList.get(factorGroupName + ":" + factorTypeName) != null){
            	    	seriesList.get(factorGroupName + ":" + factorTypeName).addLast((f.getStartDate().getTime() / sd) * sd, y);
            	    	seriesList.get(factorGroupName + ":" + factorTypeName).setTitle(factorIndex + " - " + factorGroupName + ": " + factorTypeName);
        	    	}
        	    	
    	    	}
    	    }
	    }

		plot.getRangeLabelWidget().setVisible(false);
		plot.getGraphWidget().getRangeLabelPaint().setAlpha(0);

		plot.AddOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                	PointF point = new PointF(motionEvent.getX(), motionEvent.getY());
                	
                	if (plot.getGraphWidget().getGridRect() != null && plot.getGraphWidget().getGridRect().contains(point.x, point.y)) {
                		Number x = plot.getXVal(point);
                        Number y = plot.getYVal(point);


                        selection = null;
                        double xDistance = 0;
                        double yDistance = 0;
                        
                     // find the closest value to the selection:
                        for (XYSeries series : plot.getSeriesSet()) {
                            for (int i = 0; i < series.size(); i++) {
                                Number thisX = series.getX(i);
                                Number thisY = series.getY(i);
                                if (thisX != null && thisY != null) {
                                    double thisXDistance =
                                            LineRegion.measure(x, thisX).doubleValue();
                                    double thisYDistance =
                                            LineRegion.measure(y, thisY).doubleValue();
                                    if (selection == null && 
                                    	Math.abs(thisXDistance - xDistance) < (PlotService.HOUR) &&
                                    	thisY.doubleValue() - y.doubleValue() >= 0) {
                                        selection = new Pair<Integer, XYSeries>(i, series);
                                        xDistance = thisXDistance;
                                        yDistance = thisYDistance;
                                    } else if (thisXDistance < xDistance) {
                                        selection = new Pair<Integer, XYSeries>(i, series);
                                        xDistance = thisXDistance;
                                        yDistance = thisYDistance;
                                    } else if (thisXDistance == xDistance &&
                                            thisYDistance < yDistance &&
                                            thisY.doubleValue() >= y.doubleValue()) {
                                        selection = new Pair<Integer, XYSeries>(i, series);
                                        xDistance = thisXDistance;
                                        yDistance = thisYDistance;
                                    }
                                }
                            }
                        }
                	} else {
                    // if the press was outside the graph area, deselect:
                		selection = null;
                	}
                	
                	if(selection == null) {
                        selectionWidget.setText("");
                    } else {
                        selectionWidget.setText(selection.second.getTitle());
                    }
                } 
                plot.redraw();
                return true;
            }
        });

	    addSeries();
	    
	 // Setup the BarRenderer with our selected options
	    UserFactorPlotBarRenderer renderer = ((UserFactorPlotBarRenderer)plot.getRenderer(UserFactorPlotBarRenderer.class));
	    if(renderer != null) {
    	    renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.OVERLAID);
            renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.FIXED_WIDTH);
            renderer.setBarWidth(30);
            renderer.setBarGap(30);
	    }
	    
        /*
	    MyBarRenderer renderer = ((MyBarRenderer)plot.getRenderer(MyBarRenderer.class));
        renderer.setBarWidthStyle(MyBarRenderer.BarWidthStyle.FIXED_WIDTH);
        renderer.setBarWidth(30);*/
    }
	
	private double calcY(Factor factor){
		double result = 0;
		int count = 0;
		int j = 1;
		
		for(int i = 0; i < this.dataList.size() && factor.getStartDate().getTime() / sd >= dataList.get(i).getStartDate().getTime() / sd; i++){
			if(dataList.get(i).getStartDate().getTime() / sd == factor.getStartDate().getTime() / sd && (factor.getFactorType() != null ? factor.getFactorType().getFactorGroupId() : factor.getCustomFactorType().getFactorGroupId()) == (dataList.get(i).getFactorType() != null ? dataList.get(i).getFactorType().getFactorGroupId() : dataList.get(i).getCustomFactorType().getFactorGroupId())){
				count++;
			}
		}
		
		for(int i = 0; i < this.dataList.size() && factor != dataList.get(i); i++){
			if(dataList.get(i).getStartDate().getTime() / sd == factor.getStartDate().getTime() / sd && (factor.getFactorType() != null ? factor.getFactorType().getFactorGroupId() : factor.getCustomFactorType().getFactorGroupId()) == (dataList.get(i).getFactorType() != null ? dataList.get(i).getFactorType().getFactorGroupId() : dataList.get(i).getCustomFactorType().getFactorGroupId())){
				j++;
			}
		}
		
		result = 1.0 / (double)count * (double)j;
		
		return result;
	}
	
	private void initSeries(){
		for(FactorType ft : listFactorType){
			SimpleXYSeries s = new SimpleXYSeries("");
			seriesList.put(ft.getFactorGroup().getName() + ":" + ft.getName(), s);
		}

		for(CustomFactorType cft : listCustomFactorType){
			SimpleXYSeries s = new SimpleXYSeries("");
			seriesList.put(cft.getFactorGroup().getName() + ":" + cft.getName(), s);
		}
	}
	
	private void initFormatters(){
		for(FactorType ft : listFactorType){
			UserFactorPlotBarFormatter f = new UserFactorPlotBarFormatter(ft.getColor(), Color.BLACK);
		    f.setPointLabelFormatter(new PointLabelFormatter(Color.BLACK, 0, 15));
		    f.setPointLabeler(new PointLabeler(){
		    		@Override
		            public String getLabel(XYSeries arg0, int arg1) {
		    	        return arg0.getTitle().substring(0, arg0.getTitle().indexOf(" "));
		            }
		    });
		    formatterList.put(ft.getFactorGroup().getName() + ":" + ft.getName(), f);
		}

		for(CustomFactorType cft : listCustomFactorType){
			UserFactorPlotBarFormatter f = new UserFactorPlotBarFormatter(cft.getColor(), Color.BLACK);
		    f.setPointLabelFormatter(new PointLabelFormatter(Color.BLACK, 0, 15));
		    f.setPointLabeler(new PointLabeler(){
		    		@Override
		            public String getLabel(XYSeries arg0, int arg1) {
		    	        return arg0.getTitle().substring(0, arg0.getTitle().indexOf(" "));
		            }
		    });
		    formatterList.put(cft.getFactorGroup().getName() + ":" + cft.getName(), f);
		}
	}
	
	private void addSeries(){

		for(FactorType ft : listFactorType){
		    plot.addSeries(seriesList.get(ft.getFactorGroup().getName() + ":" + ft.getName()), formatterList.get(ft.getFactorGroup().getName() + ":" + ft.getName()));
		}
		for(CustomFactorType cft : listCustomFactorType){
		    plot.addSeries(seriesList.get(cft.getFactorGroup().getName() + ":" + cft.getName()), formatterList.get(cft.getFactorGroup().getName() + ":" + cft.getName()));
		}
	} 

	@Override
	public void updateData(List<?> ... data){
	    
    }
	
	class UserFactorPlotBarFormatter extends BarFormatter {
        public UserFactorPlotBarFormatter(int fillColor, int borderColor) {
            super(fillColor, borderColor);
        }

        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
            return UserFactorPlotBarRenderer.class;
        }

        @Override
        public SeriesRenderer getRendererInstance(XYPlot plot) {
            return new UserFactorPlotBarRenderer(plot);
        }
    }

    class UserFactorPlotBarRenderer extends BarRenderer<UserFactorPlotBarFormatter> {

        public UserFactorPlotBarRenderer(XYPlot plot) {
            super(plot);
        }

        @Override
        public UserFactorPlotBarFormatter getFormatter(int index, XYSeries series) { 
            if(selection != null &&
                    selection.second == series && 
                    selection.first == index) {
            	selectionFormatter.setPointLabelFormatter(new PointLabelFormatter(Color.BLACK, 0, 15));
            	selectionFormatter.setPointLabeler(new PointLabeler(){
		    		@Override
		            public String getLabel(XYSeries arg0, int arg1) {
		    	        return selection.second.getTitle().substring(0, selection.second.getTitle().indexOf(" "));
		            }
            	});
        	    return selectionFormatter;
            } else {
                return getFormatter(series);
            }
        }
    }

}
