package cho8.noisereduction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private String selectedImagePath = "";
    final private int SELECT_IMAGE = 909;

    private ImageView imageView;
    private TextView initialTextView;

    private Button meanFilterButton;
    private Button medianFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.openImage);
        initialTextView = (TextView)findViewById(R.id.initialText);

        meanFilterButton = (Button)findViewById(R.id.meanButton);
        medianFilterButton = (Button)findViewById(R.id.medianButton);




    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE :

                if (resultCode == Activity.RESULT_OK) {
                    Bitmap selectedImageBm = null;
                    try
                    {
                        // Display selected image
                        selectedImageBm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.browseItem:
                //your code
                // EX : call intent if you want to swich to other activity
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
