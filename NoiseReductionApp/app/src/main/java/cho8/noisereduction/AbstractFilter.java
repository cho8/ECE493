package cho8.noisereduction;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import static android.R.attr.bitmap;

/**
 * Created by cho on 2017-01-22.
 */
public abstract class AbstractFilter extends Observable {

    protected Bitmap imageBm;
    protected int maskSize;


    public AbstractFilter(Bitmap image, int size) {
        imageBm = image;
        maskSize = size;

    }

    protected abstract int calcFilterMask(int[] pixels);

    public Bitmap applyFilter() {


        Bitmap newBm = imageBm.copy(Bitmap.Config.ARGB_8888, true);

        int x0, y0, x1, y1; // top left and bottom right coordinates
        int[] pixels;

        // iterate through bitmap with given window size
        for (int i=0; i < imageBm.getWidth(); i++) {
            x0 = ensureRange((i - (maskSize-1)/2), 0, imageBm.getWidth()-1);

            x1 = ensureRange((i + (maskSize-1)/2), 0, imageBm.getWidth()-1);

            // progress update
            notifyObservers((i*100)/imageBm.getWidth());
            this.setChanged();


            for (int j=0; j < imageBm.getHeight(); j++) {

                y0 = ensureRange((j - (maskSize-1)/2), 0 ,imageBm.getHeight()-1);
                y1 = ensureRange((j + (maskSize-1)/2), 0, imageBm.getHeight()-1);

                pixels = new int[(x1-x0)*(y1-y0)];

                if (this.maskSize <= 1 || pixels.length == 0) { return this.imageBm;}

                imageBm.getPixels(pixels,0, x1-x0, x0, y0, x1-x0, y1-y0);

                newBm.setPixel(i,j,calcFilterMask(pixels));
            }
        }

        return newBm;
    }

    // quick function to ensure value is within bounds
    // http://stackoverflow.com/questions/17933493/java-limit-number-between-min-and-max
    int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}


