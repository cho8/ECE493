package cho8.photowarp;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by cho on 2017-02-09.
 */

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    final private String DEBUG_TAG = "GestureDebug";

    private ArrayList<Pair> touchList;
    float xi, xf, yi, yf;

    GestureDetector detector = new GestureDetector(this);
    FilterListener listener;
    Context c;

    public MyGestureListener(FilterListener listener) {

        this.listener = listener;
        detector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);

        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            touchList = new ArrayList<>();

            xi = event.getX();
            yi = event.getY();
            Pair p = new Pair(xi, yi);

            Log.d(DEBUG_TAG, "DOWN");
        } else if (action == MotionEvent.ACTION_UP)
        {
            xf = event.getX();
            yf = event.getY();


            performAction(v.getWidth(), v.getHeight());
            Log.d(DEBUG_TAG, "UP");
        } else if (action == MotionEvent.ACTION_SCROLL) {

        }

        return true;
    }


    private void performAction(int width, int height) {
        float dx = Math.abs(xf - xi);
        float dy = Math.abs(yf - yi);

        if (dy > height/3) {
            Log.d(DEBUG_TAG, "swirl");
            listener.executeSwirlFilter();
        } else if (dx > width / 3) {
            listener.executeFishEyeFilter();
        }

    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(DEBUG_TAG, "doubletap!");
        listener.executeBulgeFilter();
        return true;
    }

}
