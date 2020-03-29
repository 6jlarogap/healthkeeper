package com.health.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.health.data.CommonFeelingGroup;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomFactorType;
import com.health.data.FactorGroup;
import com.health.data.FactorType;
import com.health.data.UnitDimension;
import com.health.data.User;
import com.health.dialog.PlotHelpDialogFragment;
import com.health.repository.IRepository;

public class CustomTypeFragment extends Fragment {

	private TextView tvTitle;
	private IRepository mRepository;
	private long mGroupId;
	private int mTypeId;
	private int mStatus;
	private User mCurrentUser = null;

	private CheckBox[] cbArray = new CheckBox[5];
	private LinearLayout[] llArray = new LinearLayout[5];
	private EditText[] etArray = new EditText[5];
	private Button[] btnQuestionArray = new Button[5];
	
	private EditText etName2_Number1;
	private EditText etName3_Number1, etName3_Number2;

	private LinearLayout[] llColorArray = new LinearLayout[3];

	private Button btnSave;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mRepository = ((HealthApplication) activity.getApplication()).getRepository();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
	}

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mGroupId = getActivity().getIntent().getExtras().getLong(CustomTypeActivity.EXTRA_PARENT_ID);
		this.mTypeId = getActivity().getIntent().getExtras().getInt(CustomTypeActivity.EXTRA_TYPE_ID);
		this.mCurrentUser = mRepository.getCurrentUser();
		View view = inflater.inflate(R.layout.custom_type_fragment, null);
		this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		this.btnSave = (Button) view.findViewById(R.id.btnSave);
		this.cbArray[0] = (CheckBox) view.findViewById(R.id.cb1);
		this.cbArray[1] = (CheckBox) view.findViewById(R.id.cb2);
		this.cbArray[2] = (CheckBox) view.findViewById(R.id.cb3);
		this.cbArray[3] = (CheckBox) view.findViewById(R.id.cb4);
		this.cbArray[4] = (CheckBox) view.findViewById(R.id.cb5);
		this.llArray[0] = (LinearLayout) view.findViewById(R.id.ll1);
		this.llArray[1] = (LinearLayout) view.findViewById(R.id.ll2);
		this.llArray[2] = (LinearLayout) view.findViewById(R.id.ll3);
		this.llArray[3] = (LinearLayout) view.findViewById(R.id.ll4);
		this.llArray[4] = (LinearLayout) view.findViewById(R.id.ll5);
		this.etArray[0] = (EditText) view.findViewById(R.id.etName1);
		this.etArray[1] = (EditText) view.findViewById(R.id.etName2);
		this.etArray[2] = (EditText) view.findViewById(R.id.etName3);
		this.etArray[3] = (EditText) view.findViewById(R.id.etName4);
		this.etArray[4] = (EditText) view.findViewById(R.id.etName5);
		
		this.btnQuestionArray[0] = (Button) view.findViewById(R.id.btnQuestion1);
		this.btnQuestionArray[1] = (Button) view.findViewById(R.id.btnQuestion2);
		this.btnQuestionArray[2] = (Button) view.findViewById(R.id.btnQuestion3);
		this.btnQuestionArray[3] = (Button) view.findViewById(R.id.btnQuestion4);
		this.btnQuestionArray[4] = (Button) view.findViewById(R.id.btnQuestion5);
		
		this.etName2_Number1 = (EditText) view.findViewById(R.id.etName2_Number1);
		this.etName3_Number1 = (EditText) view.findViewById(R.id.etName3_Number1);
		this.etName3_Number2 = (EditText) view.findViewById(R.id.etName3_Number2);

		this.llColorArray[0] = (LinearLayout) view.findViewById(R.id.ll6_1);
		this.llColorArray[1] = (LinearLayout) view.findViewById(R.id.ll6_2);
		this.llColorArray[2] = (LinearLayout) view.findViewById(R.id.ll6_3);
		initCheckBoxes();
		initLLColor();
		initButtonQuestions();
		this.btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mCurrentUser.isAnonim()){
					((CustomTypeActivity) getActivity()).finishForAnonimUser();
					return;
				}
				if (cbArray[0].isChecked()) {
					if (checkFormat1()) {
						saveFormat1();
					}
				}

				if (cbArray[1].isChecked()) {
					if (checkFormat2()) {
						saveFormat2();
					}
				}
				
				if (cbArray[2].isChecked()) {
					if (checkFormat3()) {
						saveFormat3();
					}
				}
				
				if (cbArray[3].isChecked()) {
					Toast.makeText(getActivity(), "Данный формат находиться в этапе разработки", Toast.LENGTH_LONG).show();
				}
				if (cbArray[4].isChecked()) {
					Toast.makeText(getActivity(), "Данный формат находиться в этапе разработки", Toast.LENGTH_LONG).show();
				}

			}
		});

		return view;
	}
	
	private void initCheckBoxes() {
		final int drawableId = R.color.transparent_yellow;
		
		for(int i = 0; i < llArray.length; i++){
			if(i == 0){
				cbArray[i].setChecked(true);
				llArray[i].setBackgroundResource(drawableId);
				setEnabled(llArray[i], true);
			} else {
				llArray[i].setBackgroundDrawable(null);
				setEnabled(llArray[i], false);
			}
		}
		for (CheckBox cb : cbArray) {
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						for (int i = 0; i < cbArray.length; i++) {
							if (cbArray[i] != buttonView) {
								cbArray[i].setChecked(false);
								llArray[i].setBackgroundDrawable(null);
								setEnabled(llArray[i], false);
							} else {
								llArray[i].setBackgroundResource(drawableId);
								setEnabled(llArray[i], true);
							}
						}
					}

				}
			});
		}
	}

	private void initLLColor() {
		final int drawableId = R.color.transparent_yellow;		
		llColorArray[0].setBackgroundResource(drawableId);
		mStatus = FactorType.POSITIVE_STATUS;
		for (int i = 0; i < llColorArray.length; i++) {
			llColorArray[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int j = 0; j < llColorArray.length; j++) {
						if (v != llColorArray[j]) {
							llColorArray[j].setBackgroundDrawable(null);							
						} else {
							llColorArray[j].setBackgroundResource(drawableId);							
							switch (j) {
							case 0:
								mStatus = FactorType.POSITIVE_STATUS;
								break;
							case 1:
								mStatus = FactorType.NEUTRAL_STATUS;
								break;
							case 2:
								mStatus = FactorType.NEGATIVE_STATUS;
								break;
							default:
								break;
							}
						}
					}
				}
			});
		}
	}
	
	private void setEnabled(LinearLayout ll, boolean enabled){
		for(int i = 0; i < ll.getChildCount(); i++){
			View v = ll.getChildAt(i);
			if(!(v instanceof CheckBox)){
				v.setEnabled(enabled);
				if(v instanceof EditText){
					((EditText) v).setText(null);
				}
			}
		}
	}

	private void initButtonQuestions(){
		for(int i = 0; i < btnQuestionArray.length; i++){
			btnQuestionArray[i].setTag(i);
			btnQuestionArray[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int layoutId = R.layout.dialog_help_unit_sample1;
					int pos = (Integer) v.getTag();
					switch (pos) {
					case 0:
						layoutId = R.layout.dialog_help_unit_sample1;
						break;
					case 1:
						layoutId = R.layout.dialog_help_unit_sample2;
						break;
					case 2:
						layoutId = R.layout.dialog_help_unit_sample3;
						break;
					case 3:
						layoutId = R.layout.dialog_help_unit_sample4;
						break;
					case 4:
						layoutId = R.layout.dialog_help_unit_sample5;
						break;

					default:
						break;
					}
					PlotHelpDialogFragment newFragment = PlotHelpDialogFragment.newInstance(layoutId);
			        newFragment.show(getFragmentManager(), "help");
					
				}
			});
		}
	}
	
	private boolean checkFormat1() {
		boolean result = true;
		if (etArray[0].getText().toString().length() < 2) {
			etArray[0].setError(getActivity().getString(R.string.error_field_required));
			result = false;
		}
		return result;
	}
	
	private void saveFormat1(){
		String name = etArray[0].getText().toString();
		switch (mTypeId) {
		case CustomTypeActivity.CUSTOMFACTOR_TYPE_ID:
			FactorGroup factorGroup = mRepository.getFactorGroupById(getActivity(), mGroupId);
			CustomFactorType customFactorType = new CustomFactorType();
			customFactorType.setFactorGroup(factorGroup);
			customFactorType.setName(name);
			customFactorType.setOrdinalNumber(mRepository.getNextOrdinalNumberForFactorGroup(getActivity(), mGroupId));
			customFactorType.setStatus(mStatus);
			customFactorType.setUnitDimensionId(UnitDimension.BOOLEAN_TYPE);
			mRepository.addCustomFactorType(customFactorType);
			((CustomTypeActivity) getActivity()).finish(customFactorType, null, null, null);
			break;
		case CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID:
			CommonFeelingGroup commonFeelingGroup = mRepository.getCommonFeelingGroupById(getActivity(), mGroupId);
			CustomCommonFeelingType customCommonFeelingType = new CustomCommonFeelingType();
			customCommonFeelingType.setCommonFeelingGroup(commonFeelingGroup);
			customCommonFeelingType.setName(name);
			customCommonFeelingType.setOrdinalNumber(mRepository.getNextOrdinalNumberForCommonFeelingGroup(getActivity(), mGroupId));
			customCommonFeelingType.setStatus(mStatus);
			customCommonFeelingType.setUnitDimensionId(UnitDimension.BOOLEAN_TYPE);
			mRepository.addCustomCommonFeelingType(customCommonFeelingType);
			((CustomTypeActivity) getActivity()).finish(customCommonFeelingType, null, null, null);
			break;
		}
	}
	
	private boolean checkFormat2() {
		boolean result = true;
		if (etArray[1].getText().toString().length() < 2) {
			etArray[1].setError(getActivity().getString(R.string.error_field_required));
			result = false;
		}
		if(etName2_Number1.getText().toString().length() == 0){
			etName2_Number1.setError(getActivity().getString(R.string.error_field_required));
			result = false;
		}
		return result;
	}
	
	private void saveFormat2(){
		double value1 = Double.parseDouble(etName2_Number1.getText().toString());
		String name = etArray[1].getText().toString();
		switch (mTypeId) {
		case CustomTypeActivity.CUSTOMFACTOR_TYPE_ID:
			FactorGroup factorGroup = mRepository.getFactorGroupById(getActivity(), mGroupId);
			CustomFactorType customFactorType = new CustomFactorType();
			customFactorType.setFactorGroup(factorGroup);
			customFactorType.setName(name);			
			customFactorType.setOrdinalNumber(mRepository.getNextOrdinalNumberForFactorGroup(getActivity(), mGroupId));
			customFactorType.setStatus(mStatus);
			customFactorType.setUnitDimensionId(UnitDimension.NUMBER_TYPE);
			mRepository.addCustomFactorType(customFactorType);
			((CustomTypeActivity) getActivity()).finish(customFactorType, value1, null, null);
			break;
		case CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID:
			CommonFeelingGroup commonFeelingGroup = mRepository.getCommonFeelingGroupById(getActivity(), mGroupId);
			CustomCommonFeelingType customCommonFeelingType = new CustomCommonFeelingType();
			customCommonFeelingType.setCommonFeelingGroup(commonFeelingGroup);
			customCommonFeelingType.setName(name);			
			customCommonFeelingType.setOrdinalNumber(mRepository.getNextOrdinalNumberForCommonFeelingGroup(getActivity(), mGroupId));
			customCommonFeelingType.setStatus(mStatus);
			customCommonFeelingType.setUnitDimensionId(UnitDimension.NUMBER_TYPE);
			mRepository.addCustomCommonFeelingType(customCommonFeelingType);
			((CustomTypeActivity) getActivity()).finish(customCommonFeelingType, value1, null, null);
			break;
		}
	}
	
	private boolean checkFormat3() {
		boolean result = true;
		if (this.etArray[2].getText().toString().length() < 2) {
			etArray[2].setError(getActivity().getString(R.string.error_field_required));
			result = false;
		}
		if(etName3_Number1.getText().toString().length() == 0){
			etName3_Number1.setError(getActivity().getString(R.string.error_field_required));
			result = false;
		}
		if(etName3_Number2.getText().toString().length() == 0){
			etName3_Number2.setError(getActivity().getString(R.string.error_field_required));
			result = false;
		}
		return result;
	}
	
	private void saveFormat3(){
		double value1 = Double.parseDouble(etName3_Number1.getText().toString());
		double value2 = Double.parseDouble(etName3_Number2.getText().toString());
		String name = etArray[2].getText().toString();
		switch (mTypeId) {
		case CustomTypeActivity.CUSTOMFACTOR_TYPE_ID:
			FactorGroup factorGroup = mRepository.getFactorGroupById(getActivity(), mGroupId);
			CustomFactorType customFactorType = new CustomFactorType();
			customFactorType.setFactorGroup(factorGroup);
			customFactorType.setName(name);			
			customFactorType.setOrdinalNumber(mRepository.getNextOrdinalNumberForFactorGroup(getActivity(), mGroupId));
			customFactorType.setStatus(mStatus);
			customFactorType.setUnitDimensionId(UnitDimension.NUMBER_NUMBER_TYPE);
			mRepository.addCustomFactorType(customFactorType);
			((CustomTypeActivity) getActivity()).finish(customFactorType, value1, value2, null);
			break;
		case CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID:
			CommonFeelingGroup commonFeelingGroup = mRepository.getCommonFeelingGroupById(getActivity(), mGroupId);
			CustomCommonFeelingType customCommonFeelingType = new CustomCommonFeelingType();
			customCommonFeelingType.setCommonFeelingGroup(commonFeelingGroup);
			customCommonFeelingType.setName(name);		
			customCommonFeelingType.setOrdinalNumber(mRepository.getNextOrdinalNumberForCommonFeelingGroup(getActivity(), mGroupId));
			customCommonFeelingType.setStatus(mStatus);
			customCommonFeelingType.setUnitDimensionId(UnitDimension.NUMBER_NUMBER_TYPE);
			mRepository.addCustomCommonFeelingType(customCommonFeelingType);
			((CustomTypeActivity) getActivity()).finish(customCommonFeelingType, value1, value2, null);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}