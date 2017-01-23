package cho8.noisereduction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private int medianSize;
    private int meanSize;
    private int maxSize;

    private SeekBar meanSeek;
    private SeekBar medianSeek;

    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        maxSize = getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE).getInt("MAXSIZE",0);

        meanSeek = (SeekBar) findViewById(R.id.seekMean);
        meanSeek.setProgress(1);
        meanSeek.incrementProgressBy(2);
        meanSeek.setMax(maxSize);

        medianSeek = (SeekBar) findViewById(R.id.seekMedian);
        medianSeek.setProgress(1);
        medianSeek.incrementProgressBy(2);
        medianSeek.setMax(maxSize);

        applyButton = (Button) findViewById(R.id.buttonApply);


        Log.i("SettingSize", ""+maxSize+"");

        meanSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            Toast mToast = Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT);
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mToast.setText(String.valueOf( progress));
                Log.i("SeekMean", ""+progress+"");
                meanSize = progress;
                mToast.show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        medianSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            Toast mToast = Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT);
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mToast.setText(String.valueOf( (progress)));
                Log.i("SeekMedian", ""+(progress+""));
                medianSize = (progress);
                mToast.show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putInt("MEAN_SIZE", meanSize);
                editor.putInt("MEDIAN_SIZE", medianSize);
                editor.commit();
                finish();
            }
        });


    }

}
