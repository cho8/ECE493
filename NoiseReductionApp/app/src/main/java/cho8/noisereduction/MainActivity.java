package cho8.noisereduction;

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
import android.widget.Toast;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity implements AsyncResponse{

    final private int SELECT_IMAGE = 909;

    private ImageView imageView;
    private TextView initialTextView;

    private Button meanFilterButton;
    private Button medianFilterButton;

    private Bitmap selectedImageBm;

    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageSelected);
        initialTextView = (TextView)findViewById(R.id.textInitial);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setProgress(0);

        meanFilterButton = (Button)findViewById(R.id.buttonMean);
        medianFilterButton = (Button)findViewById(R.id.buttonMedian);




        meanFilterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFilter(new MeanFilter(selectedImageBm, sharedPref.getInt("MEAN_SIZE",1)));
            }
        });

        medianFilterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFilter(new MedianFilter(selectedImageBm, sharedPref.getInt("MEDIAN_SIZE",1)));
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

                startActivityForResult(Intent.createChooser(browseIntent, "Select Image"), SELECT_IMAGE);
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
            case SELECT_IMAGE :

                if (resultCode == Activity.RESULT_OK) {
                    try
                    {
                        // Display selected image
                        selectedImageBm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                        editor.putInt("MAX_SIZE", Math.min(selectedImageBm.getWidth(), selectedImageBm.getHeight()));
                        editor.putInt("MEAN_SIZE", 1);
                        editor.putInt("MEDIAN_SIZE", 1);
                        editor.commit();

                        imageView.setImageBitmap(selectedImageBm);

                        // Set buttons
                        initialTextView.setVisibility(View.INVISIBLE);
                        meanFilterButton.setVisibility(View.VISIBLE);
                        medianFilterButton.setVisibility(View.VISIBLE);

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                }

        }
    }

    public void executeFilter(AbstractFilter filter) {

        if (filter.maskSize <= 1) {
            Toast.makeText(getApplicationContext(), "Set the mask settings!",Toast.LENGTH_SHORT).show();
            return;
        }

        mProgress.setProgress(0);

        FilterTask filterTask= new FilterTask();
        filterTask.delegate = this;
        filterTask.execute(filter, this);

    }

    @Override
    public void processFinish(Bitmap output) {
        selectedImageBm = output;
        imageView.setImageBitmap(output);
        mProgress.setProgress(0);
    }

    @Override
    public void progressUpdate(int progress) {
        mProgress.setProgress((progress));
    }



}
