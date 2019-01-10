package com.app.salesapp.timeline.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomRoundedImageView extends ImageView {

    public static float radius = 6;

    private Path clipPath;
    private RectF rect;

    public CustomRoundedImageView(Context context) {
        super(context);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (clipPath == null) {
            clipPath = new Path();
        }

        if (rect == null) {
            rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        }

        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
