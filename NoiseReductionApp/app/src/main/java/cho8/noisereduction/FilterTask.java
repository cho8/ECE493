package cho8.noisereduction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by cho on 2017-01-22.
 */

public class FilterTask extends AsyncTask<Object, Double, Bitmap> {

    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        Log.i("MeanFilterLog", "doing in Background");
        AbstractFilter filter = (AbstractFilter) params[0];
        Bitmap newBm = filter.applyFilter();
        Log.i("MeanFilterLog", "donee task");
        return newBm;
    }

    @Override
    protected void onProgressUpdate(Double[] values) {
        super.onProgressUpdate();

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        delegate.processFinish(result);

    }


}
