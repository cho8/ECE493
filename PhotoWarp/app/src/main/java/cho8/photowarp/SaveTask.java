package cho8.photowarp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by cho on 2017-02-05.
 */

public class SaveTask extends AsyncTask<Object, Integer, Void>{

    Context context;
    AsyncResponse delegate;

    public SaveTask(Context c){
        context = c;
    }
    @Override
    protected Void doInBackground(Object[] params) {

        LinkedList<Bitmap> imageBmList = (LinkedList)params[0];
        try {
            // saving image taken with camera
            // http://stackoverflow.com/a/3013625

            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File file = new File(path, timeStamp +".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            fOut = new FileOutputStream(file);

            Bitmap pictureBitmap = imageBmList.peek();
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
