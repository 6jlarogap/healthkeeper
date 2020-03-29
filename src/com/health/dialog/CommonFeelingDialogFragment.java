package com.health.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.health.data.CommonFeelingType;
import com.health.main.R;

public class CommonFeelingDialogFragment extends DialogFragment {

	public static final String VALUE = "value";
	public static final String COMMON_FEELING_TYPE_ID = "commonFeelingTypeId";

	private static Integer mInitValue = null;
	private static CommonFeelingType mCommonFeelingType = null;
	private EditText etValue = null;

	public void setInitValue(CommonFeelingType commonFeelingType, Integer mInitValue) {
		this.mCommonFeelingType = commonFeelingType;
		this.mInitValue = mInitValue;
	}

	public static CommonFeelingDialogFragment newInstance(CommonFeelingType commonFeelingType, Integer initValue) {
		CommonFeelingDialogFragment f = new CommonFeelingDialogFragment();
		f.setInitValue(commonFeelingType, initValue);
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
		View v = inflater.inflate(R.layout.dialog_commonfeeling_fragment, container, false);
		Button btnSave = (Button) v.findViewById(R.id.btnSave);
		Button bntCancel = (Button) v.findViewById(R.id.btnCancel);
		this.etValue = (EditText) v.findViewById(R.id.etValue);
		String hintValue = null;
		if (this.mCommonFeelingType != null) {
			getDialog().setTitle(this.mCommonFeelingType.getName());
			if (mCommonFeelingType.getId() == 31) {
				hintValue = "120";
			}
			if (mCommonFeelingType.getId() == 32) {
				hintValue = "80";
			}
			if (mCommonFeelingType.getId() == 33) {
				hintValue = "ударов в секунду";
			}
			this.etValue.setHint(hintValue);
		}
		if (this.mInitValue != null) {
			this.etValue.setText(this.mInitValue);
		}
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String valueText = etValue.getText().toString();
				Integer value = null;
				try {
					value = Integer.parseInt(valueText);
				} catch (NumberFormatException nfe) {
					value = null;
				}
				getActivity().getIntent().putExtra(VALUE, value);
				getActivity().getIntent().putExtra(COMMON_FEELING_TYPE_ID, mCommonFeelingType.getId());
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
