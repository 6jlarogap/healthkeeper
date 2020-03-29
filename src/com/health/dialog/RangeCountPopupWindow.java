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
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class RangeCountPopupWindow extends PopupWindow implements PeriodDateDialogFragment.OnPeriodDateSelectedListener, HistogrammSettingsDialogFragment.OnHistogrammSettingsSelectedListener {
	public static final int PERIOD_DATE_TIME_DIALOG_FRAGMENT = 10002;
	public static final int PERIOD_DATE_STEP_DIALOG_FRAGMENT = 10003;
	IRepository mRepository;
	Date dateFrom;
	Date dateTo;
	IDataPlot plot;
	RangeCountPlot RCplot;
	Context mContext;
    DateFormat mDateFormat = DateFormat.getDateInstance();
    Button btnChartPeriod, btnStep;
    Double minVal = -Double.MAX_VALUE;
    Double maxVal = Double.MAX_VALUE;
    int xOff = 0, yOff = 0;
    float mX = 0, mY = 0;
    float mW = 0, mH = 0;
	int[] l1 = new int[4];
	int[] l2 = new int[4];
    
    Format domainFormat;
    
    List<Pair<Double, Integer>> values;
    
	class PairComparator implements Comparator<Pair<Double, Integer>> {
		@Override
        public int compare(Pair<Double, Integer> lhs, Pair<Double, Integer> rhs) {
			return ((Double)lhs.first).compareTo((Double)rhs.first);
        }
	}
	
	public RangeCountPopupWindow(Date dFrom, Date dTo, IRepository repository, IDataPlot p, Context context){
		super(((Activity) context).getLayoutInflater().inflate(R.layout.popup_range_count, null), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		dateFrom = dFrom;
		dateTo = dTo;
		mRepository = repository;
		plot = p;
		mContext = context;
		
		if(plot instanceof Proton1MeVPlot ||
				plot instanceof Proton10MeVPlot ||
				plot instanceof Proton100MeVPlot ||
				plot instanceof Electron08MeVPlot ||
				plot instanceof Electron2MeVPlot){
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
			} else if(plot instanceof KpPlot){
				domainFormat = new Format() {

					@Override
					public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
						StringBuffer result = new StringBuffer();
						Number num = (Number) obj;
						result.append(((KpPlot)plot).getKpById(num.intValue()));
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

		Button btnDismiss = (Button)this.getContentView().findViewById(R.id.btnClose);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		dismiss();
    		}
    	});
        
		ImageButton btnMove = (ImageButton)this.getContentView().findViewById(R.id.btnMove);
		btnMove.setOnTouchListener(new OnTouchListener(){

			@Override
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getLocationOnScreen(l1);
                    v.getLocationInWindow(l2);
                    mX = X - l1[0] + l2[0];
                    mY = Y - l1[1] + l2[1];		
                    return true;
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					xOff = X - (int)mX;
					yOff = Y - (int)mY;
					update(xOff, yOff, getWidth(), getHeight());
					return true;
				} else {
					return true;
				}
            }});
		
		ImageButton btnResize = (ImageButton)this.getContentView().findViewById(R.id.btnResize);
		btnResize.setOnTouchListener(new OnTouchListener(){

			@Override
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getLocationOnScreen(l1);
                    v.getLocationInWindow(l2);
                    mW = X - getWidth();
                    mH = Y - getHeight();
                    return true;
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					update(xOff, yOff, X - (int)mW, Y - (int)mH);
					return true;
				} else {
					return true;
				}
            }});

        btnChartPeriod = (Button) this.getContentView().findViewById(R.id.btnChartPeriod);
		btnChartPeriod.setText(mDateFormat.format(dateFrom) + " - " + mDateFormat.format(dateTo));
        btnChartPeriod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GregorianCalendar mDateFrom = new GregorianCalendar();
				mDateFrom.setTime(dateFrom);
				GregorianCalendar mDateTo = new GregorianCalendar();
				mDateTo.setTime(dateTo);
				
				PeriodDateDialogFragment newFragment = PeriodDateDialogFragment.newInstance(mDateFrom, mDateTo);                
                newFragment.setTargetFragment(null, PERIOD_DATE_TIME_DIALOG_FRAGMENT);
                newFragment.setOnPeriodDateSelectedListener(RangeCountPopupWindow.this);
                newFragment.show(((Activity) mContext).getFragmentManager(), "perioddialog");
                
			}
		});
        
        btnStep = (Button) this.getContentView().findViewById(R.id.btnStep);
        btnStep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HistogrammSettingsDialogFragment newFragment = HistogrammSettingsDialogFragment.newInstance(plot.getHistogrammFloorStep(), minVal, maxVal);                
                newFragment.setTargetFragment(null, PERIOD_DATE_STEP_DIALOG_FRAGMENT);
                newFragment.setOnHistogrammSettingsSelectedListener(RangeCountPopupWindow.this);
                newFragment.show(((Activity) mContext).getFragmentManager(), "stepdialog");
			}
		}); 
        
		LinearLayout llPlot = (LinearLayout) this.getContentView().findViewById(R.id.llPlot);
		updateData();
		updatePlot();
		llPlot.addView(RCplot.getPlot());
	}
	
	private void updateData(){
		
		GetRangeCountTask rct = new GetRangeCountTask(null, null, mContext);
		rct.setDateRange(dateFrom, dateTo);
		String urlStr = "id=" + String.valueOf(plot.getDataTypeId()) + "&cityid=625144&hfs=" + plot.getHistogrammFloorStep();
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
			RCplot = new RangeCountPlot(this.mContext, plot.getUnit(), values, plot.getHistogrammFloorStep(), domainFormat);
		} else {
			RCplot.setHistogrammFloorStep(plot.getHistogrammFloorStep());
			RCplot.updateData(values);
		}
	}

	@Override
    public void onPeriodDateSelected(GregorianCalendar dtFrom, GregorianCalendar dtTo) {
		dateFrom = dtFrom.getTime();
		dateTo = dtTo.getTime();
		btnChartPeriod.setText(mDateFormat.format(dateFrom) + " - " + mDateFormat.format(dateTo));

        updateData();
        updatePlot();
	}

	@Override
    public void onHistogrammSettingsSelected(Double step, Double minVal, Double maxVal) {
	    plot.setHistogrammFloorStep(step);
	    this.minVal = minVal;
	    this.maxVal = maxVal;
	    
        updateData();
        updatePlot();
    }
	
	@Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
		xOff = x;
		yOff = y;
        super.showAtLocation(parent, gravity, x, y);
    }
	

}
