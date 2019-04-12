package com.example.iigar.filemanager;

import android.view.ScaleGestureDetector;

public class ListScale extends android.view.ScaleGestureDetector.SimpleOnScaleGestureListener {
    private MainActivity activity = null;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        if (scaleGestureDetector.getScaleFactor() * 100 > 100) {
            activity.openDir();
        } else {
            activity.closeDir();
        }
    }
}
