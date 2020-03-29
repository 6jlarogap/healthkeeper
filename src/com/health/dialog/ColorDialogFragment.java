package com.health.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.health.main.R;

public class ColorDialogFragment extends DialogFragment {

	public static final String EXTRA_COLOR = "EXTRA_COLOR";
	
	public static final String EXTRA_BODYFEELINGTYPEID = "EXTRA_BODYFEELINGTYPEID";
	
	private int mColor;
	
	private Long mBodyFeelingTypeId = null;
	
	private ColorArrayAdapter mColorArrayAdapter = null;

	private GridView gv;

	public void setInitValue(int color, Long bodyFeelingTypeId) {
		this.mColor = color;
		this.mBodyFeelingTypeId = bodyFeelingTypeId;
		
	}

	public static ColorDialogFragment newInstance(int color, Long bodyFeelingTypeId) {
		ColorDialogFragment f = new ColorDialogFragment();
		f.setInitValue(color, bodyFeelingTypeId);
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
		View v = inflater.inflate(R.layout.color_dialog_fragment, container, false);
		this.gv = (GridView) v.findViewById(R.id.gv);
		this.mColorArrayAdapter = new ColorArrayAdapter(getActivity());
		Button bntCancel = (Button) v.findViewById(R.id.btnCancel);
		getDialog().setTitle("Выберите цвет");
		
		bntCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
	    		dismiss();
			}
		});
		this.gv.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		this.gv.setAdapter(mColorArrayAdapter);
		this.gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				int color = mColorArrayAdapter.COLORS[position];
				chooseColor(color);
			}
		});
		
		return v;
	}
	
	public void chooseColor(int color){
		mColor = color;
		if(mBodyFeelingTypeId != null){
			getActivity().getIntent().putExtra(EXTRA_BODYFEELINGTYPEID, mBodyFeelingTypeId);
		}
		getActivity().getIntent().putExtra(EXTRA_COLOR, mColor);
		getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
		dismiss();
	}
	
	class ColorArrayAdapter extends ArrayAdapter<Integer>{
		
		public final int[] COLORS = {Color.BLACK, Color.DKGRAY, Color.GRAY,
				Color.MAGENTA, Color.RED, Color.rgb(255, 128, 0),
				Color.YELLOW, Color.GREEN, Color.CYAN,  Color.BLUE};

		public ColorArrayAdapter(Context context) {
	        super(context, android.R.layout.simple_list_item_1);        
        }
		
		@Override
		public int getCount(){
			return COLORS.length;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.color_item, parent, false);
			LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.ll);
			//Button btn = new Button(getContext());
			int color = COLORS[position];
			ll.setBackgroundColor(color);
			ll.setTag(color);
			/*ll.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					mColor = (Integer) v.getTag();
					if(mBodyFeelingTypeId != null){
						getActivity().getIntent().putExtra(EXTRA_BODYFEELINGTYPEID, mBodyFeelingTypeId);
					}
					getActivity().getIntent().putExtra(EXTRA_COLOR, mColor);
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
					dismiss();
					
				}
			});*/
			return rowView;
		}
	}

}
