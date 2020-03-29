package com.health.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.main.R;

public class CorrResultDialogFragment  extends DialogFragment {
	String mCorrResult;
	
	public void setInitValue(String mCorrResult) {
		this.mCorrResult = mCorrResult;
	}
	
	public static CorrResultDialogFragment newInstance(String mCorrResult) {
		CorrResultDialogFragment f = new CorrResultDialogFragment();
		f.setInitValue(mCorrResult);
        return f;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_corr_result, container, false);
        getDialog().setTitle("Корреляция");
        Button btnOK = (Button) v.findViewById(R.id.btnOK);
        TextView tvCorrResult = (TextView) v.findViewById(R.id.tvCorrResult);
        tvCorrResult.setText(mCorrResult);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        }); 
        return v;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = 0;
        setStyle(style, theme);
    }	
}
