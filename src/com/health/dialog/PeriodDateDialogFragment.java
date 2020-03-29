package com.health.dialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.health.main.R;

public class PeriodDateDialogFragment extends DialogFragment {
	
	interface OnPeriodDateSelectedListener{
		public void onPeriodDateSelected(GregorianCalendar dtFrom, GregorianCalendar dtTo);
	}
	
	OnPeriodDateSelectedListener onPeriodDateSelectedListener;
	
	public static final String DATE_VALUE_FROM = "date_value_from";
	
	public static final String DATE_VALUE_TO = "date_value_to";	
	
	private DatePicker dpDateFrom, dpDateTo;
	
	private GregorianCalendar mInitCalendarFrom, mInitCalendarTo;

    public static PeriodDateDialogFragment newInstance(GregorianCalendar initCalendarFrom, GregorianCalendar initCalendarTo) {
        PeriodDateDialogFragment f = new PeriodDateDialogFragment();
        f.init(initCalendarFrom, initCalendarTo);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = 0;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_period, container, false);
        getDialog().setTitle(R.string.period_dialog_title);
        Button btnSave = (Button) v.findViewById(R.id.btnSavePeriod);        
        this.dpDateFrom = (DatePicker) v.findViewById(R.id.dPeriodFrom);
        this.dpDateTo = (DatePicker) v.findViewById(R.id.dPeriodTo);        
        if(this.mInitCalendarFrom != null){
        	this.dpDateFrom.init(this.mInitCalendarFrom.get(Calendar.YEAR), this.mInitCalendarFrom.get(Calendar.MONTH), this.mInitCalendarFrom.get(Calendar.DAY_OF_MONTH), null);
        }
        if(this.mInitCalendarTo != null){
        	this.dpDateTo.init(this.mInitCalendarTo.get(Calendar.YEAR), this.mInitCalendarTo.get(Calendar.MONTH), this.mInitCalendarTo.get(Calendar.DAY_OF_MONTH), null);
        }        
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GregorianCalendar calendarFrom = new GregorianCalendar(dpDateFrom.getYear(), dpDateFrom.getMonth(), dpDateFrom.getDayOfMonth());
                GregorianCalendar calendarTo = new GregorianCalendar(dpDateTo.getYear(), dpDateTo.getMonth(), dpDateTo.getDayOfMonth());
                calendarTo.set(GregorianCalendar.HOUR_OF_DAY, 23);
                calendarTo.set(GregorianCalendar.MINUTE, 59);
                calendarTo.set(GregorianCalendar.SECOND, 59);
                if (calendarTo.getTimeInMillis() < calendarFrom.getTimeInMillis()) {
					CharSequence text = getString(R.string.error_period_date);
					Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					return;
				}
                getActivity().getIntent().putExtra(DATE_VALUE_FROM, calendarFrom);
                getActivity().getIntent().putExtra(DATE_VALUE_TO, calendarTo);
                if(getTargetFragment() != null){
                	getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                }
                if(onPeriodDateSelectedListener != null){
                	onPeriodDateSelectedListener.onPeriodDateSelected(calendarFrom, calendarTo);
                }
                dismiss();
            }
        });       
        return v;
    }
    
    public void init(GregorianCalendar fromGregorianCalendar, GregorianCalendar toGregorianCalendar){
    	this.mInitCalendarFrom = fromGregorianCalendar;
    	this.mInitCalendarTo = toGregorianCalendar;
    }
    
    public void setOnPeriodDateSelectedListener(OnPeriodDateSelectedListener listener){
    	onPeriodDateSelectedListener = listener;
    }
}
