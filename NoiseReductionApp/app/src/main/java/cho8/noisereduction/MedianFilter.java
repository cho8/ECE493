package cho8.noisereduction;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by cho on 2017-01-21.
 */

public class MedianFilter extends AbstractFilter{

    public MedianFilter(Bitmap image, int size) {
        super(image, size);
    }
    @Override
    protected int calcFilterMask(int[] pixels) {
        int[] R = new int[pixels.length];
        int[] G = new int[pixels.length];
        int[] B = new int[pixels.length];
        int[] A = new int[pixels.length];


        for (int i=0; i<pixels.length; i++) {
            R[i] = Color.red(pixels[i]);
            G[i] = Color.green(pixels[i]);
            B[i] = Color.blue(pixels[i]);
            A[i] = Color.alpha(pixels[i]);
        }

        Arrays.sort(R);
        Arrays.sort(G);
        Arrays.sort(B);
        Arrays.sort(A);


        return Color.argb(A[pixels.length/2], R[pixels.length/2], G[pixels.length/2], B[pixels.length/2]);
    }
}
