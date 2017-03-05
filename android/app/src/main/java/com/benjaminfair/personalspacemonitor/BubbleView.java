package com.benjaminfair.personalspacemonitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class BubbleView extends View {
    private static final int maxDistance = 500;
    private static final int personH = 400, personW = 400;

    private Data.Measurement mDims;

    private int x, y; // center point
    private int wh;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void configurePaint() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        paint.setTextSize(160f);
    }

    public BubbleView(Context context) {
        super(context);

        configurePaint();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO: handle style
        configurePaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int nw = w - getPaddingLeft() - getPaddingRight();
        int nh = h - getPaddingTop() - getPaddingBottom();

        this.wh = Math.min(nw, nh);

        this.x = getPaddingLeft() + nw / 2;
        this.y = getPaddingTop() + nh / 2;

    }

    final String no_data = "No Data";
    float[] widths = new float[no_data.length()];

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDims != null) {
            Drawable d = getResources().getDrawable(R.drawable.person);
            d.setBounds(x-personW/2, y-personH/2, x+personW/2, y+personH/2);

            d.draw(canvas);

            float left, top, right, bottom;
            left = (float) (x - Math.log10(mDims.left) / Math.log10(maxDistance) * wh / 2.0f);
            right = (float) (x + Math.log10(mDims.right) / Math.log10(maxDistance) * wh / 2.0f);
            top = (float) (y - Math.log10(mDims.front) / Math.log10(maxDistance) * wh / 2.0f);
            bottom = (float) (y + Math.log10(mDims.back) / Math.log10(maxDistance) * wh / 2.0f);

            canvas.drawOval(left, top, right, bottom, paint);
        } else {
            paint.getTextWidths(no_data, widths);
            float total_width = 0;
            for (float width : widths) {
                total_width += width;
            }

            canvas.drawText(no_data, x - total_width / 2.0f, (float) y, paint);
        }
    }

    public void setDims(Data.Measurement dims) {
        mDims = dims;
        invalidate();
    }
}
