package com.health.plot;

import com.androidplot.ui.SeriesRenderer;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.XYPlot;
import com.health.plot.MyBarRenderer;

class MyBarFormatter extends BarFormatter {
    public MyBarFormatter(int fillColor, int borderColor) {
        super(fillColor, borderColor);
    }

    @Override
    public Class<? extends SeriesRenderer> getRendererClass() {
        return MyBarRenderer.class;
    }

    @Override
    public SeriesRenderer getRendererInstance(XYPlot plot) {
        return new MyBarRenderer(plot);
    }
}
