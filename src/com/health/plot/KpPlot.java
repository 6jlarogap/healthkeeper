package com.health.plot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYRegionFormatter;
import com.androidplot.xy.XYSeries;
import com.health.data.GeoPhysics;
import com.health.data.GeoPhysicsType;
import com.health.data.KpIndex;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.repository.IRepository;

public class KpPlot extends DataPlot {
	private SimpleXYSeries KpSeries;
	
	private LineAndPointFormatter KpFormatter;
	
	private List<GeoPhysics> geoPhysicsList = new ArrayList<GeoPhysics>();
	private List<KpIndex> KpIndicies;
	
	public KpPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, double yAxisMin, double yAxisMax, double yAxisStep, List<GeoPhysics> geoPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, yAxisMin, yAxisMax, yAxisStep);

		ownSerieses.add(KpSeries);
		
		dataTypeId = GeoPhysicsType.KP_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_kp;
		
		initKpIndicies();
		setupKpPlot();

		updateData(geoPhysicsList);
	}
	
	private void setupKpPlot(){
		plot.getGraphWidget().getRangeLabelPaint().setTextSize(10);
		plot.setRangeValueFormat(new Format() {

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				StringBuffer result = new StringBuffer();
				Number num = (Number) obj;
				result.append(getKpById(num.intValue()));
				return result;
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		});
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof GeoPhysics){
				geoPhysicsList = (List<GeoPhysics>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateKpSeries();

		// создаем регионы
		List<RectRegion> regions = new ArrayList<RectRegion>();
		List<XYRegionFormatter> regionFormatters = new ArrayList<XYRegionFormatter>();
		for (int i = 0; i < KpSeries.size() - 1; ++i) {
			regions.add(new RectRegion(KpSeries.getX(i), KpSeries.getX(i + 1), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "Short"));
			regionFormatters.add(new XYRegionFormatter(getColorForKp(KpSeries.getY(i).intValue())));
		}
		// получаем форматтер для серии
		KpFormatter = getStepSeriesFormatter(GeoPhysicsType.KP_TYPE_COLOR, GeoPhysicsType.KP_TYPE_COLOR, null, PlotService.plf, new PointLabeler() {

			@Override
			public String getLabel(XYSeries arg0, int arg1) {
				String result = "";
				
				if((arg0.getX(arg1).longValue() - originDate.getTime()) % plot.getDomainStepValue() == 0) {
    				result = getKpById(arg0.getY(arg1).intValue());
				}
				return result;
			}

		}, null, regions, regionFormatters, 1, 2);
				
    	// задаем новую серию
    	plot.addSeries(KpSeries, KpFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}
    	
    	plot.redraw();
	}
	
	private void updateKpSeries() {
		ownSerieses.remove(KpSeries);
		KpSeries = new SimpleXYSeries("");
		ownSerieses.add(KpSeries);

		for (GeoPhysics gp : geoPhysicsList) {
			if (gp.getKpId() != null) {
				KpSeries.addLast(gp.getInfoDate().getTime(), gp.getKpId());
			}
		}
	}
	
	public String getKpById(int KpId){
		String result = "";
		
		for(int i = 0; i < KpIndicies.size() && result.equals(""); i++){
			if(KpIndicies.get(i).getId() == KpId){
				result = KpIndicies.get(i).getStrValue();
			}
		}
		return result;
	}
	
	private int getColorForKp(int Kp) {
		int alpha = 100;
		int red = 0;
		int green = 0;
		int blue = 0;

		if (Kp < 12) {
			green = 255;
		} else if (Kp >= 12 && Kp < 18) {
			green = 255;
			red = 255;
		} else if (Kp >= 18) {
			red = 255;
		}

		return Color.argb(alpha, red, green, blue);
	}
	
	private void initKpIndicies(){
		IRepository mRepository = ((HealthApplication) ((Activity) this.mContext).getApplication()).getRepository();
		KpIndicies = mRepository.getKpIndicies(); 
	}
}
