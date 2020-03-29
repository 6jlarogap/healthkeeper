package com.health.settings;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.db.UserDB;
import com.health.dialog.ColorDialogFragment;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.repository.IRepository;

public class PreferenceDefaultColorDialog extends DialogPreference {
    
    private Button btnDefaultColor;
    
    private ImageView ivDefaultColor;
    
    private IRepository mRepository;
    
    private int mChoosedDefaultColor;
        
    public PreferenceDefaultColorDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(true);
        setDialogLayoutResource(R.layout.preference_default_color_dialog_layout);
    }

    @Override
    public void onBindDialogView(View view) {                
        this.btnDefaultColor = (Button) view.findViewById(R.id.btnDefaultColor);
        this.ivDefaultColor = (ImageView) view.findViewById(R.id.ivDefaultColor);
        this.btnDefaultColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
        this.mRepository = ((HealthApplication) getContext().getApplicationContext()).getRepository();
        UserBodyFeelingType userBodyFeelingType =  this.mRepository.getDefaultUserBodyFeelingType();
        this.mChoosedDefaultColor = userBodyFeelingType.getColor();
        ivDefaultColor.setBackgroundColor(this.mChoosedDefaultColor);
        super.onBindDialogView(view);
    }
    
    public void showColorPickerDialog() {             
        int initialColor = this.mChoosedDefaultColor;
        ColorDialogFragment newFragment = ColorDialogFragment.newInstance(initialColor, null);
		newFragment.setCancelable(false);
		FragmentManager fm = ((SettingsActivity) getContext()).getFragmentManager();
		SettingsActivity settingsActivity = ((SettingsActivity) getContext());
		int mainPreferenceFragmentId = settingsActivity.getIntent().getIntExtra(SettingsActivity.EXTRA_MAIN_FRAGMENT_ID, -1);		
		MainPreferenceFragment mainPreferenceFragment = (MainPreferenceFragment) fm.findFragmentById(mainPreferenceFragmentId);		
		newFragment.setTargetFragment(mainPreferenceFragment, MainPreferenceFragment.CHOOSE_COLOR_DIALOG_FRAGMENT);
		newFragment.show(fm, "dialog");
    }
    
    public void updateData(int color){
    	mChoosedDefaultColor = color;            
        ivDefaultColor.setBackgroundColor(mChoosedDefaultColor);
    }

}
