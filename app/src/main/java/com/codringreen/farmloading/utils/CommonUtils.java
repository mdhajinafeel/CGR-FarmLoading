package com.codringreen.farmloading.utils;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtils {

    public static SpannableString customFontTypeFace(Typeface typeface, CharSequence chars) {
        if (chars == null) {
            return null;
        }
        SpannableString spannableString = new SpannableString(chars);
        spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, spannableString.length(), 33);
        return spannableString;
    }

    public static String convertTimeStampToDate(Long milliSeconds, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, new Locale(Locale.getDefault().getLanguage()));
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(milliSeconds);
        } catch (Exception e) {
            Log.e("CommonUtils", "Error in CommonUtils convertTimeStampToDate", e);
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Long getCurrentLocalDateTimeStamp() {
        return Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
    }

    public static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0.0d) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, RoundingMode.FLOOR);
        }
        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, RoundingMode.CEILING);
    }

    public static BigDecimal roundValue(double value, int numberofDecimals) {
        return BigDecimal.valueOf(value).setScale(numberofDecimals, RoundingMode.HALF_UP);
    }

    public static double calculatePieceFormula(double circumference, double length) {
        double pi = Math.PI;
        double truncValue = Math.floor(circumference / pi);
        double powerValue = Math.pow(truncValue - 5, 2);
        double multiplication = powerValue * 0.7854 * (length - 5) / 1000000;
        return Math.round(multiplication * 1000.0) / 1000.0;
    }

    public static String convertDateTimeFormatWithDefaultLocale(String input, String fromFormat, String toFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat,
                    Locale.getDefault());
            Date dateObj = sdf.parse(input);
            if(dateObj != null) {
                return new SimpleDateFormat(toFormat, Locale.getDefault()).format(dateObj);
            }
        } catch (Exception e) {
            Log.e("CommonUtils", "Error in CommonUtils convertDateTimeFormatWithDefaultLocale", e);
        }
        return input;
    }

    public static String getDateByType(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        if ("fifth".equalsIgnoreCase(type)) {
            calendar.add(Calendar.DAY_OF_YEAR, -4);  // 5th date including today
        }

        return sdf.format(calendar.getTime());
    }
}