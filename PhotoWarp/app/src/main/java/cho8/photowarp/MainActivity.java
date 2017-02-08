package cho8.photowarp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements AsyncResponse{

    final private int SELECT_IMAGE_CODE = 1;
    final private int CAMERA_IMAGE_CODE = 2;

    private ImageView imageView;
    private TextView initialTextView;

    private LinkedList<Bitmap> imageBmList;

    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private ProgressBar mProgress;

    private Button undoButton;
    private Button saveButton;
    private Button filterButton;

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
        saveButton = (Button) findViewById(R.id.buttonSave);

        filterButton = (Button) findViewById(R.id.buttonFilter);

        undoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                undoImage();
            }
        });

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFilter(new SwirlFilter(getApplicationContext(),imageBmList.peek()));
//                executeFilter(new BulgeFilter(getApplicationContext(), imageBmList.peek()));
            }
        });

        updateUI();
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
                        imageBmList = new LinkedList<Bitmap> ();
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

    public void executeFilter(AbstractFilter filter) {

        mProgress.setProgress(0);

        FilterTask filterTask= new FilterTask();
        filterTask.delegate = this;
        filterTask.execute(filter, this);

    }


    private void setImage(Bitmap data){


        imageBmList.addFirst(data);
        SharedPreferences sharedPref = this.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        int undoSetting = sharedPref.getInt("UNDO_COUNT", 5)+1;

        while (undoSetting < imageBmList.size())
        {
            Log.i("UndoHistorySetting", "Settings "+undoSetting+" Size "+imageBmList.size());
            imageBmList.removeLast();
        }


        updateUI();

        imageView.setImageBitmap(imageBmList.peek());
    }

    private void updateUI() {
        // if no image loaded
        if (imageBmList.peek() != null) {
            initialTextView.setVisibility(View.INVISIBLE);
            filterButton.setEnabled(true);
            saveButton.setEnabled(true);
        } else {
            filterButton.setEnabled(false);
            saveButton.setEnabled(false);
        }
        // if history available to undo
        if (imageBmList.size() > 1) {
            undoButton.setEnabled(true);

        } else {
            undoButton.setEnabled(false);

        }
    }

    private void saveImage() {
        SaveTask st = new SaveTask(getApplicationContext());
        st.delegate = this;
        st.execute(imageBmList);
    }

    private void undoImage() {
        imageBmList.removeFirst();
        Bitmap bm = imageBmList.pop();
        setImage(bm);
        updateUI();
        Log.i("UndoImageCount", String.valueOf(imageBmList.size()));
    }

    @Override
    public void processFinish(Bitmap output) {
        setImage(output);
        updateUI();
    }

    @Override
    public void progressUpdate(int progress) {
        mProgress.setProgress(progress);
    }
}
