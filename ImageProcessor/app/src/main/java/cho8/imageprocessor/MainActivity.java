package cho8.imageprocessor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    private String selectedImagePath = "";
    final private int SELECT_IMAGE = 909;
    private ImageView imageView;
    private Button browseButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.openImage);
        browseButton = (Button)findViewById(R.id.browseButton);

        browseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set up intent to open image from Gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
            }
        });

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

                        selectedImageBm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        imageView.setImageBitmap(selectedImageBm);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                }

        }
    }
}
