package cho8.noisereduction;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by cho on 2017-01-21.
 */

public class MeanFilter extends AbstractFilter {


    public MeanFilter(Bitmap image, int size) {
        super(image, size);
    }

    @Override
    protected int calcFilterMask(int[] pixels) {

        int len = pixels.length;
        // Channel sums
        int R=0;
        int G=0;
        int B=0;
        int A=0;

        for (int p : pixels) {
            R += Color.red(p);
            G += Color.green(p);
            B += Color.blue(p);
            A += Color.alpha(p);
        }

        return Color.argb(A/len, R/len, G/len, B/len);
    }

}
