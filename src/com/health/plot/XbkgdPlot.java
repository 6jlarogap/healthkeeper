package com.health.plot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.health.data.HelioPhysics;
import com.health.data.HelioPhysicsType;
import com.health.main.R;

public class XbkgdPlot extends DataPlot {
	private SimpleXYSeries xbkgdSeries;
	
	private LineAndPointFormatter xbkgdFormatter;
	
	private List<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
	
	public XbkgdPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<HelioPhysics> valueList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);
		
		ownSerieses.add(xbkgdSeries);
		
		dataTypeId = HelioPhysicsType.XBKGD_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_xbkgd;
		setupXbkgdPlot();
		// получаем форматтер для серии
    	xbkgdFormatter = getLineAndPointSeriesFormatter(HelioPhysicsType.XBKGD_TYPE_COLOR, HelioPhysicsType.XBKGD_TYPE_COLOR, HelioPhysicsType.XBKGD_TYPE_COLOR,
    			PlotService.plf, new PointLabeler(){
			@Override
	        public String getLabel(XYSeries arg0, int arg1) {
				String result;
				Date nowDate = new Date();
				long originDate = (nowDate.getTime() / 1000 / 60 / 60 - (nowDate.getTime() / 1000 / 60 / 60 % 24)) * 1000 * 60 * 60 + nowDate.getTimezoneOffset() * 60 * 1000;
				
				if((arg0.getX(arg1).longValue() - originDate + nowDate.getTimezoneOffset() * 60 * 1000) % plot.getDomainStepValue() == 0) {
					result = helioPhysicsList.get(arg1).getXbkgd();
				} else {
					result = "";
				}
		        return result;
	        }
		}, null, null, null, 3, 5);
    	
		updateData(valueList);
	}
	
	private void setupXbkgdPlot(){
		plot.setIndentY(0.0000001, 0.0000001);
		plot.setRangeValueFormat(new Format() {

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				StringBuffer result = new StringBuffer();
				Number num = (Number) obj;
				String s = PlotService.expFormat.format(num.doubleValue());
				String [] m = s.split("E");
				
				if(m[1].equals("-8")){
					result.append("A" + m[0]);
				} else if(m[1].equals("-7")){
					result.append("B" + m[0]);
				} else if(m[1].equals("-6")){
					result.append("C" + m[0]);
				} else if(m[1].equals("-5")){
					result.append("M" + m[0]);
				} else if(m[1].equals("-4")){
					result.append("X" + m[0]);
				} else{
					result.append(s);
				}
				
				return result;
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;

			}
		});
		plot.AddOnChangeDomainBoundaryListener(new OnChangeDomainBoundaryListener(){
			@Override
			public void OnChangeDomainBoundary(){
				updateRangeStep();
			}
		});
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof HelioPhysics){
				helioPhysicsList = (List<HelioPhysics>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
    	updateXbkgdSeries();

    	// задаем новую серию
    	plot.addSeries(xbkgdSeries, xbkgdFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
    	
    	plot.redraw();
	}
	
	private void updateXbkgdSeries() {
		ownSerieses.remove(xbkgdSeries);
		xbkgdSeries = new SimpleXYSeries("");
		ownSerieses.add(xbkgdSeries);

		for (HelioPhysics hp : helioPhysicsList) {
			if (hp.getXbkgd() != null && (xbkgdSeries.size() == 0 || hp.getInfoDate().getTime() - xbkgdSeries.getX(xbkgdSeries.size() - 1).longValue() >= MIN_TIME_INTERVAL)) {
				double d = 0;
				if(hp.getXbkgd().substring(0, 1).equals("A")){
					d = Double.parseDouble(hp.getXbkgd().substring(1)) * Math.pow(10, -8);
				} else if(hp.getXbkgd().substring(0, 1).equals("B")){
					d = Double.parseDouble(hp.getXbkgd().substring(1)) * Math.pow(10, -7);
				} else if(hp.getXbkgd().substring(0, 1).equals("C")){
					d = Double.parseDouble(hp.getXbkgd().substring(1)) * Math.pow(10, -6);
				} else if(hp.getXbkgd().substring(0, 1).equals("M")){
					d = Double.parseDouble(hp.getXbkgd().substring(1)) * Math.pow(10, -5);
				} else if(hp.getXbkgd().substring(0, 1).equals("X")){
					d = Double.parseDouble(hp.getXbkgd().substring(1)) * Math.pow(10, -4);
				}
				xbkgdSeries.addLast(hp.getInfoDate().getTime(), d);
			}
		}
	}
}
