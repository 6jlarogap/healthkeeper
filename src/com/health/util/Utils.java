package com.health.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    
    public static Calendar toRoundDate(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        if (c.get(Calendar.MINUTE) >= 30) {
            c.add(Calendar.HOUR, 1);
        }
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c;
    }
    
    public static Calendar toMaxRoundDate(Date d) {        
        return toRoundDateAndIncrementDay(d, 1);
    }
    
    public static Calendar toRoundDateAndIncrementDay(Date d, int incrementDay) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        c.add(Calendar.DAY_OF_YEAR, incrementDay);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }
    
    public static int getDaysBetween(Date d1, Date d2){
        return (int)( Math.abs((d2.getTime() - d1.getTime())) / (1000 * 60 * 60 * 24));
    }
    
    public static Date getMaxDateOfTheDay(Date d) {
    	GregorianCalendar gcTo = new GregorianCalendar();
		gcTo.setTime(d);
		gcTo.set(GregorianCalendar.HOUR_OF_DAY, 23);
		gcTo.set(GregorianCalendar.MINUTE, 59);
		gcTo.set(GregorianCalendar.SECOND, 59);
		return gcTo.getTime();
    }

}
