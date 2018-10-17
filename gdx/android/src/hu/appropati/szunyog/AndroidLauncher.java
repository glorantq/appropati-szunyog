package hu.appropati.szunyog;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

    float androidHeight = 0;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Window window = getWindow();
        View rootView = window.getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect size = new Rect();
            View view = window.getDecorView();

            view.getWindowVisibleDisplayFrame(size);

            androidHeight = size.height();

            runOnUiThread(() -> {
                RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                newParams.topMargin = (int) androidHeight - inputLayout.getHeight();
                inputLayout.setLayoutParams(newParams);
            });
        });

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGyroscope = false;
		config.useRotationVectorSensor = false;

        View gameView = initializeForView(Trainer.createTrainer(this), config);

        inputLayout = new LinearLayout(this);

        textInput = new EditText(this);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        textInput.setLayoutParams(editTextParams);
        textInput.setSingleLine(true);

        Button okButton = new Button(this);
        okButton.setText("OK");
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        okButton.setLayoutParams(buttonParams);

        inputLayout.addView(textInput);
        inputLayout.addView(okButton);
        inputLayout.setY(0);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        gameView.setLayoutParams(gameViewParams);

        relativeLayout.addView(gameView);

        RelativeLayout.LayoutParams inputParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputLayout.setLayoutParams(inputParams);
        relativeLayout.addView(inputLayout);

        setContentView(relativeLayout);

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
    public void openTextInput(String placeholder, String text, InputType type, int maxChars) {
	    runOnUiThread(() -> {
            textInput.setText(text);
            textInput.setHint(placeholder);

            textInput.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(maxChars)});

            textInput.setInputType(type == InputType.NUMBER ? android.text.InputType.TYPE_CLASS_NUMBER : android.text.InputType.TYPE_CLASS_TEXT);

            inputLayout.setVisibility(View.VISIBLE);
            textInput.requestFocus();

            changeKeyboard(true);
	    });
    }

    @Override
    public void closeTextInput() {
        runOnUiThread(() -> {
            inputLayout.setVisibility(View.INVISIBLE);

            changeKeyboard(false);
        });

        broadcastTextChange();
    }

    private void changeKeyboard(boolean show) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view == null) {
            view = new View(AndroidLauncher.this);
        }

        if(!show) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            imm.showSoftInput(textInput, 0);
        }
    }

    private void broadcastTextChange() {
	    textInputListeners.forEach((it) -> it.textUpdated(textInput.getText().toString()));
    }
}
