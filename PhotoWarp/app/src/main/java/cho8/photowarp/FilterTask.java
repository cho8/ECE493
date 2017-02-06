package cho8.photowarp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by cho on 2017-01-22.
 */

public class FilterTask extends AsyncTask<Object, Integer, Bitmap> implements Observer{

    // http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        delegate.progressUpdate(0);
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        AbstractFilter filter = (AbstractFilter) params[0];
        filter.addObserver(this);
        Bitmap newBm = filter.applyFilter();
        return newBm;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate();
        delegate.progressUpdate(values[0]);

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        delegate.processFinish(result);

    }


    @Override
    public void update(Observable o, Object arg) {
        Log.i("ProgressUpdate", String.valueOf(arg));
        publishProgress((int)arg);
    }
}
