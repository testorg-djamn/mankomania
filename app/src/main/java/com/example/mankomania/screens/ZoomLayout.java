package com.example.mankomania.screens;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class ZoomLayout extends LinearLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    private float startX = 0f;
    private float startY = 0f;

    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener((v, event) -> {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (scale > MIN_ZOOM) {
                        mode = Mode.DRAG;
                        startX = event.getX() - prevDx;
                        startY = event.getY() - prevDy;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == Mode.DRAG) {
                        dx = event.getX() - startX;
                        dy = event.getY() - startY;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = Mode.ZOOM;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = Mode.DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                    mode = Mode.NONE;
                    prevDx = dx;
                    prevDy = dy;
                    break;
            }
            scaleDetector.onTouchEvent(event);

            if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                getParent().requestDisallowInterceptTouchEvent(true);
                float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
                float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
                dx = Math.min(Math.max(dx, -maxDx), maxDx);
                dy = Math.min(Math.max(dy, -maxDy), maxDy);
                applyScaleAndTranslation();
            }

            return true;
        });
    }

    @Override
    public boolean onScaleBegin(@NonNull ScaleGestureDetector scaleDetector) {
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public void onScaleEnd(@NonNull ScaleGestureDetector scaleDetector) {
    }

    private void applyScaleAndTranslation() {
        child().setScaleX(scale);
        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().setTranslationY(dy);
    }

    private View child() {
        return getChildAt(0);
    }
}
