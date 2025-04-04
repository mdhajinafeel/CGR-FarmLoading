package com.codringreen.farmloading.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {
    private static final int DEFAULT_BORDER_WIDTH = 4;
    private final int borderWidth = DEFAULT_BORDER_WIDTH;
    private Bitmap image;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setBorderColor(Color.WHITE);
        setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(borderWidth);
        paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
    }

    public void setBorderColor(int borderColor) {
        paintBorder.setColor(borderColor);
        invalidate();
    }

    private void loadBitmap() {
        if (getDrawable() instanceof BitmapDrawable) {
            image = ((BitmapDrawable) getDrawable()).getBitmap();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) return;

        loadBitmap();
        if (image != null) {
            int width = getWidth();
            int height = getHeight();
            int radius = Math.min(width, height) / 2;

            BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(image, width, height, false),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            canvas.drawCircle(width / 2f, height / 2f, radius - borderWidth / 2f, paint);
            canvas.drawCircle(width / 2f, height / 2f, radius - borderWidth / 2f, paintBorder);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }
}
