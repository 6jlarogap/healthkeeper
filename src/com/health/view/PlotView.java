package com.health.view;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.health.plot.PlotService;
import com.health.plot.ScalePlot;
import com.health.repository.IRepository;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.health.repository.IRepository.DATA_AMOUNT_TYPE;

public class PlotView extends LinearLayout implements View.OnTouchListener {

	private final long MS_IN_DAY = 24 * 60 * 60 * 1000;

	protected final long MAX_MS_PERIOD_BY_ZOOM = 20 * MS_IN_DAY;

	private final double DB_ZOOM = 1.4;

	private final int MAX_DAY_WITH_HOURLY_DATA = 20;
	
	protected long domainStep = 3L * PlotService.HOUR;
	protected int ticksPerDomainLabel = 8;

	protected ExecutorService mExecutorService = Executors.newCachedThreadPool();

	protected DATA_AMOUNT_TYPE mDataAmountType = null;
	protected Date mMarkedDate = new Date();
	protected Date mPlotDateTo = new Date();
	protected Date mPlotDateFrom = new Date(mPlotDateTo.getTime() - 2 * 24 * 60 * 60 * 1000);
	protected Date mDbDateFrom = new Date();
	protected Date mDbDateTo = new Date();

	private LinearLayout llZoom;

	protected ScalePlot scalePlot;

	public interface OnChangeDateListener {
		void onChangeRangeDate(Date dtFrom, Date dtTo);
	}

	protected OnChangeDateListener mOnChangeDateListener;

	public void setOnChangeDateListener(OnChangeDateListener listener) {
		this.mOnChangeDateListener = listener;
	}
	
	protected void onChangeRangeDate(Date dtFrom, Date dtTo) {
		if (mOnChangeDateListener != null) {
			mOnChangeDateListener.onChangeRangeDate(dtFrom, dtTo);
		}
	}

	public void initZoom(LinearLayout view) {
		this.llZoom = view;
		llZoom.refreshDrawableState();
		llZoom.bringToFront();
		llZoom.setOnTouchListener(this);
	}

	public PlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scalePlot = new ScalePlot((Activity) getContext(), mPlotDateFrom, mPlotDateTo, mMarkedDate);
	}

	public PlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PlotView(Context context) {
		super(context);
	}

	public IRepository.DATA_AMOUNT_TYPE getAmountDataType(Date dtFrom, Date dtTo){
		int days = (int) ((dtTo.getTime() - dtFrom.getTime()) / (86400 * 1000));
		if(days < 4){
			return DATA_AMOUNT_TYPE.ALL;
		} else {
			if(days < 8){
				return DATA_AMOUNT_TYPE.HOURLY_3H;
			} else {
				if(days < 16){
					return  DATA_AMOUNT_TYPE.HOURLY_6H;
				} else {
					if(days < 40){
						return DATA_AMOUNT_TYPE.DAILY;
					} else {
						if(days < 80){
							return  DATA_AMOUNT_TYPE.DAILY_2D;
						} else {
							if(days < 160){
								return  DATA_AMOUNT_TYPE.DAILY_4D;
							} else {
								return DATA_AMOUNT_TYPE.DAILY_6D;
							}
						}
					}
				}
			}
		}
	}

	protected void updateDBDates() {
		Date newDbDateFrom = null;
		Date newDbDateTo = null;
		long days = (mPlotDateTo.getTime() - mPlotDateFrom.getTime()) / MS_IN_DAY;
		if (days > MAX_DAY_WITH_HOURLY_DATA) {
			long deltaDay = (long) (days * DB_ZOOM - days) / 2;
			newDbDateFrom = new Date(mPlotDateFrom.getTime() - deltaDay * MS_IN_DAY);
			newDbDateTo = new Date(mPlotDateTo.getTime() + deltaDay * MS_IN_DAY);
		} else {
			//long deltaDay = ((MAX_DAY_WITH_HOURLY_DATA - days) / 2) + 1;
			long deltaDay = 1;
			newDbDateFrom = new Date(mPlotDateFrom.getTime() - deltaDay * MS_IN_DAY);
			newDbDateTo = new Date(mPlotDateTo.getTime() + deltaDay * MS_IN_DAY);
		}
		Log.i("west_updateDBDates_" + getClass().getName(), String.format("%s-%s", newDbDateFrom.toGMTString(), newDbDateTo.toGMTString()));
		this.mDbDateFrom = newDbDateFrom;
		this.mDbDateTo = newDbDateTo;
	}

	protected boolean isNeedChangeDBDates() {
		boolean result = false;
		if (mPlotDateFrom.before(mDbDateFrom) || mPlotDateFrom.after(mDbDateTo) || mPlotDateTo.before(mDbDateFrom) || mPlotDateTo.after(mDbDateTo)) {
			result = true;
		} else {
			DATA_AMOUNT_TYPE dataAmountType = getAmountDataType(mPlotDateFrom, mPlotDateTo);
			if(dataAmountType.ordinal() < mDataAmountType.ordinal()){
				result = true;
			}
		}
		return result;
	}	
	

	static final int NONE = 0;
	static final int ONE_FINGER_DRAG = 1;
	static final int TWO_FINGERS_DRAG = 2;
	int mode = NONE;

	PointF firstFinger;
	float distBetweenFingers;
	boolean stopThread = false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: // Start gesture
			firstFinger = new PointF(event.getX(), event.getY());
			mode = ONE_FINGER_DRAG;
			stopThread = true;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // second finger
			distBetweenFingers = spacing(event);
			// the distance check is done to avoid false alarms
			if (distBetweenFingers > 5f) {
				mode = TWO_FINGERS_DRAG;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == ONE_FINGER_DRAG) {
				PointF oldFirstFinger = firstFinger;
				firstFinger = new PointF(event.getX(), event.getY());
				scroll(oldFirstFinger.x - firstFinger.x);
			} else if (mode == TWO_FINGERS_DRAG) {
				float oldDist = distBetweenFingers;
				distBetweenFingers = spacing(event);
				float scale = oldDist / distBetweenFingers;
				if ((mPlotDateTo.getTime() - mPlotDateFrom.getTime()) < MAX_MS_PERIOD_BY_ZOOM) {
					zoom(scale);
				} else {
					if (scale < 1) {
						zoom(scale);
					}
				}
			}
			break;
		}
		return true;
	}

	private void zoom(float scale) {
		long domainSpan = mPlotDateTo.getTime() - mPlotDateFrom.getTime();
		float domainMidPoint = mPlotDateTo.getTime() - domainSpan / 2.0f;
		float offset = domainSpan * scale / 2.0f;
		long time1 = (long) (domainMidPoint - offset);
		long time2 = (long) (domainMidPoint + offset);
		mPlotDateFrom.setTime(time1);
		mPlotDateTo.setTime(time2);
		setDateRange(mPlotDateFrom, mPlotDateTo, true);
	}

	private void scroll(float pan) {
		if(scalePlot.getPlot().getWidth() != 0){
    		long domainSpan = mPlotDateTo.getTime() - mPlotDateFrom.getTime();
    		float step = domainSpan / scalePlot.getPlot().getWidth();
    		long offset = (long) (pan * step);
    		long time1 = mPlotDateFrom.getTime() + offset;
    		long time2 = mPlotDateTo.getTime() + offset;
    		mPlotDateFrom.setTime(time1);
    		mPlotDateTo.setTime(time2);
    		setDateRange(mPlotDateFrom, mPlotDateTo, true);
		}
	}

	public void setDateRange(Date dtFrom, Date dtTo, boolean isRaiseDateRangeEvent) {
		this.mPlotDateFrom = dtFrom;
		this.mPlotDateTo = dtTo;
		updateDomainStep();
		scalePlot.updateUI(mPlotDateFrom, mPlotDateTo, domainStep, ticksPerDomainLabel);
		if(isRaiseDateRangeEvent){
			onChangeRangeDate(mPlotDateFrom, mPlotDateTo);
		}
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	}

	protected void updateDomainStep(){
		/*
		 * interval - количество милисекунд на графике
		 * тик - вертикальная линия на графике
		 * domainStep - интервал между двумя тиками (далее ИТ)
		 * лэйбл - подпись времени на графике или жирная линия на графике (подпись времени есть только по жирной линией)
		 * bigStep - интервал между двумя лэйблами (далее БШ (Большой Шаг))
		 * ticksPerDomainLabel - количество тиков между лэйблами (далее КТЛ)
		 * bspi - bigStepPerInterval - количество БШ на интервал (далее БШ/И)
		 */

		final int MAX_BSPI = 7;//Максимальное БШ/И
		final int MIN_BSPI = 3;//Минимальное БШ/И
		final long interval = mPlotDateTo.getTime() - mPlotDateFrom.getTime();//количество милисекунд на графике
		long bigStep = domainStep * ticksPerDomainLabel;				//размер БШ
		long lastBigStep = 0;											//последний размер БШ
		int bspi = 0;													

		while(lastBigStep != bigStep){	//пока БШ не не изменится
			lastBigStep = bigStep;		//запоминаем БШ
			bspi = (int)(interval / bigStep);	//расчитываем БШ/И
    		if(interval <= PlotService.DAY){//если интервал меньше дня
    			bigStep = 3L * PlotService.HOUR;//то БШ равен 3 часа (т.е. минимальный БШ - это три часа)
    		} else if(bspi > MAX_BSPI){//если БШ/И больше максимального
    			bigStep = bigStep * 2 < PlotService.DAY ? PlotService.DAY : bigStep * 2;//БШ либо удваивается, либо становиться равным дню
    		} else if(bspi < MIN_BSPI){//если БШ/И меньше минимального
    			bigStep = bigStep / 2 < PlotService.DAY ? PlotService.DAY : bigStep / 2;//БШ либо урезается вдвое, либо становиться равным дню
    		}
		}

		bspi = (int)(interval / bigStep);	//расчитываем БШ/И
		
		if (bigStep > PlotService.DAY){//если БШ больше дня
    			if(interval / domainStep > 20){
    				while(interval / domainStep > 20){
    					domainStep *= 2;
    				}
    			} else if(interval / domainStep < 10){
    				while(interval / domainStep < 10){
    					domainStep /= 2;
    				}
    			}
			ticksPerDomainLabel = (int)(bigStep / domainStep);
		} else if (bigStep == PlotService.DAY) {//если БШ равен дню
			switch(bspi){
			case 0:
				domainStep = PlotService.HOUR;
	    		break;
			case 1:
				domainStep = 3 * PlotService.HOUR;
	    		break;
			case 2:
				domainStep = 3 * PlotService.HOUR;
	    		break;
			case 3:
				domainStep = 6 * PlotService.HOUR;
	    		break;
			case 4:
				domainStep = 6 * PlotService.HOUR;
	    		break;
			case 5:
				domainStep = 6 * PlotService.HOUR;
	    		break;
			case 6:
				domainStep = 12 * PlotService.HOUR;
	    		break;
			}
    		ticksPerDomainLabel = (int)(PlotService.DAY / domainStep);
		} else if (bigStep < PlotService.DAY){//если БШ меньше дня
    		domainStep = PlotService.HOUR;//то ИТ равен часу
    		ticksPerDomainLabel = 3;//а КТЛ равно трем (т.е. тик будет на каждый час, а лэйбл на каждые три часа)
		}
	}
}
