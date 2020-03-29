package com.health.plot;

import java.util.Date;

public interface IFactorPlot extends IPlot {
	void updateUI(Date dtFrom, Date dtTo, long domainStep, int ticksPerDomainLabel);
}
