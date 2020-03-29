package com.health.plot;

import android.content.Context;

public abstract class Plot implements IPlot {
	protected Context mContext;
	protected XYPlotZoomPan plot;
	
	Plot(Context context){
		mContext = context;
		plot = new XYPlotZoomPan(mContext, "plot");
	}

	@Override
    public XYPlotZoomPan getPlot() {
	    return plot;
    }
}
