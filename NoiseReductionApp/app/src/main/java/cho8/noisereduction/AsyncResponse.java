package cho8.noisereduction;

import android.graphics.Bitmap;

/**
 * Created by cho on 2017-01-22.
 */

// http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
public interface AsyncResponse {
    void processFinish(Bitmap output);
    void progressUpdate(int progress);

}
