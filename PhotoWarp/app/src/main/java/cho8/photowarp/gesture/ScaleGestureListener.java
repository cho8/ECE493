package cho8.photowarp.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewStub;

import cho8.photowarp.filter.FilterListener;

/**
 * Created by cho on 2017-02-10.
 */

public class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener implements ViewStub.OnTouchListener {
    final private String DEBUG_TAG = "GestureDebug";

    FilterListener listener;
    ScaleGestureDetector mDetector;

    float startSpan, endSpan;


    public ScaleGestureListener(Context c, FilterListener l) {
        listener = l;
        mDetector = new ScaleGestureDetector(c, this);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        startSpan = detector.getCurrentSpan();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        endSpan = detector.getCurrentSpan();

        if (startSpan < endSpan) {
            listener.executeBulgeFilter();
            return;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }
}