package cho8.photowarp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity {

    final private int SELECT_IMAGE_CODE = 1;
    final private int CAMERA_IMAGE_CODE = 2;

    private ImageView imageView;
    private TextView initialTextView;

    private LinkedList<Bitmap> imageBmList;

    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private ProgressBar mProgress;

    private Button undoButton;
    private Button redoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageBmList = new LinkedList<>();

        imageView = (ImageView)findViewById(R.id.imageSelected);
        initialTextView = (TextView)findViewById(R.id.textInitial);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setProgress(0);

        undoButton = (Button) findViewById(R.id.buttonUndo);
        redoButton = (Button) findViewById(R.id.buttonRedo);

        undoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        redoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        // default values upon creation
        sharedPref = this.getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemBrowse:
                Intent browseIntent = new Intent();
                browseIntent.setType("image/*");
                browseIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(browseIntent, "Select image"), SELECT_IMAGE_CODE);
                return true;

            case R.id.itemCamera:
                Intent cameraIntent = new Intent();
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE);
                }
                return true;

            case R.id.itemSettings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);

                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE_CODE :

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        // Display selected image
                        Bitmap imageBm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        setImage(imageBm);

                        // Set buttons
                        initialTextView.setVisibility(View.INVISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;

            case CAMERA_IMAGE_CODE :

                if (resultCode == Activity.RESULT_OK) {
                    try {
//                        saveCameraImage(data);
                        initialTextView.setVisibility(View.INVISIBLE);
                        imageBmList = new LinkedList<Bitmap> ();
                        Bitmap imageBm = (Bitmap) data.getExtras().get("data");

                        setImage((Bitmap) data.getExtras().get("data"));
                        saveImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
        }
    }


    private void setImage(Bitmap data) throws IOException{


        imageBmList.addFirst(data);
        SharedPreferences sharedPref = this.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        int undoSetting = sharedPref.getInt("UNDO_COUNT", 5)+1;

        while (undoSetting < imageBmList.size())
        {
            Log.i("UndoHistoryRemove", "Settings "+undoSetting+" Size "+imageBmList.size());
            imageBmList.removeLast();
        }


        updateUI();

        imageView.setImageBitmap(imageBmList.peek());
    }

    private void updateUI() {
        if (imageBmList.peek() != null) {
            initialTextView.setVisibility(View.INVISIBLE);
        }

        if (imageBmList.size() > 1)
        {
            undoButton.setEnabled(true);
        } else
        {
            undoButton.setEnabled(false);
        }
    }

    private void saveImage() {
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

            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
