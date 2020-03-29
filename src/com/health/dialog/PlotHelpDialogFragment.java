package com.health.dialog;

import com.health.main.R;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlotHelpDialogFragment extends DialogFragment {
    public static final String EXTRA_PAGE = "EXTRA_PAGE";

	int mLayoutId;
    int mPageIndex = -1;

    public ViewPager mViewPager = null;
    public Button mBtnPrev, mBtnNext = null;
	
	public void setInitValue(int layoutId) {
		this.mLayoutId = layoutId;
	}
	
	public static PlotHelpDialogFragment newInstance(int layoutId) {
		PlotHelpDialogFragment f = new PlotHelpDialogFragment();
		f.setInitValue(layoutId);
        return f;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mLayoutId, container, false);
        getDialog().setTitle("Справка");
        Button btnOK = (Button) v.findViewById(R.id.btnOK);
        this.mViewPager = (ViewPager) v.findViewById(R.id.pager);
        this.mBtnNext = (Button) v.findViewById(R.id.btnNext);
        this.mBtnPrev = (Button) v.findViewById(R.id.btnPrev);
        if(this.mViewPager != null){
            String countStr = (String) this.mViewPager.getTag();
            WizardPagerAdapter adapter = new WizardPagerAdapter(Integer.parseInt(countStr));
            this.mViewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if(getArguments() != null){
                mPageIndex = getArguments().getInt(EXTRA_PAGE, 0);
                this.mViewPager.setCurrentItem(mPageIndex);
            }
            this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position > 0) {
                        mBtnPrev.setEnabled(true);
                    } else {
                        mBtnPrev.setEnabled(false);
                    }
                    if (position < (mViewPager.getAdapter().getCount() - 1)) {
                        mBtnNext.setEnabled(true);
                    } else {
                        mBtnNext.setEnabled(false);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        if(mBtnNext != null){
            mBtnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item = mViewPager.getCurrentItem() + 1;
                    mViewPager.setCurrentItem(item, true);
                    if(item < (mViewPager.getAdapter().getCount() - 1)){
                        v.setEnabled(true);
                    } else {
                        v.setEnabled(false);
                    }
                }
            });
        }
        if(mBtnPrev != null){
            mBtnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item = mViewPager.getCurrentItem() - 1;
                    mViewPager.setCurrentItem(item, true);
                    if(item > 0){
                        v.setEnabled(true);
                    } else {
                        v.setEnabled(false);
                    }
                }
            });
        }

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

    class WizardPagerAdapter extends PagerAdapter {

        private int mPageSize;

        public WizardPagerAdapter(int pageSize){
            super();
            this.mPageSize = pageSize;
        }

        @Override
        public Object instantiateItem (ViewGroup container, int position){
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.page_1;
                    break;
                case 1:
                    resId = R.id.page_2;
                    break;
                case 2:
                    resId = R.id.page_3;
                    break;
                case 3:
                    resId = R.id.page_4;
                    break;
                case 4:
                    resId = R.id.page_5;
                    break;
                case 5:
                    resId = R.id.page_6;
                    break;
                case 6:
                    resId = R.id.page_7;
                    break;
                case 7:
                    resId = R.id.page_8;
                    break;
                case 8:
                    resId = R.id.page_9;
                    break;
                case 9:
                    resId = R.id.page_10;
                    break;
                case 10:
                    resId = R.id.page_11;
                    break;
                case 11:
                    resId = R.id.page_12;
                    break;
                case 12:
                    resId = R.id.page_13;
                    break;
                case 13:
                    resId = R.id.page_14;
                    break;
                case 14:
                    resId = R.id.page_15;
                    break;
                case 15:
                    resId = R.id.page_16;
                    break;
                case 16:
                    resId = R.id.page_17;
                    break;
                case 17:
                    resId = R.id.page_18;
                    break;
                case 18:
                    resId = R.id.page_19;
                    break;
                case 19:
                    resId = R.id.page_20;
                    break;
                case 20:
                    resId = R.id.page_21;
                    break;
                case 21:
                    resId = R.id.page_22;
                    break;
                case 22:
                    resId = R.id.page_23;
                    break;
                case 23:
                    resId = R.id.page_24;
                    break;
                case 24:
                    resId = R.id.page_25;
                    break;
                case 25:
                    resId = R.id.page_26;
                    break;
                case 26:
                    resId = R.id.page_27;
                    break;
                case 27:
                    resId = R.id.page_28;
                    break;
                case 28:
                    resId = R.id.page_29;
                    break;
                case 29:
                    resId = R.id.page_30;
                    break;
            }
            View resultView = null;
            resultView = getView().findViewById(resId);
            if(resultView.getParent() != null){
                ((ViewGroup)resultView.getParent()).removeView(resultView);
            }
            mViewPager.addView(resultView);
            return  resultView;
        }

        @Override
        public int getCount() {
            return mPageSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }
	
}
