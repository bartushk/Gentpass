package bartushk.gentpass.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import bartushk.gentpass.R;

/**
 * Name : GenerateOptionsScreen.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Screen for prompting the user for info about the password they
 * would like generated for them. For example: password length, include capital
 * letters, etc. Extends activity and implements OnClickListener for it's
 * buttons and OnSeekBarChangeListener for the seek bars.
 */
public class GenerateOptionsScreen extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	// Seek bars for user input on the password length and number of touches
	// during password generation.
	SeekBar lengthSeek, touchSeek;
	// Check boxes for inclusion of numbers, capitals, and special characters in
	// the password.
	// special characters are not included yet.
	CheckBox specialCharacterCheck, capitalLetterCheck, numbersCheck;
	// Buttons for moving to the generate screen or canceling.
	Button generateButton, cancelButton;
	// Textviews displaying the vales of the seek bars.
	TextView lengthText, touchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_options_screen);
		// Initiate the UI.
		initUI();
	}

	// Initiate the UI elements.
	private void initUI() {
		// Bind variables to buttons from the layout XML.
		generateButton = (Button) findViewById(R.id.generateButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		lengthSeek = (SeekBar) findViewById(R.id.lengthSeek);
		touchSeek = (SeekBar) findViewById(R.id.touchSeek);
		lengthText = (TextView) findViewById(R.id.lengthText);
		touchText = (TextView) findViewById(R.id.touchText);
		// specialCharacterCheck = (CheckBox) findViewById(R.id.specialsCheck);
		capitalLetterCheck = (CheckBox) findViewById(R.id.capitalsCheck);
		numbersCheck = (CheckBox) findViewById(R.id.numbersCheck);
		// Set the class as its own listener for the buttons and seek bars.
		generateButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		touchSeek.setOnSeekBarChangeListener(this);
		lengthSeek.setOnSeekBarChangeListener(this);
		// Set initial values of the seek bars to 10
		touchSeek.setProgress(10);
		lengthSeek.setProgress(10);
		// Set initial text of the textviews displying the seek bar info.
		lengthText.setText("Length:\n"
				+ String.valueOf(lengthSeek.getProgress()));
		touchText
				.setText("Tap # :\n" + String.valueOf(touchSeek.getProgress()));
	}

	public void onClick(View arg0) {
		// Switch statements for which view was clicked
		// that has this class sat as its listener.
		switch (arg0.getId()) {
		case R.id.generateButton:
			// Generate button is clicked, if the touch button
			// is greater than 0 start the password generation
			// activity, otherwise display a toast notifying the
			// user of the touch value error.
			if (touchSeek.getProgress() > 0) {
				startGenerateActivity();
			} else {
				ToastUtils.displayToast(
						"Number of touches must be at least 1.", this);
			}
			break;

		// Finish the activity if the cancel button is pressed.
		case R.id.cancelButton:
			this.finish();
			break;
		}
	}

	/**
	 * Starts the password generation activity with the correct values.
	 * 
	 * @return void
	 */
	private void startGenerateActivity() {
		// String values to be passed to the next activity
		// that are initialized to default values.
		String upper = "yes";
		String number = "yes";
		String touches = "10";
		String length = "10";

		// Set the correct values based on the user input.
		length = String.valueOf(lengthSeek.getProgress());
		touches = String.valueOf(touchSeek.getProgress());
		if (!numbersCheck.isChecked()) {
			number = "no";
		}
		if (!capitalLetterCheck.isChecked()) {
			upper = "no";
		}

		// Create an intent and bundle the correct info to it
		// and pass it to the next activity.
		Intent i = new Intent(GenerateOptionsScreen.this,
				GenerateTouchScreen.class);
		Bundle bun = new Bundle();
		bun.putString("length", length);
		bun.putString("touches", touches);
		bun.putString("number", number);
		bun.putString("upper", upper);
		i.putExtras(bun);
		startActivity(i);
	}

	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {

		// When a progress bar changes, reset the values of the
		// corresponding textViews.
		switch (arg0.getId()) {
		case R.id.touchSeek:
			touchText.setText("Tap # :\n"
					+ String.valueOf(touchSeek.getProgress()));
			break;
		case R.id.lengthSeek:
			lengthText.setText("Length:\n"
					+ String.valueOf(lengthSeek.getProgress()));
			break;
		}

	}

	// Unused seek bar listener function.
	public void onStartTrackingTouch(SeekBar arg0) {

	}

	// Unused seek bar listener function.
	public void onStopTrackingTouch(SeekBar arg0) {

	}

}
