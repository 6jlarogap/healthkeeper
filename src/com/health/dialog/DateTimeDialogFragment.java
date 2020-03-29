package com.health.dialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.health.main.R;

public class DateTimeDialogFragment extends DialogFragment {
	
	public static final String DATE_VALUE = "date_value";
	
	private GregorianCalendar mInitValue = null;

    public void setInitValue(GregorianCalendar mInitValue) {
		this.mInitValue = mInitValue;
	}

	public static DateTimeDialogFragment newInstance(GregorianCalendar initValue) {
        DateTimeDialogFragment f = new DateTimeDialogFragment();
        f.setInitValue(initValue);
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
        View v = inflater.inflate(R.layout.datetime_fragment, container, false);
        getDialog().setTitle(R.string.datetime_dialogtitle);
        Button btnSave = (Button) v.findViewById(R.id.btnSave);
        Button bntCancel = (Button) v.findViewById(R.id.btnCancel);
        final DatePicker dpDate = (DatePicker) v.findViewById(R.id.dpDate);
        final TimePicker tpTime = (TimePicker) v.findViewById(R.id.tpTime);
        tpTime.setIs24HourView(true);
        if(this.mInitValue != null){
        	dpDate.init(mInitValue.get(Calendar.YEAR), mInitValue.get(Calendar.MONTH), mInitValue.get(Calendar.DAY_OF_MONTH), null);
        	tpTime.setCurrentHour(mInitValue.get(Calendar.HOUR_OF_DAY));
        	tpTime.setCurrentMinute(mInitValue.get(Calendar.MINUTE));
        }        
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GregorianCalendar calendar = new GregorianCalendar(dpDate.getYear(), dpDate.getMonth(), dpDate.getDayOfMonth(), tpTime.getCurrentHour(), tpTime.getCurrentMinute());                
                getActivity().getIntent().putExtra(DATE_VALUE, calendar);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });
        bntCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }
}
