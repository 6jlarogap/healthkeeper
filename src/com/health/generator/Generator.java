package com.health.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import android.content.Context;

import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
import com.health.main.R;

public class Generator {    

    public static ArrayList<BodyFeeling> generateBodyFeelings(Context context) {
        ArrayList<BodyFeeling> bodyFeelingList = new ArrayList<BodyFeeling>();
        bodyFeelingList.addAll(getBodyFeelings(11, 4, "Головная боль,затылок", 5, context.getResources().getColor(R.color.color1)));
        bodyFeelingList.addAll(getBodyFeelings(22, 10, "Слабость", 3, context.getResources().getColor(R.color.color2)));
        bodyFeelingList.addAll(getBodyFeelings(33, 15, "Высокое давление", 1, context.getResources().getColor(R.color.color3)));
        bodyFeelingList.addAll(getBodyFeelings(4, 11, "Сердцебиение", 1, context.getResources().getColor(R.color.color4)));
        bodyFeelingList.addAll(getBodyFeelings(5, 19, "Спина, радикулит", 1, context.getResources().getColor(R.color.color6)));
        bodyFeelingList.addAll(getBodyFeelings(6, 5, "Рука, локоть болит", 1, context.getResources().getColor(R.color.color5)));
        bodyFeelingList.addAll(getBodyFeelings(7, 19, "Другое", 1, context.getResources().getColor(R.color.colorOther)));
        int index = 0;
        for(BodyFeeling bf : bodyFeelingList){
            bf.setId(new Long(index++));
        }
        return bodyFeelingList;
    }
    
    private static ArrayList<BodyFeeling> getBodyFeelings(int id, int numberOfBodyFeelings, String organ, int rate, int color) {
        ArrayList<BodyFeeling> bodyFeelingList = new ArrayList<BodyFeeling>();
        int maxMonth = 11;
        int maxDay = 10;
        int maxHour = 23;
        int randomMonth;
        int randomDay;
        int randomHour;
        Random random = new Random();
        GregorianCalendar dateNow = new GregorianCalendar();        
        for (int i = 0; i < numberOfBodyFeelings + 1; i++) {
            randomMonth = random.nextInt(maxMonth);
            randomDay = random.nextInt(maxDay);
            randomHour = random.nextInt(maxHour);
            GregorianCalendar date = new GregorianCalendar();
            date.set(Calendar.YEAR, 2013);
            int dayOfYear = randBetween(1, date.getActualMaximum(Calendar.DAY_OF_YEAR));
            date.set(Calendar.DAY_OF_YEAR, dayOfYear);
            BodyFeeling bodyFeeling = new BodyFeeling();
            bodyFeeling.setStartDate(date.getTime());
            bodyFeeling.setBodyFeelingType(new BodyFeelingType());
            bodyFeeling.getBodyFeelingType().setId(new Long(id));
            bodyFeeling.getBodyFeelingType().setName(organ);
            bodyFeelingList.add(bodyFeeling);
        }        
        return bodyFeelingList;
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public static int getRandomColor() {
        int color;
        Random random = new Random();
        int color1 = random.nextInt();
        int color2 = random.nextInt(88);
        color = color1 + color2;
        return color;
    }

}
