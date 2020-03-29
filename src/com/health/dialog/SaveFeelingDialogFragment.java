package com.health.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.health.data.BodyFeeling;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingType;
import com.health.data.IFeelingTypeInfo;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.repository.IRepository;
import com.health.repository.Repository;
import com.health.viewmodel.BodyFeelingTypeInfo;
import com.health.viewmodel.CommonFeelingTypeInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex sh on 27.10.2015.
 */
public class SaveFeelingDialogFragment extends DialogFragment {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TYPE_ID = "EXTRA_TYPE_ID";

    public static final int EXTRA_BODYFEELING_TYPE_ID = 1;
    public static final int EXTRA_CUSTOMBODYFEELING_TYPE_ID = 2;
    public static final int EXTRA_COMMONFEELING_TYPE_ID = 3;
    public static final int EXTRA_CUSTOMCOMMONFEELING_TYPE_ID = 4;

    private IFeelingTypeInfo mFeelingTypeInfo = null;
    private Date mStartDate = new Date();
    private SimpleDateFormat mSDF = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
    private IRepository mRepository = null;

    public void setData(IFeelingTypeInfo feelingTypeInfo){
        this.mFeelingTypeInfo = feelingTypeInfo;
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static SaveFeelingDialogFragment newInstance(IFeelingTypeInfo feelingTypeInfo) {
        SaveFeelingDialogFragment f = new SaveFeelingDialogFragment();
        f.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        f.setData(feelingTypeInfo);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRepository = ((HealthApplication) getActivity().getApplication()).getRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.save_feeling_dialog, container, false);
        TextView tvFeeling = (TextView) v.findViewById(R.id.tvFeeling);
        Button btnOk = (Button)v.findViewById(R.id.btnOk);
        Button btnCancel = (Button)v.findViewById(R.id.btnCancel);
        if(mFeelingTypeInfo != null){
            tvFeeling.setText(String.format("%s %s", mSDF.format(mStartDate), mFeelingTypeInfo.getName()));
        }
        btnOk.setTag(mFeelingTypeInfo);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IFeelingTypeInfo feelingTypeInfo = (IFeelingTypeInfo) v.getTag();
                int typeId = -1;
                long id = -1;
                if(feelingTypeInfo instanceof BodyFeelingTypeInfo){
                    BodyFeelingTypeInfo bodyFeelingTypeInfo = (BodyFeelingTypeInfo) feelingTypeInfo;
                    if(bodyFeelingTypeInfo.getBodyFeelingType() != null){
                        typeId = EXTRA_BODYFEELING_TYPE_ID;
                        BodyFeeling bodyFeeling = new BodyFeeling();
                        bodyFeeling.setStartDate(mStartDate);
                        bodyFeeling.setBodyRegionId(bodyFeelingTypeInfo.getBodyRegion().getId());
                        bodyFeeling.setFeelingTypeId(bodyFeelingTypeInfo.getBodyFeelingType().getId());
                        mRepository.addBodyFeeling(bodyFeeling);
                        id = bodyFeeling.getId();
                    }
                    if(bodyFeelingTypeInfo.getCustomBodyFeelingType() != null){
                        typeId = EXTRA_CUSTOMBODYFEELING_TYPE_ID;
                        BodyFeeling bodyFeeling = new BodyFeeling();
                        bodyFeeling.setStartDate(mStartDate);
                        bodyFeeling.setBodyRegionId(bodyFeelingTypeInfo.getBodyRegion().getId());
                        bodyFeeling.setCustomFeelingTypeId(bodyFeelingTypeInfo.getCustomBodyFeelingType().getId());
                        mRepository.addBodyFeeling(bodyFeeling);
                        id = bodyFeeling.getId();
                    }
                }
                if(feelingTypeInfo instanceof CommonFeelingTypeInfo){
                    CommonFeelingTypeInfo commonFeelingTypeInfo = (CommonFeelingTypeInfo) feelingTypeInfo;
                    if(commonFeelingTypeInfo.getCommonFeelingType() != null){
                        typeId = EXTRA_COMMONFEELING_TYPE_ID;
                        CommonFeeling commonFeeling = new CommonFeeling();
                        commonFeeling.setStartDate(mStartDate);
                        commonFeeling.setCommonFeelingTypeId(commonFeelingTypeInfo.getCommonFeelingType().getId());
                        mRepository.addCommonFeeling(commonFeeling);
                        id = commonFeeling.getId();
                    }
                    if(commonFeelingTypeInfo.getCustomCommonFeelingType() != null){
                        typeId = EXTRA_CUSTOMCOMMONFEELING_TYPE_ID;
                        CommonFeeling commonFeeling = new CommonFeeling();
                        commonFeeling.setStartDate(mStartDate);
                        commonFeeling.setCustomCommonFeelingTypeId(commonFeelingTypeInfo.getCustomCommonFeelingType().getId());
                        mRepository.addCommonFeeling(commonFeeling);
                        id = commonFeeling.getId();
                    }
                }
                getActivity().getIntent().putExtra(EXTRA_TYPE_ID, typeId);
                getActivity().getIntent().putExtra(EXTRA_ID, id);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                dismiss();
            }
        });

        return v;
    }
}
