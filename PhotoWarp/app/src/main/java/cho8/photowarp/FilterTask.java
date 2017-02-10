package cho8.photowarp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by cho on 2017-01-22.
 */

public class FilterTask extends AsyncTask<Object, Integer, Bitmap>{

    // http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        AbstractFilter filter = (AbstractFilter) params[0];
        filter.applyFilter();
        return filter.getResult();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate();

    }

    protected void onPostExecute(Bitmap result) {
        delegate.processFinish(result);

    }
}
