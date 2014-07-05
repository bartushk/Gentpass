package bartushk.gentpass.ui;

import bartushk.gentpass.R;
import bartushk.gentpass.crypto.Convert;
import bartushk.gentpass.crypto.HashUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * Name : GenerateTouchScreen.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.1<br>
 * Description : This activity uses touches from the user to get psuedo random
 * data and generate a password based upon it.
 */
public class GenerateTouchScreen extends Activity implements OnTouchListener {

	// Alert dialog used to display the password when the user is done with his
	// "touches" and allow the user to copy the password or cancel.s
	AlertDialog copyDlg;

	// Total touches the user desires, current touch, and user desired password
	// length.
	int touchesTotal, currentTouch, passwordLength;

	// If the user desires capitals or numbers in the password.
	boolean upper, number;
	// Displays the current touch number and a instruction to the user.
	TextView touchNumberText, touchInstructionText;

	// String of the most current touch coordinate and the password
	// most recently generated.
	String touchData, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set layout xml and initiate values.
		setContentView(R.layout.generate_touch_screen);
		touchesTotal = 10;
		currentTouch = 0;
		passwordLength = 10;
		upper = true;
		number = true;
		touchData = "";
		password = "";
		// Get the data bundled with the incoming intent.
		getData();
		// Initiate UI
		initUI();

	}

	// Initiate the UI elements.
	private void initUI() {
		// Bind variables to buttons from the layout XML.
		touchNumberText = (TextView) findViewById(R.id.touchCountText);
		touchInstructionText = (TextView) findViewById(R.id.touchInstructionText);
		// Set listener for touches from the user.
		touchNumberText.setOnTouchListener(this);

		// Set the initial values of the textViews.
		touchInstructionText.setText("Please Touch the Screen " + touchesTotal
				+ " times to generate");
		touchNumberText.setText(String.valueOf(currentTouch));

		// Create the dialog for displaying the generated password.
		copyDlg = new AlertDialog.Builder(GenerateTouchScreen.this).create();
		copyDlg.setTitle("Generated Password:");

		// Create the first button.
		copyDlg.setButton("Copy", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// When this button, titled copy, is clicked, copy the current
				// password value to the android clipboard.
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				clipboard.setText(password);
			}
		});
		copyDlg.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// When this button, titled copy, is clicked, dismiss the
				// dialog.
				copyDlg.dismiss();
			}
		});
	}

	// Gets the data that's bundled with the incoming intent.
	private void getData() {
		Bundle bun = this.getIntent().getExtras();
		touchesTotal = Integer.parseInt(bun.getString("touches"));
		passwordLength = Integer.parseInt(bun.getString("length"));
		if (bun.getString("upper").equals("no")) {
			upper = false;
		}
		if (bun.getString("number").equals("no")) {
			number = false;
		}
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {

		// Adds to the touchData string bassed on the touch
		// event coordinates.
		touchData += String.valueOf(arg1.getX());
		touchData += String.valueOf(arg1.getY());
		// increment the current touch number.
		currentTouch++;
		// Display the password when the current touch reaches the total number
		// of touches.
		if (touchesTotal == currentTouch) {
			displayPassword();
		}
		// Set the textView displaying the touch number to the correct value.
		touchNumberText.setText(String.valueOf(currentTouch));
		return false;
	}

	// Generate a pseudo random password utilizing the Convert static functions
	// in the format that the desires. Generate the password based of the SHA256
	// hash of the touchData string generated from user touch input.
	private void displayPassword() {

		if (upper && number) {
			password = Convert.lowerUpperNumberString(
					HashUtils.sha256Byte(touchData)).substring(0,
					passwordLength);
		} else if (number) {
			password = Convert.lowerNumberString(
					HashUtils.sha256Byte(touchData)).substring(0,
					passwordLength);
		} else if (upper) {
			password = Convert
					.lowerUpperString(HashUtils.sha256Byte(touchData))
					.substring(0, passwordLength);
		} else {
			password = Convert.lowerString(HashUtils.sha256Byte(touchData))
					.substring(0, passwordLength);
		}
		// Display the generated password with copyDlg.
		copyDlg.setMessage(password);
		copyDlg.show();
		// Reset the necessary data to restart generation.
		currentTouch = 0;
		touchData = "";
	}

}
