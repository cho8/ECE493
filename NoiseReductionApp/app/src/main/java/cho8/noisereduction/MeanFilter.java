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

        int[] sum = {0,0,0,0} ;   // R,G,B,A


        for (int p : pixels) {
            sum[0] += Color.red(p);
            sum[1] += Color.green(p);
            sum[2] += Color.blue(p);
            sum[3] += Color.alpha(p);
        }
        for (int i=0; i<sum.length ; i++) {
            sum[i] = sum[i]/pixels.length;
        }


        return Color.argb(sum[3], sum[0], sum[1], sum[2]);
    }

}
