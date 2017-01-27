package cho8.noisereduction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private int medianSize;
    private int meanSize;
    private int maxSize;

    private SeekBar meanSeek;
    private SeekBar medianSeek;

    private EditText editMean;
    private EditText editMedian;

    private Button applyButton;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPref = getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
        maxSize = setOddMaxSize(sharedPref.getInt("MAX_SIZE",1));

        Log.i("SettingsMax", "Max size: "+String.valueOf(maxSize));

        meanSeek = (SeekBar) findViewById(R.id.seekMean);
        meanSeek.incrementProgressBy(2);

        medianSeek = (SeekBar) findViewById(R.id.seekMedian);
        medianSeek.incrementProgressBy(2);


        editMean = (EditText) findViewById(R.id.editMean);
        editMean.setText(String.valueOf(meanSize));

        editMedian = (EditText) findViewById(R.id.editMedian);
        editMedian.setText(String.valueOf(medianSize));

        setEditTextMaxLength(String.valueOf(maxSize).length());
        applyButton = (Button) findViewById(R.id.buttonApply);


        meanSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int minimum = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < minimum) {
                    progress = minimum;
                }
                meanSize = sanitizeSizeInput(progress);
                editMean.setText(String.valueOf(meanSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        medianSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int minimum = 1 ;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < minimum) {
                    progress = 1;
                }
                medianSize = sanitizeSizeInput(progress);
                editMedian.setText(String.valueOf(medianSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        editMean.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    return;
                }
                meanSize = Integer.parseInt(s.toString());
                meanSeek.setProgress(meanSize);
                editMean.setSelection(editMean.getText().length());

            }
        });

        editMean.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Editable meanText = editMean.getText();
                if (hasFocus) {
                    meanText.clear();
                }
            }
        });

        editMedian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    return;
                }
                medianSize = Integer.parseInt(s.toString());
                medianSeek.setProgress(medianSize);
                editMedian.setSelection(editMedian.length());
            }
        });

        editMedian.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Editable medianText = editMedian.getText();
                if(hasFocus) {
                    medianText.clear();
                }
            }
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


        // Touch interceptor for putting edit texts in and out of focus
        FrameLayout touchInterceptor = (FrameLayout)findViewById(R.id.touchInterceptor);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (editMean.isFocused()) {
                        Rect outRect = new Rect();
                        editMean.getGlobalVisibleRect(outRect);

                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {

                            meanSize = sanitizeSizeInput(meanSize);
                            editMean.setText(String.valueOf(meanSize));
                            meanSeek.setProgress(meanSize);
                            editMean.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    } else if (editMedian.isFocused()) {
                        Rect outRect = new Rect();
                        editMedian.getGlobalVisibleRect(outRect);

                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {

                            medianSize = sanitizeSizeInput(medianSize);
                            editMedian.setText(String.valueOf(medianSize));
                            medianSeek.setProgress(medianSize);
                            editMedian.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        meanSize = sharedPref.getInt("MEAN_SIZE",1);
        meanSeek.setProgress(meanSize);
        meanSeek.setMax(maxSize);

        medianSize = sharedPref.getInt("MEDIAN_SIZE",1);
        medianSeek.setProgress(medianSize);
        medianSeek.setMax(maxSize);

    }



    // check size input to odd and less than maxSize
    int sanitizeSizeInput (int size) {
        if (size > maxSize) {
            size = maxSize;
        }
        if (size % 2 == 0) {
            size = size + 1;
        }
        return size;
    }


    int setOddMaxSize(int size) {
        if (size % 2 == 0) {
            return size -1;
        }
        else return size;
    }

    public void setEditTextMaxLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editMean.setFilters(FilterArray);
        editMedian.setFilters(FilterArray);
    }

}
