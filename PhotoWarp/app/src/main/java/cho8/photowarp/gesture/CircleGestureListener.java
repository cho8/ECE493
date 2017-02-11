package cho8.photowarp.gesture;

import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import cho8.photowarp.filter.FilterListener;

/**
 * Created by cho on 2017-02-10.
 */

public class CircleGestureListener implements View.OnTouchListener  {

    final private String DEBUG_TAG = "GestureDebug";
    final private float THRESHOLD = 200;

    private boolean top,right,bottom,left;

    private final FilterListener listener;


    private VelocityTracker mVelocityTracker = null;


    public CircleGestureListener( FilterListener f) {
        listener = f;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if(mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);

                mVelocityTracker.computeCurrentVelocity(1000);

                float xVel = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
                float yVel = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);

                flagEdgeDrawn(xVel,yVel);

                break;

            case MotionEvent.ACTION_UP:

                if(top && right && bottom && left) {
                    listener.executeSwirlFilter();
                }
                resetEdges();

                break;

            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                resetEdges();
                break;
        }
        return true;
    }

    /** Helper function to determine if xy-velocities from motion match an edge.
        @args:  direction - velocity in direction of the edge
                constant - velocity perpendicular to edge
     */
    public boolean withinThreshold(float direction, float constant) {
        if (Math.abs(direction) > THRESHOLD && Math.abs(constant) < THRESHOLD) {
            return true;
        }
        return false;
    }

    /* Flags the edge that was drawn based on the velocity of the motionevent */

    public void flagEdgeDrawn(float xVel, float yVel) {

        // handle detecting rectangle edges
        if ( xVel > 0 && withinThreshold(xVel, yVel) && !top && !bottom && !right && !left) {
            top = true;
        } else if (yVel < 0 && withinThreshold(yVel, xVel)&& top) {
            right = true;
        } else if (xVel < 0 && withinThreshold(xVel, yVel) && top && right) {
            bottom = true;
        } else if (yVel >0 && withinThreshold(yVel,xVel) && top && right && bottom) {
            left = true;
            Log.d(DEBUG_TAG, "Rectangle drawn");
        }

    }

    public void resetEdges() {
        top = right = bottom = left = false;
    }
}