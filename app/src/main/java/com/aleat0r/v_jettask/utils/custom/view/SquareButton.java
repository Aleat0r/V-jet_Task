package com.aleat0r.v_jettask.utils.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class SquareButton extends Button {

    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int size = Math.min(height, width);
        setMeasuredDimension(size, size);
    }
}
