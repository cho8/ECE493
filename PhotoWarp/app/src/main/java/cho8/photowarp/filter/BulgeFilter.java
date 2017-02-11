package cho8.photowarp.filter;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by cho on 2017-02-06.
 */

public class BulgeFilter extends AbstractFilter {


    public BulgeFilter(Context c, Bitmap image) {
        super(c, image);
    }

    @Override
    public void applyFilter() {
        script.invoke_bulge(1.0f);
    }
}
