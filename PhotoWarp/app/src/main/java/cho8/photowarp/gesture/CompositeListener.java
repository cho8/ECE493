package cho8.photowarp.gesture;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cho on 2017-02-10.
 *
 * Idea for composite listener adapted from
 * http://stackoverflow.com/questions/5465204/how-can-i-set-up-multiple-listeners-for-one-event
 */

public class CompositeListener implements View.OnTouchListener {
    private List<View.OnTouchListener> listeners = new ArrayList<>();

    public void addGestureListener (View.OnTouchListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for (View.OnTouchListener listener :listeners ) {
            listener.onTouch(v, event);
        }
        return true;
    }
}
