package cho8.photowarp.filter;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by cho on 2017-02-10.
 */

public class FishEyeFilter extends AbstractFilter {
    public FishEyeFilter(Context c, Bitmap image) {
        super(c, image);
    }

    @Override
    public void applyFilter() {
        script.invoke_fishEye();
    }
}
