package com.health.dialog;

import com.health.main.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HistogrammSettingsDialogFragment extends DialogFragment {

	interface OnHistogrammSettingsSelectedListener{
		public void onHistogrammSettingsSelected(Double step, Double minVal, Double maxVal);
	}
	
	OnHistogrammSettingsSelectedListener onHistogrammSettingsSelectedListener;
	
	public static final String STEP = "step";
	public static final String MINVAL = "minVal";
	public static final String MAXVAL = "maxVal";

	Double step;
	Double minVal = -Double.MAX_VALUE;
	Double maxVal = Double.MAX_VALUE;
	
	EditText etStep;
	EditText etMinVal;
	EditText etMaxVal;

    public static HistogrammSettingsDialogFragment newInstance(double s, Double minv, Double maxv) {
    	HistogrammSettingsDialogFragment f = new HistogrammSettingsDialogFragment();
        f.init(s, minv, maxv);
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
        View v = inflater.inflate(R.layout.dialog_histogramm_step, container, false);
        getDialog().setTitle("Настройки");
        Button btnSave = (Button) v.findViewById(R.id.btnSaveStep);     
        
        this.etStep = (EditText) v.findViewById(R.id.etStep);
        this.etStep.setText(String.valueOf(step));
        this.etStep.setSelection(0, etStep.getText().length());  
        
        this.etMinVal = (EditText) v.findViewById(R.id.etMinVal);
        if(minVal != null && minVal != -Double.MAX_VALUE){
        	this.etMinVal.setText(String.valueOf(minVal));
        }
        this.etMinVal.setSelection(0, etMinVal.getText().length());
        
        this.etMaxVal = (EditText) v.findViewById(R.id.etMaxVal);
        if(maxVal != null && maxVal != Double.MAX_VALUE){
        	this.etMaxVal.setText(String.valueOf(maxVal));
        }
        this.etMaxVal.setSelection(0, etMaxVal.getText().length());
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(etStep.getText().length() > 0){
            		getActivity().getIntent().putExtra(STEP, Double.valueOf(etStep.getText().toString()));
            	}
            	if(etMinVal.getText().length() > 0){
            		getActivity().getIntent().putExtra(MINVAL, Double.valueOf(etMinVal.getText().toString()));
                } else {
                	getActivity().getIntent().putExtra(MINVAL, -Double.MAX_VALUE);
                }
            	if(etMaxVal.getText().length() > 0){
            		getActivity().getIntent().putExtra(MAXVAL, Double.valueOf(etMaxVal.getText().toString()));
                } else {
                	getActivity().getIntent().putExtra(MAXVAL, Double.MAX_VALUE);
                }
                if(getTargetFragment() != null){
                	getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                }
                if(onHistogrammSettingsSelectedListener != null){
                	Double s = step, minV, maxV;
                	if(etStep.getText().length() > 0){
                		s = Double.valueOf(etStep.getText().toString());
                	}
                	if(etMinVal.getText().length() > 0){
                		minV = Double.valueOf(etMinVal.getText().toString());
                	} else {
                		minV = -Double.MAX_VALUE;
                	}
                	if(etMaxVal.getText().length() > 0){
                		maxV = Double.valueOf(etMaxVal.getText().toString());
                	} else {
                		maxV = Double.MAX_VALUE;
                	}
                	onHistogrammSettingsSelectedListener.onHistogrammSettingsSelected(s, minV, maxV);
                }
                dismiss();
            }
        });       
        return v;
    }

    public void init(double s, Double minv, Double maxv){
    	this.step = s;
    	this.minVal = minv;
    	this.maxVal = maxv;
    }
    
    public void setOnHistogrammSettingsSelectedListener(OnHistogrammSettingsSelectedListener listener){
    	onHistogrammSettingsSelectedListener = listener;
    }
}
