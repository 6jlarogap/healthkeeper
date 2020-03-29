package com.health.plot;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.androidplot.xy.PointLabelFormatter;

import android.graphics.Color;

public class PlotService {
	public static final long HOUR = 60L * 60L * 1000L;
	public static final long DAY = 24L * HOUR;

	public static final DecimalFormat numFormat = new DecimalFormat("###.##");
	public static final DecimalFormat expFormat = new DecimalFormat("0.0E0");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
	public static final SimpleDateFormat hourFormat = new SimpleDateFormat("H ч.");
	public static final Format numPositiveFormat = new Format() {

		@Override
		public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
			StringBuffer result = new StringBuffer();
			Number num = (Number) obj;
			if(num.doubleValue() >= 0){
				result.append(numFormat.format(num));
			}
			return result;
		}

		@Override
		public Object parseObject(String source, ParsePosition pos) {
			return null;
		}
	};
	public static final Format numPercentFormat = new Format() {

		@Override
		public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
			StringBuffer result = new StringBuffer();
			Number num = (Number) obj;
			if(num.doubleValue() >= 0 && num.doubleValue() <= 100){
				result.append(numFormat.format(num));
			}
			return result;
		}

		@Override
		public Object parseObject(String source, ParsePosition pos) {
			return null;
		}
	};
	
	public static final PointLabelFormatter plf = new PointLabelFormatter(Color.BLACK);

	static{
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		hourFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
}
