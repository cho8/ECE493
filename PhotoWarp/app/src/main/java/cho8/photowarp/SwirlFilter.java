package cho8.photowarp;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by cho on 2017-02-06.
 */

public class SwirlFilter extends AbstractFilter {

    public SwirlFilter(Context c, Bitmap image) {
        super(c, image);
    }

    @Override
    public void applyFilter() {
        script.invoke_swirl(0.005f);
    }
}
