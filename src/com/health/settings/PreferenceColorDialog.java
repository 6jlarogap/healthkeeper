package com.health.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.health.data.BaseDTO;
import com.health.data.BodyFeelingType;
import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.db.DB;
import com.health.dialog.ColorDialogFragment;
import com.health.dialog.CustomValueDialogFragment;
import com.health.main.HealthApplication;
import com.health.main.QuestionnaireFragment;
import com.health.main.R;
import com.health.repository.IRepository;

public class PreferenceColorDialog extends DialogPreference {
    
    private EditText etFilterBodyFeelingType;
    
    private ListView lvItems;
    
    private int mDefaultColor;
    
    private IRepository mRepository;
    
    public PreferenceColorDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(true);
        setDialogLayoutResource(R.layout.preference_color_dialog_layout);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(R.string.settings_choose_color_dialog);
        builder.setPositiveButton(getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();                
            }
        });
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    public void onBindDialogView(View view) {
        this.lvItems = (ListView) view.findViewById(R.id.lvItems);
        this.etFilterBodyFeelingType = (EditText) view.findViewById(R.id.etFilterBodyFeelingType);
        
        this.mRepository = ((HealthApplication) getContext().getApplicationContext()).getRepository();
        UserBodyFeelingType defaultUserBodyFeelingType =  this.mRepository.getDefaultUserBodyFeelingType();
        this.mDefaultColor = defaultUserBodyFeelingType.getColor();
        
        initListView();
        ThreadManager.startGetData(this.mRepository, this.lvItems, this.etFilterBodyFeelingType.getText().toString().toLowerCase());
        
        this.etFilterBodyFeelingType.addTextChangedListener(new TextWatcher() {
            
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
                ThreadManager.startGetData(mRepository, lvItems, s.toString().toLowerCase());
            }
        });
        
        super.onBindDialogView(view);
    }
    
    public void showColorPickerDialog(long bodyFeelingTypeId) {               
        int initialColor = mRepository.getUserColorForBodyFeelingType(bodyFeelingTypeId);
        ColorDialogFragment newFragment = ColorDialogFragment.newInstance(initialColor, bodyFeelingTypeId);
		newFragment.setCancelable(false);
		FragmentManager fm = ((SettingsActivity) getContext()).getFragmentManager();
		SettingsActivity settingsActivity = ((SettingsActivity) getContext());
		int mainPreferenceFragmentId = settingsActivity.getIntent().getIntExtra(SettingsActivity.EXTRA_MAIN_FRAGMENT_ID, -1);		
		MainPreferenceFragment mainPreferenceFragment = (MainPreferenceFragment) fm.findFragmentById(mainPreferenceFragmentId);		
		newFragment.setTargetFragment(mainPreferenceFragment, MainPreferenceFragment.CHOOSE_COLOR_DIALOG_FRAGMENT);
		newFragment.show(fm, "dialog");				
    }
    
    public void updateData(){
    	ThreadManager.startGetData(mRepository, lvItems, etFilterBodyFeelingType.getText().toString().toLowerCase());
    }
    
    public void initListView(){
        List<UserBodyFeelingType> items =  new ArrayList<UserBodyFeelingType>();
        BodyFeelingTypeColorAdapter adapter = new BodyFeelingTypeColorAdapter(getContext(), items);
        this.lvItems.setAdapter(adapter);
    }
    
    static class GetUserBodyFeelingTypeRunnable implements Runnable {
        
        private final GetUserBodyFeelingTypeTask mTask;
        
        GetUserBodyFeelingTypeRunnable(GetUserBodyFeelingTypeTask task) {
            this.mTask = task;
        }
        
        @Override
        public void run() {
            this.mTask.setThread(Thread.currentThread());                        
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            List<UserBodyFeelingType> items =  mTask.getRepository().getUserBodyFeelingTypes(this.mTask.getFilter());
            this.mTask.setResult(items);            
            this.mTask.handleState(ThreadManager.COMPLETE_MSG_CODE);
        }
        
    }
    
    static class GetUserBodyFeelingTypeTask {
        
        private Thread mThreadThis;
        
        private String mFilter;
        
        private ListView mListView;
        
        private ThreadManager mThreadManager;
        
        private IRepository mRepository;
        
        private List<UserBodyFeelingType> mResult;
        
        public IRepository getRepository() {
            return mRepository;
        }

        public GetUserBodyFeelingTypeTask(ThreadManager threadManager, IRepository repository, ListView listView, String filterBodyFeelingTypeName){
            this.mThreadManager = threadManager;
            this.mListView = listView;
            this.mFilter = filterBodyFeelingTypeName;
            this.mRepository = repository;
        }
        
        public Thread getThread() {
            return mThreadThis;
        }

        public void setThread(Thread mThreadThis) {
            this.mThreadThis = mThreadThis;
        }

        public ListView getListView() {
            return mListView;
        }
        
        public String getFilter() {
            return mFilter;
        }

        public void setFilter(String mFilter) {
            this.mFilter = mFilter;
        }
        
        public void handleState(int state) {
            this.mThreadManager.handleState(this, state);
        }
        
        public void setResult(List<UserBodyFeelingType> result){
            this.mResult = result;
        }
        
        public List<UserBodyFeelingType> getResult(){
            return mResult;
        }
        
    }
    
    public static class ThreadManager {
        
        private static final int COMPLETE_MSG_CODE = 0;
        
        private static ThreadManager sInstance = null;
        
        private static Thread mThread;
                
        private Handler mHandlerUI;       
                
        static {
            sInstance = new ThreadManager();
        }
        
        private ThreadManager() {
            mHandlerUI = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message inputMessage) {
                    GetUserBodyFeelingTypeTask task = (GetUserBodyFeelingTypeTask) inputMessage.obj;
                    sInstance.updateDataInListView(task);
                }
            };
        }
        
        public static ThreadManager getInstance() {
            return sInstance;
        }
        
        public static GetUserBodyFeelingTypeTask startGetData(IRepository repository, ListView listView, String filter) {            
            synchronized (sInstance) {                
                if(mThread != null && mThread.isAlive()){
                    mThread.interrupt();
                }
            }
            GetUserBodyFeelingTypeTask task = new GetUserBodyFeelingTypeTask(getInstance(), repository, listView, filter);
            GetUserBodyFeelingTypeRunnable mRunnable = new GetUserBodyFeelingTypeRunnable(task);
            mThread = new Thread(mRunnable);
            mThread.start();
            return task;
        }
        
        //from background thread
        public void handleState(GetUserBodyFeelingTypeTask task, int state) {     
            Message completeMessage = mHandlerUI.obtainMessage(state, task);
            completeMessage.sendToTarget();                  
        }
        
        //from UI thread
        public void updateDataInListView(GetUserBodyFeelingTypeTask task){
            ListView listView = task.getListView();
            if (listView != null) {
                List<UserBodyFeelingType> items =  task.getResult();
                BodyFeelingTypeColorAdapter adapter = (BodyFeelingTypeColorAdapter) listView.getAdapter();
                adapter.setItems(items);
            }            
        }
        
    } 
    
    public class BodyFeelingTypeColorAdapter extends BaseAdapter {

        private List<UserBodyFeelingType> mItems;
        private Context mContext;

        public BodyFeelingTypeColorAdapter(Context context, List<UserBodyFeelingType> items) {
            this.mContext = context;
            this.mItems = items;
        }
        
        public void setItems(List<UserBodyFeelingType> items){
            this.mItems = items;
            this.notifyDataSetChanged();          
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.preference_color_list_item, parent, false);
            }
            TextView tvBodyFeelingType = (TextView) convertView.findViewById(R.id.tv_bodyfeelingtype);
            Button btnColor = (Button) convertView.findViewById(R.id.btn_color);
            ImageView ivColor = (ImageView) convertView.findViewById(R.id.iv_сolor);
            UserBodyFeelingType item =  mItems.get(position);
            BodyFeelingType bodyFeelingType = DB.db().newSession().getBodyFeelingTypeDao().load(item.getBodyFeelingTypeId());
            tvBodyFeelingType.setText(bodyFeelingType.getName());
            btnColor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    long bodyFeelingTypeId = (Long) v.getTag();
                    showColorPickerDialog(bodyFeelingTypeId);
                }
            });
            if(item.getId() != null){
                ivColor.setBackgroundColor(item.getColor());
            } else {
                ivColor.setBackgroundColor(mDefaultColor);
            }            
            btnColor.setTag(item.getBodyFeelingTypeId());
            return convertView;
        }

        public final int getCount() {
            return mItems.size();
        }

        public final Object getItem(int position) {
            return mItems.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

}
