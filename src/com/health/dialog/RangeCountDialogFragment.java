package com.health.dialog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.health.main.R;
import com.health.plot.Electron08MeVPlot;
import com.health.plot.Electron2MeVPlot;
import com.health.plot.IDataPlot;
import com.health.plot.KpPlot;
import com.health.plot.Proton100MeVPlot;
import com.health.plot.Proton10MeVPlot;
import com.health.plot.Proton1MeVPlot;
import com.health.plot.RangeCountPlot;
import com.health.repository.IRepository;
import com.health.settings.Settings;
import com.health.task.GetRangeCountTask;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class RangeCountDialogFragment extends DialogFragment {
	public static final int PERIOD_DATE_TIME_DIALOG_FRAGMENT = 10002;
	public static final int PERIOD_DATE_STEP_DIALOG_FRAGMENT = 10003;
	IRepository mRepository;
	Date dtFrom;
	Date dtTo;
	IDataPlot p;
	Context mContext;
	RangeCountPlot RCplot;
    public DateFormat mDateFormat = DateFormat.getDateInstance();
    Button btnChartPeriod, btnStep;
    Format domainFormat;
    Double minVal = -Double.MAX_VALUE;
    Double maxVal = Double.MAX_VALUE;
    
    //Pair<Double, Integer> rc = new Pair<Double, Integer>(null, null);
    List<Pair<Double, Integer>> values;
    
	class PairComparator implements Comparator<Pair<Double, Integer>> {
		@Override
        public int compare(Pair<Double, Integer> lhs, Pair<Double, Integer> rhs) {
			return ((Double)lhs.first).compareTo((Double)rhs.first);
        }
	}
	
	public void setInitValue(Date dtFrom, Date dtTo, IRepository mRepository, final IDataPlot p, Context context) {
		this.dtFrom = dtFrom;
		this.dtTo = dtTo;
		this.mRepository = mRepository;
		this.p = p;
		this.mContext = context;
		if(p instanceof Proton1MeVPlot ||
			p instanceof Proton10MeVPlot ||
			p instanceof Proton100MeVPlot ||
			p instanceof Electron08MeVPlot ||
			p instanceof Electron2MeVPlot){
			domainFormat = new Format() {

				DecimalFormat df = new DecimalFormat("0.0E0");
				
				@Override
				public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
					StringBuffer result = new StringBuffer();
					Number num = (Number) obj;
					result.append(df.format(num.longValue()));
					return result;
				}

				@Override
				public Object parseObject(String source, ParsePosition pos) {
					return null;

				}
			};
		} else if(p instanceof KpPlot){
			domainFormat = new Format() {

				@Override
				public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
					StringBuffer result = new StringBuffer();
					Number num = (Number) obj;
					result.append(((KpPlot)p).getKpById(num.intValue()));
					return result;
				}

				@Override
				public Object parseObject(String source, ParsePosition pos) {
					return null;

				}
			};
		} else {
			domainFormat = new Format() {

				DecimalFormat df = new DecimalFormat("#.###");
				
				@Override
				public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
					StringBuffer result = new StringBuffer();
					Number num = (Number) obj;
					result.append(df.format(num.longValue()));
					return result;
				}

				@Override
				public Object parseObject(String source, ParsePosition pos) {
					return null;

				}
			};
		}
	}
	
	public static RangeCountDialogFragment newInstance(Date dtFrom, Date dtTo, IRepository mRepository, IDataPlot p, Context context) {
		RangeCountDialogFragment f = new RangeCountDialogFragment();
		f.setInitValue(dtFrom, dtTo, mRepository, p, context);
        return f;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_range_count, container, false);
        getDialog().setTitle("Диапазон и Количество");
        btnChartPeriod = (Button) v.findViewById(R.id.btnChartPeriod);
		btnChartPeriod.setText(mDateFormat.format(dtFrom) + " - " + mDateFormat.format(dtTo));
        btnChartPeriod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GregorianCalendar mDateFrom = new GregorianCalendar();
				mDateFrom.setTime(dtFrom);
				GregorianCalendar mDateTo = new GregorianCalendar();
				mDateTo.setTime(dtTo);
				PeriodDateDialogFragment newFragment = PeriodDateDialogFragment.newInstance(mDateFrom, mDateTo);                
                newFragment.setTargetFragment(RangeCountDialogFragment.this, PERIOD_DATE_TIME_DIALOG_FRAGMENT);
                newFragment.show(getFragmentManager(), "perioddialog");
			}
		}); 

        btnStep = (Button) v.findViewById(R.id.btnStep);
        btnStep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HistogrammSettingsDialogFragment newFragment = HistogrammSettingsDialogFragment.newInstance(p.getHistogrammFloorStep(), minVal, maxVal);                
                newFragment.setTargetFragment(RangeCountDialogFragment.this, PERIOD_DATE_STEP_DIALOG_FRAGMENT);
                newFragment.show(getFragmentManager(), "stepdialog");
			}
		}); 
        Button btnOK = (Button) v.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        }); 
		LinearLayout llPlot = (LinearLayout) v.findViewById(R.id.llPlot);
		updateData();
		updatePlot();
		llPlot.addView(RCplot.getPlot());
        return v;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = 0;
        setStyle(style, theme);
    }
	
	private void updateData(){
		
		GetRangeCountTask rct = new GetRangeCountTask(null, null, mContext);
		rct.setDateRange(dtFrom, dtTo);
		String urlStr = "id=" + String.valueOf(p.getDataTypeId()) + "&cityid=625144&hfs=" + p.getHistogrammFloorStep();
		if(minVal != null){
			urlStr += "&minVal=" + minVal;
		}
		if(maxVal != null){
			urlStr += "&maxVal=" + maxVal;
		}
		rct.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, Settings.getStatUrl(this.mContext, urlStr));
		try {
			values = rct.get();
        } catch (InterruptedException e) {
	        e.printStackTrace();
        } catch (ExecutionException e) {
	        e.printStackTrace();
        }
	}
	
	private void updatePlot(){
		if(RCplot == null){
			RCplot = new RangeCountPlot(this.mContext, p.getUnit(), values, p.getHistogrammFloorStep(), domainFormat);
		} else {
			RCplot.setHistogrammFloorStep(p.getHistogrammFloorStep());
			RCplot.updateData(values);
		}
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == Activity.RESULT_OK){
    		if(requestCode == PERIOD_DATE_TIME_DIALOG_FRAGMENT ) {
                GregorianCalendar calendarFrom = (GregorianCalendar) data.getExtras().get(PeriodDateDialogFragment.DATE_VALUE_FROM);
                GregorianCalendar calendarTo = (GregorianCalendar) data.getExtras().get(PeriodDateDialogFragment.DATE_VALUE_TO);

                dtFrom = calendarFrom.getTime();
                dtTo = calendarTo.getTime();

        		btnChartPeriod.setText(mDateFormat.format(dtFrom) + " - " + mDateFormat.format(dtTo));
                
                updateData();
                updatePlot();
            } else if(requestCode == PERIOD_DATE_STEP_DIALOG_FRAGMENT ) {
            	if(data.getExtras().get(HistogrammSettingsDialogFragment.STEP) != null){
                    p.setHistogrammFloorStep((double) data.getExtras().get(HistogrammSettingsDialogFragment.STEP));
            	}
            	if(data.getExtras().get(HistogrammSettingsDialogFragment.MINVAL) != null){
            		minVal = (Double) data.getExtras().get(HistogrammSettingsDialogFragment.MINVAL);
            	} else {
            		minVal = -Double.MAX_VALUE;
            	}
            	if(data.getExtras().get(HistogrammSettingsDialogFragment.MAXVAL) != null){
            		maxVal = (Double) data.getExtras().get(HistogrammSettingsDialogFragment.MAXVAL);
            	} else {
            		maxVal = Double.MAX_VALUE;
            	}
            	
                updateData();
                updatePlot();
            }
    	}
        
    }
	
}
