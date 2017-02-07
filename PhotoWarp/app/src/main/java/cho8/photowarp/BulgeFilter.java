package cho8.photowarp;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by cho on 2017-02-06.
 */

public class BulgeFilter extends AbstractFilter {

    protected ScriptC_transform script;

    public BulgeFilter(Context c, Bitmap image) {

        super(c, image);

        script = new ScriptC_transform(rs);
        script.set_height(image.getHeight());
        script.set_width(image.getWidth());
        script.bind_input(in);
    }

    @Override
    public void applyFilter() {
        script.forEach_bulge(in, out);
    }
}
