package com.health.main;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.health.dependency.DependencySet;

import junit.framework.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void createDemonstrateBodyFeelings(){
        DependencySet dependencySet = new DependencySet();
        Calendar calendar = GregorianCalendar.getInstance();
        Date dtFrom, dtTo;
        dtTo = calendar.getTime();
        calendar.add(Calendar.MONTH, -3);
        dtFrom = calendar.getTime();
        dependencySet.generateDiaryFeeling(1, dtFrom, dtTo);
    }
}