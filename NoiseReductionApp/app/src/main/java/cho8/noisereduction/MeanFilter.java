package cho8.noisereduction;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by cho on 2017-01-21.
 */

public class MeanFilter extends AbstractFilter {


    public MeanFilter(Bitmap image, int size) {
        super(image, size);
    }

    @Override
    protected int[] applyFilterWindow(int[] pixels) {

        int sum =0;
        int[] newPixels = new int[pixels.length];
        Arrays.fill(newPixels, 0);

//        for (int i : pixels) {
//            sum += i;
//        }
//
//        for (int i=0; i<pixels.length; i++) {
//            newPixels[i] = pixels[i]/sum;
//        }

        return newPixels;
    }

}
