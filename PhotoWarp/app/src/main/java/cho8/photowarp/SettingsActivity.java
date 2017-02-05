package cho8.photowarp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    private int undoCount;


    private SeekBar undoSeek;

    private EditText editUndo;

    private Button applyButton;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPref = getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);

        undoSeek = (SeekBar) findViewById(R.id.seekUndo);


        editUndo = (EditText) findViewById(R.id.editUndo);
        editUndo.setText(String.valueOf(undoCount));

        applyButton = (Button) findViewById(R.id.buttonApply);


        undoSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int minimum = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < minimum) {
                    progress = minimum;
                }
                undoCount = sanitizeSizeInput(progress);
                editUndo.setText(String.valueOf(undoCount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        editUndo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    return;
                }
                undoCount = Integer.parseInt(s.toString());
                undoSeek.setProgress(undoCount);
                editUndo.setSelection(editUndo.getText().length());

            }
        });

        editUndo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Editable meanText = editUndo.getText();
                if (hasFocus) {
                    meanText.clear();
                }
            }
        });


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putInt("UNDO_COUNT", undoCount);
                editor.putInt("MEDIAN_SIZE", undoCount);
                editor.commit();
                finish();
            }
        });


        // Touch interceptor for putting edit texts in and out of focus
        // http://stackoverflow.com/questions/4828636/edittext-clear-focus-on-touch-outside
        FrameLayout touchInterceptor = (FrameLayout)findViewById(R.id.touchInterceptor);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (editUndo.isFocused() || editUndo.isFocused()) {
                        Rect outRect = new Rect();
                        editUndo.getGlobalVisibleRect(outRect);

                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {

                            undoCount = sanitizeSizeInput(undoCount);
                            editUndo.setText(String.valueOf(undoCount));
                            undoSeek.setProgress(undoCount);
                            editUndo.clearFocus();


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
        undoCount = sharedPref.getInt("UNDO_COUNT",1);
        undoSeek.setProgress(undoCount);

    }



    // check size input to odd and less than maxSize
    int sanitizeSizeInput (int size) {

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
        editUndo.setFilters(FilterArray);
        editUndo.setFilters(FilterArray);
    }

}
