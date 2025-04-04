package com.codringreen.farmloading.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import androidx.annotation.NonNull;

public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;

    public CustomTypefaceSpan(String family, Typeface type) {
        super(family);
        this.newType = type;
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.CharacterStyle
    public void updateDrawState(@NonNull TextPaint ds) {
        applyCustomTypeFace(ds, this.newType);
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.MetricAffectingSpan
    public void updateMeasureState(@NonNull TextPaint paint) {
        applyCustomTypeFace(paint, this.newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        Typeface typeface = paint.getTypeface();
        int style = (typeface == null ? 0 : typeface.getStyle()) & (~tf.getStyle());
        if ((style & 1) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((style & 2) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(tf);
    }
}