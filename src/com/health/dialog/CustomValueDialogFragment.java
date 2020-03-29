package com.health.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.health.data.IGridItem;
import com.health.data.IGridItemValue;
import com.health.data.UnitDimension;
import com.health.main.R;

public class CustomValueDialogFragment extends DialogFragment {

	public static final String EXTRA_TYPE_ID = "extra_typeid";
	public static final String EXTRA_VALUE1 = "extra_value1";
	public static final String EXTRA_VALUE2 = "extra_value2";
	public static final String EXTRA_VALUE3 = "extra_value3";

	private IGridItem mGridItem;
	private IGridItemValue mGridItemValue;
	private int mTypeId;

	private EditText etValue1, etValue2;

	public void setInitValue(IGridItem gridItem, IGridItemValue gridItemValue, int typeId) {
		this.mGridItem = gridItem;
		this.mGridItemValue = gridItemValue;
		this.mTypeId = typeId;
	}

	public static CustomValueDialogFragment newInstance(IGridItem gridItem, IGridItemValue gridItemValue, int typeId) {
		CustomValueDialogFragment f = new CustomValueDialogFragment();
		f.setInitValue(gridItem, gridItemValue, typeId);
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
		View v = null;
		switch ((int) mGridItem.getUnitId()) {
		case (int) UnitDimension.NUMBER_TYPE:
			v = inflater.inflate(R.layout.enter_custom_value_fragment2, container, false);
			this.etValue1 = (EditText) v.findViewById(R.id.etValue1);
			if(mGridItemValue != null && mGridItemValue.getValue1() != null){				
				this.etValue1.setText(Double.toString(mGridItemValue.getValue1()));
			}
			break;
		case (int) UnitDimension.NUMBER_NUMBER_TYPE:
			v = inflater.inflate(R.layout.enter_custom_value_fragment3, container, false);
			this.etValue1 = (EditText) v.findViewById(R.id.etValue1);
			this.etValue2 = (EditText) v.findViewById(R.id.etValue2);
			if(mGridItemValue != null && mGridItemValue.getValue1() != null){				
				this.etValue1.setText(Double.toString(mGridItemValue.getValue1()));
			}
			if(mGridItemValue != null && mGridItemValue.getValue2() != null){				
				this.etValue2.setText(Double.toString(mGridItemValue.getValue2()));
			}
			break;

		default:
			break;
		}
		Button btnSave = (Button) v.findViewById(R.id.btnSave);
		Button bntCancel = (Button) v.findViewById(R.id.btnCancel);
		getDialog().setTitle(mGridItem.getName());

		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {				
				boolean result = save();
			}
		});
		bntCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getIntent().putExtra(EXTRA_TYPE_ID, mTypeId);
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
	    		dismiss();
			}
		});
		return v;
	}

	private boolean save() {
		Double value1 = null;
		Double value2 = null;
		Double value3 = null;
		boolean result = true;
		String text1 = this.etValue1 != null ? this.etValue1.getText().toString() : null;
		String text2 = this.etValue2 != null ? this.etValue2.getText().toString() : null;
		switch ((int) mGridItem.getUnitId()) {
		case (int) UnitDimension.NUMBER_TYPE:
			if (TextUtils.isEmpty(text1)) {
				result = false;
				etValue1.setError(getString(R.string.error_field_required));
			}
			if (result) {
				value1 = Double.valueOf(Double.parseDouble(text1));
			}
			break;
		case (int) UnitDimension.NUMBER_NUMBER_TYPE:
			if (TextUtils.isEmpty(text1)) {
				result = false;
				etValue1.setError(getString(R.string.error_field_required));
			}
			if (TextUtils.isEmpty(text2)) {
				result = false;
				etValue2.setError(getString(R.string.error_field_required));
			}
			if (result) {
				value1 = Double.valueOf(Double.parseDouble(text1));
				value2 = Double.valueOf(Double.parseDouble(text2));
			}
			break;

		default:
			break;
		}		
		if(result){
    		getActivity().getIntent().putExtra(EXTRA_VALUE1, value1);
    		getActivity().getIntent().putExtra(EXTRA_VALUE2, value2);
    		getActivity().getIntent().putExtra(EXTRA_VALUE3, value3);
    		getActivity().getIntent().putExtra(EXTRA_TYPE_ID, mTypeId);
    		getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
    		dismiss();
		}
		
		return result;
	}

}
