package com.health.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.health.data.BaseDTO;
import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
import com.health.data.BodyRegion;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingGroup;
import com.health.data.CommonFeelingType;
import com.health.data.CustomBodyFeelingType;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomFactorType;
import com.health.data.DaoSession;
import com.health.data.Factor;
import com.health.data.FactorType;
import com.health.data.IDictionaryDTO;
import com.health.data.IGridGroup;
import com.health.data.IGridItem;
import com.health.data.IGridItemValue;
import com.health.data.UnitDimension;
import com.health.data.User;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.dialog.CustomValueDialogFragment;
import com.health.dialog.DateTimeDialogFragment;
import com.health.loader.BodyFeelingTypeData;
import com.health.loader.BodyFeelingTypeLoader;
import com.health.repository.IRepository;
import com.health.settings.Settings;
import com.health.view.TouchImageView;


public class QuestionnaireFragment extends Fragment implements View.OnTouchListener, LoaderManager.LoaderCallbacks<BodyFeelingTypeData> {

	private static final String SELECTED_TAB_ID = "selected_tab_id";

	private static final int BODYFEELINGTYPE_LOADER = 0x01;

	private IRepository mRepository;

	private TabHost tabHost;

	private int mSelectedTabId = 0;

	public static final int BODYFEELING_DATE_TIME_DIALOG_FRAGMENT = 10001;
	public static final int COMMONFEELING_DATE_TIME_DIALOG_FRAGMENT = 10002;
	public static final int FACTOR_DATE_TIME_DIALOG_FRAGMENT = 10003;
	
	public static final int ADD_CUSTOM_TYPE_ACTIVITY = 20001;
	public static final int ADD_CUSTOM_VALUE_DIALOG_FRAGMENT = 20002;

	public static final int VIEW_TYPE_ID_FRONT = 0;
	public static final int VIEW_TYPE_ID_BACK = 1;
	public int mSelectedZoneIndex = BaseDTO.INT_NULL_VALUE;

	public User mUser = null;
	public int mImageViewSex = User.ANONIM_SEX;
	public int mImageViewTypeId = VIEW_TYPE_ID_FRONT;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_CANCELED){
			if(requestCode == ADD_CUSTOM_VALUE_DIALOG_FRAGMENT){
				int typeId = (Integer) data.getExtras().get(CustomValueDialogFragment.EXTRA_TYPE_ID);
				switch (typeId) {
				case CustomTypeActivity.CUSTOMFACTOR_TYPE_ID:					
					mFactorGroupAdapter.cancelValues();
					break;
				case CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID:					
					mCommonFeelingGroupAdapter.cancelValues();
					break;
				}
			}
		}
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == BODYFEELING_DATE_TIME_DIALOG_FRAGMENT) {
				GregorianCalendar calendar = (GregorianCalendar) data.getExtras().get(DateTimeDialogFragment.DATE_VALUE);
				setDateForBodyFeeling(calendar.getTime());
			}
			if (requestCode == COMMONFEELING_DATE_TIME_DIALOG_FRAGMENT) {
				GregorianCalendar calendar = (GregorianCalendar) data.getExtras().get(DateTimeDialogFragment.DATE_VALUE);
				setDateForCommonFeeling(calendar.getTime());
			}			
			if (requestCode == FACTOR_DATE_TIME_DIALOG_FRAGMENT) {
				GregorianCalendar calendar = (GregorianCalendar) data.getExtras().get(DateTimeDialogFragment.DATE_VALUE);
				setDateForFactor(calendar.getTime());
			}
			if(requestCode == ADD_CUSTOM_VALUE_DIALOG_FRAGMENT){
				Double value1 = (Double) data.getExtras().get(CustomValueDialogFragment.EXTRA_VALUE1);
				Double value2 = (Double) data.getExtras().get(CustomValueDialogFragment.EXTRA_VALUE2);
				Double value3 = (Double) data.getExtras().get(CustomValueDialogFragment.EXTRA_VALUE3);
				int typeId = (Integer) data.getExtras().get(CustomValueDialogFragment.EXTRA_TYPE_ID);
				switch (typeId) {
				case CustomTypeActivity.CUSTOMFACTOR_TYPE_ID:					
					mFactorGroupAdapter.setValues(value1, value2, value3);
					break;
				case CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID:					
					mCommonFeelingGroupAdapter.setValues(value1, value2, value3);
					break;
				}
				
			}
			if(requestCode == ADD_CUSTOM_TYPE_ACTIVITY){
				if(data.getExtras().getBoolean(CustomTypeActivity.EXTRA_ANONIMUSER)){
					((MainActivity)getActivity()).checkRegistration();
				} else {
    				long id = (Long) data.getExtras().get(CustomTypeActivity.EXTRA_ID);
    				int typeId = (Integer) data.getExtras().get(CustomTypeActivity.EXTRA_TYPE_ID);
    				Double value1 = (Double) data.getExtras().get(CustomTypeActivity.EXTRA_VALUE1);
    				Double value2 = (Double) data.getExtras().get(CustomTypeActivity.EXTRA_VALUE2);
    				Double value3 = (Double) data.getExtras().get(CustomTypeActivity.EXTRA_VALUE3);
    				DaoSession daoSession = DB.db().newSession();
    				switch (typeId) {
    				case CustomTypeActivity.CUSTOMFACTOR_TYPE_ID:
    					CustomFactorType customFactorType = daoSession.getCustomFactorTypeDao().load(id);
    					mFactorGroupAdapter.addCustomType(customFactorType, value1, value2, value3);
    					break;
    				case CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID:
    					CustomCommonFeelingType customCommonFeelingType = daoSession.getCustomCommonFeelingTypeDao().load(id);
    					mCommonFeelingGroupAdapter.addCustomType(customCommonFeelingType, value1, value2, value3);
    					break;
    				}
				}
			}
		}
	}

	private int getDrawableIdBodyImage(int sex, int viewTypeId, boolean isMask) {
		int drawableId = -1;
		int[] drawableManIds = new int[] { R.drawable.man_face_color, R.drawable.man_back_color, R.drawable.man_face_black, R.drawable.man_back_black };
		int[] drawableWomanIds = new int[] { R.drawable.woman_face_color, R.drawable.woman_back_color, R.drawable.woman_face_black, R.drawable.woman_back_black };
		int[] drawableIds = null;
		switch (sex) {
		case User.ANONIM_SEX:
			drawableIds = drawableManIds;
			break;
		case User.MAN_SEX:
			drawableIds = drawableManIds;
			break;
		case User.WOMAN_SEX:
			drawableIds = drawableWomanIds;
			break;
		}
		if (isMask) {
			drawableId = drawableIds[viewTypeId + 2];
		} else {
			drawableId = drawableIds[viewTypeId];
		}
		return drawableId;
	}

	// BodyFeelingType
	private OnChangeBodyFeelingListener mOnChangeBodyFeelingListener;

	private Button btnDate;
	private ListView lvChooseBodyFeeling;
	private LinearLayout llChooseBodyFeeling;
	private Button btnSave, btnHistory;
	private TouchImageView ivBody;
	private LinearLayout llImageBody;
	private ImageView ivSelect;
	private RelativeLayout rlMain;
	private ImageButton btnFront, btnBack;
	private RelativeLayout rlHelpMessage;
	private ImageButton ibCloseHelpMessage;
	private ImageButton ibSelectMan, ibSelectWoman;
	private LinearLayout llSelectSex;
	private ScrollView svQuestionnnaireMain;
	private ScrollView svSelectSex;
	private ImageButton ibChangeSexBody;
	private TextView tvBodyFeeling;
	private EditText etCustomBodyFeelingType;
	private TextView tvBodyCoordinates;

	private Bitmap mBodyMask;

	private DictionaryDTOAdapter mChoseBodyFeelingAdapter;
	private int mBodyFeelingId = BaseDTO.INT_NULL_VALUE;
	private BodyRegion mSelectedBodyRegion = null;
	private BodyFeelingType mSelectedBodyFeelingType = null;
	private String mSelectedCustomBodyFeelingTypeName = null;
	private Date mSelectedDate = new Date();
	private int mBodyX = BaseDTO.INT_NULL_VALUE;
	private int mBodyY = BaseDTO.INT_NULL_VALUE;
	private int mBodyRegionId = BaseDTO.INT_NULL_VALUE;

	private boolean mIsVisibleHelpMessage = true;

	// CommonFeelingType
	private Button btnCommonFeelingDate;
	private Button btnCommonFeelingSave;
	private Button btnCommonFeelingHistory;
	private GridView gvCommonFeelingGroup;
	private RelativeLayout rlCommonFeelingGroup;
	private GroupGridAdapter mCommonFeelingGroupAdapter;
	private Date mCommonFeelingSelectedDate = new Date();
	

	// Factor
	private Button btnFactorDate;
	private Button btnFactorSave;
	private Button btnFactorHistory;
	private GridView gvFactorGroup;
	private RelativeLayout rlFactorGroup;
	private GroupGridAdapter mFactorGroupAdapter;
	private Date mFactorSelectedDate = new Date();
	public int mGridColumnWidth = 250;
	public int mGridRowHeight = 250;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mUser = UserDB.getCurrentUser();
		if (this.mImageViewSex == User.ANONIM_SEX) {
			this.mImageViewSex = this.mUser.getSex();
		}
		this.mOnChangeBodyFeelingListener = (OnChangeBodyFeelingListener) activity;
		this.mRepository = ((HealthApplication) getActivity().getApplication()).getRepository();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateTab1(savedInstanceState);
		onCreateTab2(savedInstanceState);
		onCreateTab3(savedInstanceState);
	}

	private void onCreateTab1(Bundle savedInstanceState) {
		this.mBodyFeelingId = getActivity().getIntent().getIntExtra(HistoryBodyFeelingActivity.EXTRA_FEELING_ID, BaseDTO.INT_NULL_VALUE);
		if (this.mBodyFeelingId != BaseDTO.INT_NULL_VALUE) {
			BodyFeeling bodyFeeling = mRepository.getBodyFeelingById(getActivity(), this.mBodyFeelingId);
			if (bodyFeeling != null) {
				this.mSelectedBodyRegion = bodyFeeling.getBodyRegion();
				this.mSelectedBodyFeelingType = bodyFeeling.getBodyFeelingType();
				this.mSelectedDate = bodyFeeling.getStartDate();

			}
		} else {
			this.mSelectedDate = new Date();
		}
	}

	private void onCreateTab2(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			this.mCommonFeelingSelectedDate = new Date();
		}
	}

	private void onCreateTab3(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			this.mFactorSelectedDate = new Date();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int displayWidth = size.x;
		float density =	getResources().getDisplayMetrics().density;
		int widthDpi = (int) (displayWidth / density);
		if(widthDpi > 750){
			mGridColumnWidth = widthDpi / 3 - 3 * 10;
		} else {
			if(widthDpi > 400){
				mGridColumnWidth = widthDpi / 2 - 2 * 10;
			} else {
				mGridColumnWidth = widthDpi - 1 * 10;
			}
		}
		
		View view = inflater.inflate(R.layout.questionnaire_fragment, null);
		this.tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator(getString(R.string.questionnaire_bodyfeeling_tab));
		/*
		 * Spanned spanned =
		 * Html.fromHtml(getString(R.string.questionnaire_bodyfeeling_tab));
		 * TextView textView = new TextView(getActivity());
		 * textView.setText(spanned); spec.setIndicator(textView);
		 */
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator(getString(R.string.questionnaire_commonfeeling_tab));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tag3");
		spec.setContent(R.id.tab3);
		spec.setIndicator(getString(R.string.questionnaire_factor_tab));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tag4");
		spec.setContent(R.id.tab4);
		spec.setIndicator(getString(R.string.questionnaire_estimation_health_tab));
		tabHost.addTab(spec);

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				mSelectedTabId = tabHost.getCurrentTab();
				if (mSelectedTabId == 0) {
					rlMain.postDelayed(new Runnable() {

						@Override
						public void run() {
							setNewImage(mImageViewTypeId);
							setRightSizeForImageBody();
						}
					}, 100);
				}
				switch (mSelectedTabId) {
				case 0:					
					setDateForBodyFeeling(new Date());
					break;
				case 1:
					setDateForCommonFeeling(new Date());
					break;
				case 2:
					setDateForFactor(new Date());
					break;
				default:
					break;
				}
			}
		});
		tabHost.setCurrentTab(mSelectedTabId);

		onCreateViewTab1(view, savedInstanceState);
		onCreateViewTab2(view, savedInstanceState);
		onCreateViewTab3(view, savedInstanceState);
		getLoaderManager().initLoader(BODYFEELINGTYPE_LOADER, null, this).forceLoad();
		return view;
	}
	
	public void resetLoader(){
		getLoaderManager().restartLoader(BODYFEELINGTYPE_LOADER, null, this).forceLoad();
	}

	private void onCreateViewTab1(View view, Bundle savedInstanceState) {
		this.lvChooseBodyFeeling = (ListView) view.findViewById(R.id.lvBodyFeelingType);
		this.llChooseBodyFeeling = (LinearLayout) view.findViewById(R.id.llBodyFeelingType);
		this.tvBodyFeeling = (TextView) view.findViewById(R.id.tvBodyFeeling);
		this.etCustomBodyFeelingType = (EditText) view.findViewById(R.id.etCustomBodyFeelingType);
		this.btnDate = (Button) view.findViewById(R.id.btnDate);
		this.btnSave = (Button) view.findViewById(R.id.btnSave);
		this.btnHistory = (Button) view.findViewById(R.id.btnHistory);
		this.tvBodyCoordinates = (TextView) view.findViewById(R.id.tvBodyCoordinates);
		if(mUser.isAnonim()){
			this.btnHistory.setVisibility(View.GONE);
		}
		this.ivBody = (TouchImageView) view.findViewById(R.id.ivBody);
		this.llImageBody = (LinearLayout) view.findViewById(R.id.llImageBody);
		this.ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
		this.rlMain = (RelativeLayout) view.findViewById(R.id.rlMain);
		this.btnFront = (ImageButton) view.findViewById(R.id.btnFront);
		this.btnBack = (ImageButton) view.findViewById(R.id.btnBack);
		this.rlHelpMessage = (RelativeLayout) view.findViewById(R.id.rlHelpMessage);
		this.ibCloseHelpMessage = (ImageButton) rlHelpMessage.findViewById(R.id.ibCloseHelpMessage);
		this.ibSelectMan = (ImageButton) view.findViewById(R.id.btnSelectMan);
		this.ibSelectWoman = (ImageButton) view.findViewById(R.id.btnSelectWoman);
		this.llSelectSex = (LinearLayout) view.findViewById(R.id.llSelectSex);
		this.svQuestionnnaireMain = (ScrollView) view.findViewById(R.id.scrollViewMain);
		this.svSelectSex = (ScrollView) view.findViewById(R.id.scrollViewSelectSex);
		this.ibChangeSexBody = (ImageButton) view.findViewById(R.id.ibChangeSexBody);
		if (mImageViewSex == User.ANONIM_SEX) {
			this.svSelectSex.setVisibility(View.VISIBLE);
			this.svQuestionnnaireMain.setVisibility(View.GONE);
		}
		setSmallImageForChangeSexBody();
		this.ibChangeSexBody.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeSexBody();
			}
		});

		this.ibSelectMan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mImageViewSex = User.MAN_SEX;
				svSelectSex.setVisibility(View.GONE);
				svQuestionnnaireMain.setVisibility(View.VISIBLE);
				setNewImage(mImageViewTypeId);
				setSmallImageForChangeSexBody();
			}
		});
		this.ibSelectWoman.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mImageViewSex = User.WOMAN_SEX;
				svSelectSex.setVisibility(View.GONE);
				svQuestionnnaireMain.setVisibility(View.VISIBLE);
				setNewImage(mImageViewTypeId);
				setSmallImageForChangeSexBody();
			}
		});
		this.ibCloseHelpMessage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsVisibleHelpMessage = false;
				updateHelpMessage(0);
			}
		});

		this.btnFront.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mImageViewTypeId == VIEW_TYPE_ID_BACK) {
					setNewImage(VIEW_TYPE_ID_FRONT);
				} else {
					setNewImage(VIEW_TYPE_ID_BACK);
				}
			}
		});

		this.btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mImageViewTypeId == VIEW_TYPE_ID_BACK) {
					setNewImage(VIEW_TYPE_ID_FRONT);
				} else {
					setNewImage(VIEW_TYPE_ID_BACK);
				}
			}
		});

		this.setDateForBodyFeeling(this.mSelectedDate);
		this.btnDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DateTimeDialogFragment newFragment = DateTimeDialogFragment.newInstance(new GregorianCalendar());
				newFragment.setTargetFragment(QuestionnaireFragment.this, BODYFEELING_DATE_TIME_DIALOG_FRAGMENT);
				newFragment.show(getFragmentManager(), "dialog");
			}
		});

		this.mChoseBodyFeelingAdapter = new DictionaryDTOAdapter(getActivity());
		this.lvChooseBodyFeeling.setAdapter(this.mChoseBodyFeelingAdapter);

		this.lvChooseBodyFeeling.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				IDictionaryDTO dto = (IDictionaryDTO) lvChooseBodyFeeling.getAdapter().getItem(position);
				if (dto instanceof BodyFeelingType) {
					mSelectedBodyFeelingType = (BodyFeelingType) dto;
					mSelectedCustomBodyFeelingTypeName = null;
				}
				if (dto instanceof CustomBodyFeelingType) {
					CustomBodyFeelingType customBodyFeelingType = (CustomBodyFeelingType) dto;
					mSelectedCustomBodyFeelingTypeName = customBodyFeelingType.getName();
					mSelectedBodyFeelingType = null;
				}
				updateSelectedBodyFeeling();
				mChoseBodyFeelingAdapter.clear();
			}
		});
		this.tvBodyFeeling.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSelectedBodyFeelingType != null) {
					mSelectedBodyFeelingType = null;
					mSelectedCustomBodyFeelingTypeName = null;
				} else {
					mSelectedBodyFeelingType = null;
					mSelectedCustomBodyFeelingTypeName = null;
					mSelectedBodyRegion = null;
				}
				updateSelectedBodyFeeling();
			}
		});
		this.etCustomBodyFeelingType.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				mSelectedCustomBodyFeelingTypeName = s.toString();
				updateSelectedBodyFeeling();
			}
		});

		this.ivBody.setOnTouchListener(this);
		
		this.btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				saveBodyFeeling();
			}
		});

		this.btnHistory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), HistoryBodyFeelingActivity.class);
				intent.putExtra(HistoryBodyFeelingActivity.EXTRA_FEELINGTYPE_ID, HistoryBodyFeelingActivity.BODYFEELING_TYPE_ID);
				startActivity(intent);
			}
		});
		updateSelectedBodyFeeling();
		setNewImage(mImageViewTypeId);		
	}

	private void onCreateViewTab2(View view, Bundle savedInstanceState) {
		this.btnCommonFeelingDate = (Button) view.findViewById(R.id.btnCommonFeelingDate);
		this.btnCommonFeelingSave = (Button) view.findViewById(R.id.btnCommonFeelingSave);
		this.btnCommonFeelingHistory = (Button) view.findViewById(R.id.btnCommonFeelingHistory);
		if(mUser.isAnonim()){
			this.btnCommonFeelingHistory.setVisibility(View.GONE);
		}
		this.gvCommonFeelingGroup = (GridView) view.findViewById(R.id.gvCommonFeelingGroup);
		this.rlCommonFeelingGroup = (RelativeLayout) view.findViewById(R.id.rlCommonFeelingGroup);

		if (this.mCommonFeelingGroupAdapter == null) {
			this.mCommonFeelingGroupAdapter = new GroupGridAdapter(getActivity(), GroupGridAdapter.COMMONFEELING_TYPE, this.rlCommonFeelingGroup);
		}
		this.mCommonFeelingGroupAdapter.setRootView(this.rlCommonFeelingGroup);		
		gvCommonFeelingGroup.setNumColumns(GridView.AUTO_FIT);
		gvCommonFeelingGroup.setColumnWidth(mGridColumnWidth);
		gvCommonFeelingGroup.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		this.gvCommonFeelingGroup.setAdapter(this.mCommonFeelingGroupAdapter);
		hackForGridView(this.gvCommonFeelingGroup);

		
		this.btnCommonFeelingDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DateTimeDialogFragment newFragment = DateTimeDialogFragment.newInstance(new GregorianCalendar());
				newFragment.setTargetFragment(QuestionnaireFragment.this, COMMONFEELING_DATE_TIME_DIALOG_FRAGMENT);
				newFragment.show(getFragmentManager(), "dialog");
			}
		});

		this.setDateForCommonFeeling(this.mCommonFeelingSelectedDate);
		
		this.mCommonFeelingGroupAdapter.setOnSaveListener(new OnSaveListener() {

			@Override
			public void onSave(ItemListAdapter itemListAdapter) {				
				HashMap<IGridItem, IGridItemValue> operativeValues = itemListAdapter.getOperativeValues();
				saveCommonFeeling(operativeValues);
				itemListAdapter.clearOperativeValues();
				itemListAdapter.notifyDataSetChanged();
			}
		});

		this.btnCommonFeelingHistory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), HistoryBodyFeelingActivity.class);
				intent.putExtra(HistoryBodyFeelingActivity.EXTRA_FEELINGTYPE_ID, HistoryBodyFeelingActivity.COMMONFEELING_TYPE_ID);
				startActivity(intent);
			}
		});

	}

	private void onCreateViewTab3(View view, Bundle savedInstanceState) {
		this.btnFactorDate = (Button) view.findViewById(R.id.btnFactorDate);
		this.btnFactorSave = (Button) view.findViewById(R.id.btnFactorSave);
		this.btnFactorHistory = (Button) view.findViewById(R.id.btnFactorHistory);
		if(mUser.isAnonim()){
			this.btnFactorHistory.setVisibility(View.GONE);
		}
		this.rlFactorGroup = (RelativeLayout) view.findViewById(R.id.rlFactorGroup);
		this.gvFactorGroup = (GridView) view.findViewById(R.id.gvFactorGroup);

		if (this.mFactorGroupAdapter == null) {
			this.mFactorGroupAdapter = new GroupGridAdapter(getActivity(), GroupGridAdapter.FACTOR_TYPE, this.rlFactorGroup);
		}
		this.mFactorGroupAdapter.setRootView(this.rlFactorGroup);		
		gvFactorGroup.setNumColumns(GridView.AUTO_FIT);
		gvFactorGroup.setColumnWidth(mGridColumnWidth);
		gvFactorGroup.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		this.gvFactorGroup.setAdapter(this.mFactorGroupAdapter);
		
		
						
		hackForGridView(this.gvFactorGroup);

		this.btnFactorDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DateTimeDialogFragment newFragment = DateTimeDialogFragment.newInstance(new GregorianCalendar());
				newFragment.setTargetFragment(QuestionnaireFragment.this, FACTOR_DATE_TIME_DIALOG_FRAGMENT);
				newFragment.show(getFragmentManager(), "dialog");
			}
		});

		this.setDateForFactor(this.mFactorSelectedDate);

		this.mFactorGroupAdapter.setOnSaveListener(new OnSaveListener() {

			@Override
			public void onSave(ItemListAdapter itemListAdapter) {				
				HashMap<IGridItem, IGridItemValue> operativeValues = itemListAdapter.getOperativeValues();
				saveFactor(operativeValues);
				itemListAdapter.clearOperativeValues();
				itemListAdapter.notifyDataSetChanged();
			}
		});

		this.btnFactorHistory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), HistoryBodyFeelingActivity.class);
				intent.putExtra(HistoryBodyFeelingActivity.EXTRA_FEELINGTYPE_ID, HistoryBodyFeelingActivity.FACTOR_TYPE_ID);
				startActivity(intent);
			}
		});		
	}
	
	// HACK: для того чтобы первый item в GridView отзывался на клики
	private void hackForGridView(final GridView gv) {
		gv.postDelayed(new Runnable() {

			@Override
			public void run() {
				int hackPosition = 1;
				if (gv.getAdapter().getCount() > hackPosition) {
					gv.performItemClick(gv.getAdapter().getView(hackPosition, null, null), hackPosition, gv.getAdapter().getItemId(hackPosition));
				}

			}
		}, 300);
	}

	private void changeSexBody() {
		if (mImageViewSex == User.MAN_SEX) {
			mImageViewSex = User.WOMAN_SEX;
			ibChangeSexBody.setImageResource(R.drawable.ic_man_face);
		} else {
			mImageViewSex = User.MAN_SEX;
			ibChangeSexBody.setImageResource(R.drawable.ic_woman_face);
		}
		setNewImage(mImageViewTypeId);
	}

	private void setSmallImageForChangeSexBody() {
		if (mUser.isAnonim()) {
			ibChangeSexBody.setVisibility(View.VISIBLE);
			if (mImageViewSex == User.MAN_SEX) {
				ibChangeSexBody.setImageResource(R.drawable.ic_woman_face);
			} else {
				ibChangeSexBody.setImageResource(R.drawable.ic_man_face);
			}
		}
	}

	private void saveBodyFeeling() {
		if (!((MainActivity)getActivity()).checkRegistration()) {
			return;
		}
		if (!((MainActivity)getActivity()).checkSaveBodyFeeling(mSelectedDate, mSelectedBodyFeelingType, mSelectedBodyRegion, mSelectedCustomBodyFeelingTypeName)) {
			return;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(mSelectedDate);
		if (mSelectedBodyFeelingType != null) {
			addBodyFeeling(calendar, mSelectedBodyFeelingType.getId(), null, mSelectedBodyRegion.getId(), mBodyX, mBodyY);
		} else {
			CustomBodyFeelingType customBodyFeelingType = new CustomBodyFeelingType();
			customBodyFeelingType.setName(mSelectedCustomBodyFeelingTypeName);
			customBodyFeelingType = mRepository.addCustomBodyFeelingType(customBodyFeelingType);
			addBodyFeeling(calendar, null, customBodyFeelingType.getId(), mSelectedBodyRegion.getId(), mBodyX, mBodyY);
		}
		Toast.makeText(getActivity(), getResources().getString(R.string.bodyfeeling_add_toast), Toast.LENGTH_LONG).show();
		resetBodyFeelingUI();
		getActivity().getIntent().putExtra(MainActivity.EXTRA_DATE_UPLOAD, mSelectedDate.getTime());
	}



	private void showMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title).setMessage(message).setIcon(R.drawable.heart).setCancelable(true).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void saveCommonFeeling(HashMap<IGridItem, IGridItemValue> commonFeelingHashMap) {
		if (!((MainActivity)getActivity()).checkRegistration()) {
			return;
		}
		if (!checkSaveCommonFeeling(commonFeelingHashMap)) {
			return;
		}
		for (IGridItem type : commonFeelingHashMap.keySet()) {
			IGridItemValue commonFeeling = commonFeelingHashMap.get(type);
			commonFeeling.setGridItem(type);			
			commonFeeling.setStartDate(mCommonFeelingSelectedDate);
			Double value1 = commonFeeling.getValue1();
			if(type instanceof CustomCommonFeelingType){
				mRepository.addCustomCommonFeelingType((CustomCommonFeelingType) commonFeeling.getGridItem());
			}
			mRepository.addCommonFeeling((CommonFeeling) commonFeeling);
		}
		this.mOnChangeBodyFeelingListener.onCompleteChangeUserData();		
		Toast.makeText(getActivity(), "Данные сохранены", Toast.LENGTH_SHORT).show();
		getActivity().getIntent().putExtra(MainActivity.EXTRA_DATE_UPLOAD, mCommonFeelingSelectedDate.getTime());
	}

	private void saveFactor(HashMap<IGridItem, IGridItemValue> factorHashMap) {
		if (!((MainActivity)getActivity()).checkRegistration()) {
			return;
		}
		if (!checkSaveFactor(factorHashMap)) {
			return;
		}
		for (IGridItem factorType : factorHashMap.keySet()) {
			IGridItemValue factor = factorHashMap.get(factorType);
			factor.setGridItem(factorType);			
			factor.setStartDate(mFactorSelectedDate);
			if(factorType instanceof CustomFactorType){
				mRepository.addCustomFactorType((CustomFactorType)factor.getGridItem());
			}
			mRepository.addFactor((Factor)factor);
		}
		this.mOnChangeBodyFeelingListener.onCompleteChangeUserData();
		Toast.makeText(getActivity(), "Данные сохранены", Toast.LENGTH_SHORT).show();
	}

	private boolean checkSaveCommonFeeling(HashMap<IGridItem, IGridItemValue> commonFeelingHashMap) {
		boolean result = true;
		Date nowDate = new Date();
		if (mCommonFeelingSelectedDate.before(mUser.getBirthDate())) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_before));
		}
		if (mCommonFeelingSelectedDate.after(nowDate)) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_after));
		}
		if (result) {
			GregorianCalendar gcFrom = new GregorianCalendar();
			GregorianCalendar gcTo = new GregorianCalendar();
			gcFrom.setTime(mCommonFeelingSelectedDate);
			gcTo.setTime(mCommonFeelingSelectedDate);
			gcFrom.add(Calendar.HOUR_OF_DAY, -1);
			gcTo.add(Calendar.HOUR_OF_DAY, 1);			
			for (IGridItem type : commonFeelingHashMap.keySet()) {
				List<CommonFeeling> list = null;
				if (type instanceof CommonFeelingType) {
					list = mRepository.getCommonFeelings(type.getId(), null, gcFrom.getTime(), gcTo.getTime());
				}
				if (type instanceof CustomCommonFeelingType) {
					list = mRepository.getCommonFeelings(null, type.getId(), gcFrom.getTime(), gcTo.getTime());
				}
				if (list.size() > 0) {
					result = false;
					showMessage(null, getString(R.string.incorect_value_feeling_date));
					break;
				}
			}

		}
		return result;
	}

	private boolean checkSaveFactor(HashMap<IGridItem, IGridItemValue> factorHashMap) {
		boolean result = true;
		Date nowDate = new Date();
		if (mFactorSelectedDate.before(mUser.getBirthDate())) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_before));
		}
		if (mFactorSelectedDate.after(nowDate)) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_after));
		}
		if (result) {
			GregorianCalendar gcFrom = new GregorianCalendar();
			GregorianCalendar gcTo = new GregorianCalendar();
			gcFrom.setTime(mFactorSelectedDate);
			gcTo.setTime(mFactorSelectedDate);
			gcFrom.add(Calendar.HOUR_OF_DAY, -1);
			gcTo.add(Calendar.HOUR_OF_DAY, 1);
			for (IGridItem factorType : factorHashMap.keySet()) {
				IGridItemValue factor = factorHashMap.get(factorType);
				List<Factor> list = null;
				if (factorType instanceof FactorType) {
					list = mRepository.getFactors(factorType.getId(), null, gcFrom.getTime(), gcTo.getTime());
				}
				if (factorType instanceof CustomFactorType) {
					list = mRepository.getFactors(null, factorType.getId(), gcFrom.getTime(), gcTo.getTime());
				}
				if (list.size() > 0) {
					result = false;
					showMessage(null, getString(R.string.incorect_value_factor_date));
					break;
				}

			}
		}
		return result;
	}

	@Override
	public void onResume() {
		super.onResume();
		onResumeTab1();
	}

	public void onResumeTab1() {
		setRightSizeForImageBody();
	}

	private void setRightSizeForImageBody() {
		ViewTreeObserver vto = this.ivBody.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				double ratio = 400.0 / 780.0;
				final int width, height;
				int prevHeight = ivBody.getHeight();
				int prevWidth = ivBody.getWidth();
				width = prevWidth;
				height = (int) (prevWidth / ratio);
				if (height > 0) {
					ivBody.getLayoutParams().height = height;
					ivBody.getLayoutParams().width = width;
					llImageBody.getLayoutParams().height = height;
					llImageBody.requestLayout();					
					updateHelpMessage(width / 2);
				}
				return true;
			}
		});

	}

	private void updateHelpMessage(int leftX) {
		if (mIsVisibleHelpMessage) {
			setMargins(rlHelpMessage, leftX, 0);
			rlHelpMessage.setVisibility(View.VISIBLE);
		} else {
			rlHelpMessage.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState != null){
			outState.putInt(SELECTED_TAB_ID, this.tabHost.getCurrentTab());
			onSaveInstanceStateTab1(outState);
		}
	}

	public void onSaveInstanceStateTab1(Bundle outState) {
		outState.putInt(HistoryBodyFeelingActivity.EXTRA_FEELING_ID, this.mBodyFeelingId);
	}

	private void resetBodyFeelingUI() {
		this.mBodyRegionId = BaseDTO.INT_NULL_VALUE;
		this.mBodyFeelingId = BaseDTO.INT_NULL_VALUE;
		this.mSelectedBodyRegion = null;
		this.mSelectedBodyFeelingType = null;
		this.mSelectedCustomBodyFeelingTypeName = null;
		this.etCustomBodyFeelingType.setText(null);
		updateSelectedBodyFeeling();
		ivSelect.setVisibility(View.GONE);
	}	

	private void updateSelectedBodyFeeling() {
		boolean isSaveEnabled = false;
		boolean isShowBodyFeeling = false;
		boolean isShowChooseBodyFeelingType = false;
		StringBuilder sbMessage = new StringBuilder();
		if (mSelectedBodyRegion != null) {
			isShowBodyFeeling = true;
			isShowChooseBodyFeelingType = true;
			sbMessage.setLength(0);
			sbMessage.append(String.format("%s", mSelectedBodyRegion.getName()));
		}
		if (mSelectedBodyFeelingType != null) {
			isShowBodyFeeling = true;
			isShowChooseBodyFeelingType = false;
			sbMessage.append(String.format("  %s", mSelectedBodyFeelingType.getName()));
		}
		if (mSelectedCustomBodyFeelingTypeName != null) {
			sbMessage.append(String.format("  %s", mSelectedCustomBodyFeelingTypeName));
		}

		if (mSelectedBodyRegion != null && mSelectedBodyFeelingType != null) {
			isSaveEnabled = true;
		}
		if (mSelectedBodyRegion != null && (mSelectedCustomBodyFeelingTypeName != null ? mSelectedCustomBodyFeelingTypeName.trim().length() > 2 : false)) {
			isSaveEnabled = true;
		}
		String htmlMessage = "<u>" + sbMessage.toString() + "</u>";
		Spanned spanMessage = android.text.Html.fromHtml(htmlMessage);
		this.tvBodyFeeling.setText(spanMessage);
		this.btnSave.setEnabled(isSaveEnabled);
		if (isShowBodyFeeling) {
			this.tvBodyFeeling.setVisibility(View.VISIBLE);
		} else {
			this.tvBodyFeeling.setVisibility(View.GONE);
		}
		if (isShowChooseBodyFeelingType) {
			this.llChooseBodyFeeling.setVisibility(View.VISIBLE);
		} else {
			this.llChooseBodyFeeling.setVisibility(View.GONE);
		}
	}

	private void initImageMask() {
		if (mBodyMask != null) {
			mBodyMask.recycle();
			mBodyMask = null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		int drawableId = getDrawableIdBodyImage(mImageViewSex, mImageViewTypeId, true);
		mBodyMask = BitmapFactory.decodeResource(getResources(), drawableId, options);
	}

	private void setNewImage(int viewTypeId) {
		int drawableId = getDrawableIdBodyImage(mImageViewSex, viewTypeId, false);
		this.mImageViewTypeId = viewTypeId;
		initImageMask();
		this.ivBody.setImageDrawable(getResources().getDrawable(drawableId));
		this.ivBody.refreshDrawableState();
		this.ivSelect.setVisibility(View.GONE);
	}

	private int getBodyRegionIdByImage(int x, int y) {
		int resultId = -1;
		int pixel = this.mBodyMask.getPixel(x, y);
		int BLUE_MASK = 0x0000FF;
		int blue = pixel & BLUE_MASK;
		if (pixel != 0) {
			resultId = blue;
		}
		return resultId;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		boolean result = true;		
		if (view == this.ivBody) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				mIsVisibleHelpMessage = false;
				updateHelpMessage(0);
				view.getParent().requestDisallowInterceptTouchEvent(true);
			}
			if(event.getAction() != MotionEvent.ACTION_CANCEL){
    			int leftX = (int) event.getX() - ivSelect.getWidth() / 2;
    			int topY = (int) event.getY() - ivSelect.getHeight() / 2;
    			setMargins(ivSelect, leftX, topY);
    			ivSelect.setVisibility(View.VISIBLE);
    			int x, y;
    			float pixelX, pixelY;
    			if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
    				RectF zoomRect = ivBody.getZoomedRect();
    				pixelX = view.getWidth() * zoomRect.left + event.getX() * (zoomRect.right- zoomRect.left);
    				pixelY = view.getHeight() * zoomRect.top + event.getY() * (zoomRect.bottom - zoomRect.top);	    				
    				x = (int) (mBodyMask.getWidth() * pixelX / view.getWidth());
    				y = (int) (mBodyMask.getHeight() * pixelY / view.getHeight());    				
    				int bodyregionid = getBodyRegionIdByImage(x, y);
    				this.mBodyX = x;
    				this.mBodyY = y;
    				this.tvBodyCoordinates.setText(String.format("x:%d, y:%d", x, y));
    				if(bodyregionid != mBodyRegionId){
    					this.updateListViewForChooseBodyFeeling(bodyregionid);
    					updateSelectedBodyFeeling();
    				}
    				this.mBodyRegionId = bodyregionid;
    			}
			}
		}
		return result;
	}

	private void updateListViewForChooseBodyFeeling(long bodyRegionId) {
		mSelectedBodyFeelingType = null;
		DaoSession daoSession = DB.db().newSession();
		mSelectedBodyRegion = daoSession.getBodyRegionDao().load(bodyRegionId);
		if (mSelectedBodyRegion != null) {
			List<BodyFeelingType> bodyFeelingTypes = mRepository.getBodyFeelingTypes(mSelectedBodyRegion.getId());
			List<CustomBodyFeelingType> customBodyFeelingTypes = mRepository.getCustomBodyFeelingTypes(mSelectedBodyRegion.getId());
			List<IDictionaryDTO> types = new ArrayList<IDictionaryDTO>();			
			types.addAll(bodyFeelingTypes);
			types.addAll(customBodyFeelingTypes);
			mChoseBodyFeelingAdapter.setData(types);
		}
		updateSelectedBodyFeeling();
	}

	private void setMargins(View view, int leftX, int topY) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
		params.setMargins(leftX, topY, 0, 0);
		view.setLayoutParams(params);
		view.invalidate();
	}

	public void setDateForBodyFeeling(Date date) {
		this.mSelectedDate = date;
		SimpleDateFormat sdf = new SimpleDateFormat(Settings.DATETIME_FORMAT_UI);
		this.btnDate.setText(sdf.format(date));
	}

	public void setDateForCommonFeeling(Date date) {
		this.mCommonFeelingSelectedDate = date;
		SimpleDateFormat sdf = new SimpleDateFormat(Settings.DATETIME_FORMAT_UI);
		this.btnCommonFeelingDate.setText(sdf.format(date));
	}

	public void setDateForFactor(Date date) {
		this.mFactorSelectedDate = date;
		SimpleDateFormat sdf = new SimpleDateFormat(Settings.DATETIME_FORMAT_UI);
		this.btnFactorDate.setText(sdf.format(date));
	}

	private void addBodyFeeling(GregorianCalendar bodyFeelingDate, Long bodyFeelingTypeId, Long customBodyFeelingTypeId, Long bodyRegionId, int x, int y) {
		BodyFeeling bodyFeeling = new BodyFeeling();
		bodyFeeling.setStartDate(bodyFeelingDate.getTime());
		if (customBodyFeelingTypeId != null) {
			bodyFeeling.setCustomFeelingTypeId(customBodyFeelingTypeId);
		}
		if (bodyFeelingTypeId != null) {
			bodyFeeling.setFeelingTypeId(bodyFeelingTypeId);			
		}
		bodyFeeling.setBodyRegionId(bodyRegionId);		
		bodyFeeling.setX(x);
		bodyFeeling.setY(y);
		this.mOnChangeBodyFeelingListener.onAddBodyFeeling(bodyFeeling);
	}

	public class DictionaryDTOAdapter extends ArrayAdapter<IDictionaryDTO> {

		private Context mContext;
		private List<? extends IDictionaryDTO> mItems;

		public DictionaryDTOAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1, new ArrayList<IDictionaryDTO>());
			this.mContext = context;
			this.mItems = new ArrayList<IDictionaryDTO>();
		}

		public void setData(List<? extends IDictionaryDTO> items) {
			clear();
			this.mItems = items;
			for (IDictionaryDTO dto : this.mItems) {
				insert(dto, this.getCount());
			}
			notifyDataSetChanged();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(android.R.layout.simple_list_item_1, null);
			}
			IDictionaryDTO item = getItem(position);
			if (item != null) {
				TextView tvName = (TextView) view.findViewById(android.R.id.text1);
				if (tvName != null) {
					tvName.setText(String.format("%s", item.getName()));
				}
			}
			return view;
		}

	}

	public class CommonFeelingGroupAdapter extends ArrayAdapter<CommonFeelingGroup> {

		private Context mContext;
		private List<CommonFeelingGroup> mItems;
		private int mSelectedPosition = AdapterView.INVALID_POSITION;

		public CommonFeelingGroupAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1, new ArrayList<CommonFeelingGroup>());
			this.mContext = context;
			this.mItems = new ArrayList<CommonFeelingGroup>();
		}

		public void setData(List<CommonFeelingGroup> items) {
			clear();
			this.mItems = items;
			for (CommonFeelingGroup group : this.mItems) {
				insert(group, this.getCount());
			}
			notifyDataSetChanged();
		}

		public void setSelectedPosition(int position) {
			this.mSelectedPosition = position;
			notifyDataSetChanged();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.item_list, null);
			}
			CommonFeelingGroup item = getItem(position);
			if (item != null) {
				TextView tvName = (TextView) view.findViewById(R.id.tvName);
				if (tvName != null) {
					tvName.setText(String.format("%s", item.getName()));
				}
			}
			return view;
		}

	}

	interface OnCommonFeelingChangeListener {
		void onChange();
	}

	interface OnChangeListener {
		void onChange(ItemListAdapter itemListAdapter);
	}

	interface OnSaveListener {
		void onSave(ItemListAdapter itemListAdapter);
	}

	static class ViewHolder {
		public TextView textView;
		public ListView listView;
		public Button btnMore;
		public Button btnSave;
		
		public Button dlgBtnSave;
		public Button dlgBtnCancel;
		public Button dlgBtnNew;
		
		
		
		public void setTag(Object object){
			if(textView != null){
				textView.setTag(object);
			}
			if(listView != null){
				listView.setTag(object);
			}
			if(btnMore != null){
				btnMore.setTag(object);
			}
			if(btnSave != null){
				btnSave.setTag(object);
			}
			if(dlgBtnSave != null){
				dlgBtnSave.setTag(object);
			}
			if(dlgBtnCancel != null){
				dlgBtnCancel.setTag(object);
			}
			if(dlgBtnNew != null){
				dlgBtnNew.setTag(object);
			}
		}
	}

	class GroupGridAdapter extends BaseAdapter {
		
		public static final int FACTOR_TYPE = CustomTypeActivity.CUSTOMFACTOR_TYPE_ID;
		public static final int COMMONFEELING_TYPE = CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID;
		
		private RelativeLayout mRelativeLayoutRootView;

		private Context mContext;
		private int mType;

		private View mCurrentGroupView = null;
		private IGridGroup mCurrentGroup = null; 

		private HashMap<IGridGroup, List<IGridItem>> mItemsHashMap;
		private ArrayList<IGridGroup> mItems = new ArrayList<IGridGroup>();

		private HashMap<Integer, ItemListAdapter> mFactorTypeAdapterHashMap = new HashMap<Integer, ItemListAdapter>();
		private HashMap<Integer, ViewHolder> mViewHolderHashMap = new HashMap<Integer, ViewHolder>();
		private HashMap<Integer, OnChangeListener> mGridBtnHashMap = new HashMap<Integer, OnChangeListener>();
		private HashMap<Integer, OnChangeListener> mDialogBtnHashMap = new HashMap<Integer, OnChangeListener>();
		private HashMap<Integer, View> mGridViewHashMap = new HashMap<Integer, View>();

		private OnSaveListener mOnSaveListener = null;
		
		public IGridGroup getCurrentGroup(){
			return mCurrentGroup;
		}
		
		public View getCurrentGroupView(){
			return mCurrentGroupView;
		}

		public void setOnSaveListener(OnSaveListener onSaveListener) {
			this.mOnSaveListener = onSaveListener;			
		}

		public GroupGridAdapter(Context context, int type, RelativeLayout rootView) {
			super();
			this.mContext = context;
			this.mType = type;
			this.mRelativeLayoutRootView = rootView;
		}
		
		public void setRootView(RelativeLayout rootView){
			this.mRelativeLayoutRootView = rootView;
		}

		public void setData(HashMap<IGridGroup, List<IGridItem>> factorTypeHashMap) {
			this.mItemsHashMap = factorTypeHashMap;
			this.mItems.clear();
			int pos = 0;
			for (IGridGroup group : this.mItemsHashMap.keySet()) {
				this.mItems.add(group);
				ItemListAdapter listAdapter = this.mFactorTypeAdapterHashMap.get(pos);
				if(listAdapter != null){
					listAdapter.setData(mItemsHashMap.get(group));
				}
				pos++;
			}
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(mViewHolderHashMap.containsKey(position)){
				holder = mViewHolderHashMap.get(position);
			} else {
				holder = new ViewHolder();
				mViewHolderHashMap.put(position, holder);
			}
			if(mGridViewHashMap.containsKey(position)){
				convertView = mGridViewHashMap.get(position);
			} else{
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.grid_item, parent, false);
				mGridViewHashMap.put(position, convertView);
			}
			holder.textView = (TextView) convertView.findViewById(R.id.tvText);
			holder.listView = (ListView) convertView.findViewById(R.id.lvValue);
			holder.btnMore = (Button) convertView.findViewById(R.id.btnMore);
			holder.btnSave = (Button) convertView.findViewById(R.id.btnSave);				
			holder.setTag(position);
			
			IGridGroup currentFactorGroup = mItems.get(position);
			SpannableString content = new SpannableString(currentFactorGroup.getName());
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			holder.textView.setText(content);
			ItemListAdapter factorAdapter = null;
			if (mFactorTypeAdapterHashMap.containsKey(position)) {
				factorAdapter = mFactorTypeAdapterHashMap.get(position);				
			} else {
				factorAdapter = new ItemListAdapter(mContext, mItemsHashMap.get(mItems.get(position)), mType);
				factorAdapter.setPosition(position);
				mFactorTypeAdapterHashMap.put(position, factorAdapter);
				OnChangeListener btnChangeListener = null;
				if(mGridBtnHashMap.containsKey(position)){
					btnChangeListener = mGridBtnHashMap.get(position);
				} else {
					btnChangeListener = new OnChangeListener() {
						
						@Override
						public void onChange(ItemListAdapter factorAdapter) {
							int position = factorAdapter.getPosition();
							if (factorAdapter.isExistOperativeValues()) {
								mViewHolderHashMap.get(position).btnSave.setEnabled(true);								
							} else {								
								mViewHolderHashMap.get(position).btnSave.setEnabled(false);
							}
							
						}
					};
					mGridBtnHashMap.put(position, btnChangeListener);
				}
				factorAdapter.setOnChangeListener(btnChangeListener);				
			}
			holder.listView.setAdapter(factorAdapter);
			holder.btnMore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mCurrentGroupView != null) {
						mRelativeLayoutRootView.removeView(mCurrentGroupView);
					}					
					int position = (Integer) v.getTag();
					LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					mCurrentGroupView = vi.inflate(R.layout.enter_checked_dialog, null);
					mCurrentGroup = mItems.get(position);
					ItemListAdapter listAdapter = mFactorTypeAdapterHashMap.get(position);
					TextView tvTitle = (TextView) mCurrentGroupView.findViewById(R.id.tvTitle);
					ListView lv = (ListView) mCurrentGroupView.findViewById(R.id.lvValue);
					lv.setAdapter(listAdapter);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mGridColumnWidth * 3 / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
					ViewHolder holder = mViewHolderHashMap.get(position);
					holder.dlgBtnNew = (Button) mCurrentGroupView.findViewById(R.id.btnNewCustom);					
					holder.dlgBtnSave = (Button) mCurrentGroupView.findViewById(R.id.btnSave);
					holder.dlgBtnCancel = (Button) mCurrentGroupView.findViewById(R.id.btnCancel);
					switch(mType){
					case FACTOR_TYPE:
						holder.dlgBtnNew.setText("Новый фактор");
						break;
					case COMMONFEELING_TYPE:
						holder.dlgBtnNew.setText("Новое ощущение");
						break;
					}
					holder.setTag(position);
					OnChangeListener dlgBtnChangeListener = null;
					if(mDialogBtnHashMap.containsKey(position)){
						dlgBtnChangeListener = mDialogBtnHashMap.get(position);
					} else {
						dlgBtnChangeListener = new OnChangeListener() {
							
							@Override
							public void onChange(ItemListAdapter factorAdapter) {
								int position = factorAdapter.getPosition();
								ViewHolder holder = mViewHolderHashMap.get(position);
								if (factorAdapter.isExistOperativeValues()) {
	    							holder.dlgBtnSave.setEnabled(true);
	    						} else {
	    							holder.dlgBtnSave.setEnabled(false);								
	    						}								
							}
						};
						mDialogBtnHashMap.put(position, dlgBtnChangeListener);
					}
					listAdapter.setOnChangeListener(dlgBtnChangeListener);

					tvTitle.setText(mItems.get(position).getName());
					int columnCount = 0;
					int width_cell = 0;
					switch (mType) {
					case FACTOR_TYPE:
						columnCount = gvFactorGroup.getNumColumns();
						width_cell = gvFactorGroup.getWidth() / columnCount;
						break;
					case COMMONFEELING_TYPE:
						columnCount = gvCommonFeelingGroup.getNumColumns();
						width_cell = gvCommonFeelingGroup.getWidth() / columnCount;
						break;
					default:
						break;
					}
					
					int row = (int) Math.ceil(position / columnCount);
					int column = position - row * columnCount;
					int x = column * width_cell - width_cell / 4;
					int y = position/columnCount * mGridRowHeight;
					y = (int) ((View) v.getParent().getParent().getParent()).getY();
					x = x > 0 ? x : 0;
					y = y > 0 ? y : 0;
					params.setMargins(x, y, 0, 0);
					mCurrentGroupView.setLayoutParams(params);
					mRelativeLayoutRootView.addView(mCurrentGroupView);
					holder.dlgBtnSave.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							int position = (Integer) v.getTag();
							ItemListAdapter factorAdapter = mFactorTypeAdapterHashMap.get(position);
							saveFactor(factorAdapter);
							mRelativeLayoutRootView.removeView(mCurrentGroupView);
						}
					});
					holder.dlgBtnCancel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							int position = (Integer) v.getTag();
							ItemListAdapter factorAdapter = mFactorTypeAdapterHashMap.get(position);							
							factorAdapter.clearOperativeValues();
							factorAdapter.notifyDataSetChanged();
							mRelativeLayoutRootView.removeView(mCurrentGroupView);
						}
					});
					holder.dlgBtnNew.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							int position = (Integer) v.getTag();
							ItemListAdapter listAdapter = mFactorTypeAdapterHashMap.get(position);
							Intent intent = new Intent(getActivity(), CustomTypeActivity.class);
							intent.putExtra(CustomTypeActivity.EXTRA_PARENT_ID, mItems.get(position).getId());
							switch(mType){
    							case FACTOR_TYPE:
    								intent.putExtra(CustomTypeActivity.EXTRA_TYPE_ID, CustomTypeActivity.CUSTOMFACTOR_TYPE_ID);
    								break;
    							case COMMONFEELING_TYPE:
    								intent.putExtra(CustomTypeActivity.EXTRA_TYPE_ID, CustomTypeActivity.CUSTOMCOMMONFEELING_TYPE_ID);
    								break;
							}
							startActivityForResult(intent, ADD_CUSTOM_TYPE_ACTIVITY);
						}
					});
					holder.dlgBtnSave.setEnabled(listAdapter.isExistOperativeValues());
				}
			});

			holder.btnSave.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					ItemListAdapter factorAdapter = mFactorTypeAdapterHashMap.get(position);					
					saveFactor(factorAdapter);
				}
			});
			holder.btnSave.setEnabled(factorAdapter.isExistOperativeValues());			
			return convertView;
		}

		private void saveFactor(ItemListAdapter factorAdapter) {
			if (mOnSaveListener != null) {
				mOnSaveListener.onSave(factorAdapter);
			}
		}		
		
		public void addCustomType(IGridItem type, Double value1, Double value2, Double value3) {
			int position = -1;
			for(IGridGroup group : mItems){
				position++;
				if(group.getId() == type.getGroup().getId()){
					break;
				}
			}
			ItemListAdapter listAdapter = mFactorTypeAdapterHashMap.get(position);
			listAdapter.add(type);
			IGridItemValue value = null;
			if(type instanceof CustomFactorType){
				value = new Factor();
			}
			if(type instanceof CustomCommonFeelingType){
				value = new CommonFeeling();
			}
			value.setGridItem(type);
			value.setValue1(value1);
			value.setValue2(value2);
			value.setValue3(value3);
			listAdapter.addOperativeValue(value);
			listAdapter.notifyDataSetChanged();
			ViewHolder holder = mViewHolderHashMap.get(position);
			if (listAdapter.isExistOperativeValues()) {
				if(holder.dlgBtnSave != null){
					holder.dlgBtnSave.setEnabled(true);
				}
				if(holder.btnSave != null){
					holder.btnSave.setEnabled(true);
				}				
			} else {
				if(holder.dlgBtnSave != null){
					holder.dlgBtnSave.setEnabled(false);
				}
				if(holder.btnSave != null){
					holder.btnSave.setEnabled(false);
				}						
			}
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);

		}

		@Override
		public long getItemId(int position) {
			return mItems.get(position).getId();
		}
		
		public void setValues(Double value1, Double value2, Double value3){
			for(ItemListAdapter listAdapter : mFactorTypeAdapterHashMap.values()){
				if(listAdapter.getInputValue() != null){
					listAdapter.setValues(value1, value2, value3);
					listAdapter.notifyDataSetChanged();
					break;
				}
			}
			
		}
		
		public void cancelValues(){
			for(ItemListAdapter listAdapter : mFactorTypeAdapterHashMap.values()){
				if(listAdapter.getInputValue() != null){
					listAdapter.resetInputValue();
					listAdapter.notifyDataSetChanged();
					break;
				}
			}
			
		}

	}

	class ItemListAdapter extends ArrayAdapter<IGridItem> {
		
		private int mType;

		private HashMap<IGridItem, IGridItemValue> mOperativeValuesHashMap;

		private ArrayList<OnChangeListener> mOnChangeListenerList = new ArrayList<OnChangeListener>();
		
		private int position = AdapterView.INVALID_POSITION;
		
		private IGridItemValue mInputValue = null;
		
		public IGridItemValue getInputValue(){
			return mInputValue;
		}
		
		public IGridItemValue resetInputValue(){
			for(IGridItem key : mOperativeValuesHashMap.keySet()){
				IGridItemValue value = mOperativeValuesHashMap.get(key);
				if(value == mInputValue) {
					removeOperativeValue(value);
					this.mInputValue = null;
					break;
				}
			}
			return mInputValue;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public void setOnChangeListener(OnChangeListener onChangeListener) {
			boolean isAdd = true;
			for(OnChangeListener listener : mOnChangeListenerList){
				if(listener == onChangeListener){
					isAdd = false;
					break;
				}
			}
			if(isAdd){
				mOnChangeListenerList.add(onChangeListener);
			}	
		}

		public ItemListAdapter(Context context, List<IGridItem> factorTypes, int type) {
			super(context, 0, factorTypes);
			this.mOperativeValuesHashMap = new HashMap<IGridItem, IGridItemValue>();
			this.mType = type;
		}
		
		public void setData(List<IGridItem> factorTypes){
			clear();
			addAll(factorTypes);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			IGridItem gridItem = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox_list, parent, false);
			}
			CheckBox cbValue = (CheckBox) convertView.findViewById(R.id.cbValue);
			TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);
			cbValue.setTag(gridItem);			
			tvValue.setTag(gridItem);
			boolean isCheck = false;
			IGridItemValue gridItemValue = null;
			if (mOperativeValuesHashMap.containsKey(gridItem)) {
				gridItemValue = mOperativeValuesHashMap.get(gridItem);
				isCheck = true;
			}
			
			if(gridItemValue != null){
				switch ((int)gridItem.getUnitId()) {
				case (int) UnitDimension.NUMBER_TYPE:
					if(gridItemValue.getValue1() != null){
						String v1 = gridItemValue.getValue1().longValue() == gridItemValue.getValue1() ? "" + gridItemValue.getValue1().longValue() : "" + gridItemValue.getValue1();
						cbValue.setText(String.format("%s (%s)", gridItem.getName(), v1));
					} else {
						cbValue.setText(gridItem.getName());
					}
					break;
				case (int) UnitDimension.NUMBER_NUMBER_TYPE:
					if(gridItemValue.getValue1() != null && gridItemValue.getValue2() != null){
						String v1 = gridItemValue.getValue1().longValue() == gridItemValue.getValue1() ? "" + gridItemValue.getValue1().longValue() : "" + gridItemValue.getValue1();
						String v2 = gridItemValue.getValue2().longValue() == gridItemValue.getValue2() ? "" + gridItemValue.getValue2().longValue() : "" + gridItemValue.getValue2();
						cbValue.setText(String.format("%s (%s/%s)", gridItem.getName(), v1, v2));
					} else {
						cbValue.setText(gridItem.getName());
					}
					break;
				default:
					cbValue.setText(gridItem.getName());
					break;
				}
			} else {
				cbValue.setText(gridItem.getName());
			}
			if(isCheck){
				tvValue.setVisibility(View.VISIBLE);				
				tvValue.setText(Integer.toString(gridItem.getOrdinalNumber()));
				tvValue.setBackgroundColor(gridItem.getColor());
			} else {
				tvValue.setVisibility(View.VISIBLE);
				tvValue.setText(Integer.toString(gridItem.getOrdinalNumber()));
				tvValue.setBackgroundColor(gridItem.getColor());
			}
			cbValue.setChecked(isCheck);
			cbValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					IGridItem tagObject = (IGridItem) buttonView.getTag();
					IGridItemValue value = null;					
					switch (mType) {
					case GroupGridAdapter.FACTOR_TYPE:
						value = new Factor();
						break;
					case GroupGridAdapter.COMMONFEELING_TYPE:
						value = new CommonFeeling();
						break;
					}
					if(mOperativeValuesHashMap.containsKey(tagObject)){
						value = mOperativeValuesHashMap.get(tagObject);
					}
					value.setGridItem(tagObject);
					value.setStartDate(new Date());
					if (isChecked) {
						if(tagObject.getUnitId() != UnitDimension.BOOLEAN_TYPE){
							if(mInputValue == null && value.getValue1() == null && value.getValue2() == null && value.getValue3() == null){
								mInputValue = value;
								CustomValueDialogFragment newFragment = CustomValueDialogFragment.newInstance(tagObject, value, mType);
								newFragment.setCancelable(false);
								newFragment.setTargetFragment(QuestionnaireFragment.this, ADD_CUSTOM_VALUE_DIALOG_FRAGMENT);
								newFragment.show(getFragmentManager(), "dialog");
							}
						}						
						addOperativeValue(value);
					} else {
						removeOperativeValue(value);
					}
					for(OnChangeListener listener : mOnChangeListenerList){
						if (listener != null) {
							listener.onChange(ItemListAdapter.this);
						}
					}					
				}
			});
			return convertView;
		}

		public void addOperativeValue(IGridItemValue value) {
			IGridItem key = value.getGridItem();			
			mOperativeValuesHashMap.put(key, value);
		}

		public void removeOperativeValue(IGridItemValue value) {
			IGridItem key = value.getGridItem();
			mOperativeValuesHashMap.remove(key);			
		}

		public HashMap<IGridItem, IGridItemValue> getOperativeValues() {
			return this.mOperativeValuesHashMap;
		}

		public void clearOperativeValues() {
			this.mOperativeValuesHashMap.clear();
		}

		public boolean isExistOperativeValues() {
			boolean result = false;
			if (this.mOperativeValuesHashMap.size() > 0) {
				result = true;
			}
			return result;
		}
		
		public void setValues(Double value1, Double value2, Double value3){
			if(mInputValue != null){
				mInputValue.setValue1(value1);
				mInputValue.setValue2(value2);
				mInputValue.setValue3(value3);				
			}
			mInputValue = null;
		}
	}

	@Override
	public Loader<BodyFeelingTypeData> onCreateLoader(int id, Bundle args) {
		return new BodyFeelingTypeLoader(getActivity().getBaseContext());
	}

	@Override
	public void onLoadFinished(Loader<BodyFeelingTypeData> loader, BodyFeelingTypeData data) {
		this.mCommonFeelingGroupAdapter.setData(data.getCommonFeelingTypeHashMap());
		this.mFactorGroupAdapter.setData(data.getFactorTypeHashMap());
	}

	@Override
	public void onLoaderReset(Loader<BodyFeelingTypeData> loader) {
		getLoaderManager().restartLoader(BODYFEELINGTYPE_LOADER, null, this).forceLoad();
	}

}