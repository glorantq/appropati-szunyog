package hu.appropati.szunyog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hu.appropati.szunyog.input.text.TextInputListener;
import hu.appropati.szunyog.input.text.TextInputProvider;

public class AndroidLauncher extends AndroidApplication implements TextInputProvider {
    private LinearLayout inputLayout;
    private EditText textInput;
    private List<TextInputListener> textInputListeners = new CopyOnWriteArrayList<>();

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGyroscope = false;
		config.useRotationVectorSensor = false;

        View gameView = initializeForView(Trainer.createTrainer(this), config);

        setContentView(gameView);

        inputLayout = new LinearLayout(this);
        LinearLayout.LayoutParams inputLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputLayoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        inputLayoutParams.topMargin = inputLayoutParams.leftMargin;
        inputLayout.setLayoutParams(inputLayoutParams);

        textInput = new EditText(this);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        textInput.setLayoutParams(editTextParams);
        textInput.setSingleLine(true);
        textInput.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        textInput.setBackgroundColor(Color.WHITE);

        Button okButton = new Button(this);
        okButton.setText("OK");
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        okButton.setLayoutParams(buttonParams);

        inputLayout.addView(textInput);
        inputLayout.addView(okButton);

        addContentView(inputLayout, inputLayoutParams);

        inputLayout.setVisibility(View.INVISIBLE);

        okButton.setOnClickListener(view -> {
            closeTextInput();
            broadcastTextChange();
        });

        textInput.setOnKeyListener((view, i, keyEvent) -> {
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                closeTextInput();
                return true;
            }

            broadcastTextChange();

            return false;
        });
	}

    @Override
    public void registerListener(TextInputListener textInputListener) {
        textInputListeners.add(textInputListener);
    }

    @Override
    public void removeListener(TextInputListener textInputListener) {
        textInputListeners.remove(textInputListener);
    }

    @Override
    public void openTextInput(String placeholder, InputType type, int maxChars) {
	    Log.i("Open", "O");
	    runOnUiThread(() -> {
            textInput.setText("");
            textInput.setHint(placeholder);

            textInput.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(maxChars)});

            textInput.setInputType(type == InputType.NUMBER ? android.text.InputType.TYPE_CLASS_NUMBER : android.text.InputType.TYPE_CLASS_TEXT);

            inputLayout.setVisibility(View.VISIBLE);
            inputLayout.requestFocus();

	        Log.i("Open", "UI");
	    });
    }

    @Override
    public void closeTextInput() {
        runOnUiThread(() -> {
            inputLayout.setVisibility(View.INVISIBLE);

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();

            if (view == null) {
                view = new View(AndroidLauncher.this);
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        broadcastTextChange();
    }

    private void broadcastTextChange() {
	    textInputListeners.forEach((it) -> it.textUpdated(textInput.getText().toString()));
    }
}
