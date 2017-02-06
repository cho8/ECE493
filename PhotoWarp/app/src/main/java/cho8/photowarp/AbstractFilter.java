package cho8.photowarp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import java.util.Observable;

/**
 * Created by cho on 2017-01-22.
 */
public abstract class AbstractFilter extends Observable {

    protected Bitmap imageBm;
    protected RenderScript rs;
    protected ScriptC_warp script;
    protected Allocation in, out;


    public AbstractFilter(Context c, Bitmap image) {
        imageBm = image;
        rs = RenderScript.create(c);
        script = new ScriptC_warp(rs);

        in = Allocation.createFromBitmap(rs, image, Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
        out = Allocation.createTyped(rs, in.getType());

        script.set_height(image.getHeight());
        script.set_width(image.getWidth());
        script.bind_input(in);
        
    }

    public abstract Bitmap applyFilter();
}


