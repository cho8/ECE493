package cho8.photowarp.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cho8.photowarp.filter.FilterListener;

/**
 * Created by cho on 2017-02-09.
 */

public class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    final private String DEBUG_TAG = "GestureDebug";

    float xi, xf, yi, yf;

    GestureDetector detector = new GestureDetector(this);
    FilterListener listener;
    Context c;

    public DoubleTapGestureListener(FilterListener listener) {

        this.listener = listener;
        detector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        listener.executeFishEyeFilter();
        return true;
    }

}
