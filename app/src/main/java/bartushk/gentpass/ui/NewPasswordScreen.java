package bartushk.gentpass.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import bartushk.gentpass.R;

/**
 * Name : NewPasswordScreen.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Activity where a user can create or edit a saved password.
 */
public class NewPasswordScreen extends Activity implements OnClickListener {

	// Buttons for canceling, generating a password, or creating the new
	// password.
	Button createButton, cancelButton, generateButton;
	// EditTexts for corresponding user input.
	EditText titleText, passwordText, confirmText, notesText;
	// Strings for the PasswordInfo info.
	String notes, password, title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_password_screen);
		title = "";
		password = "";
		notes = "";
		// Gets the data passed with the intent bundle.
		getData();
		// Initialize the UI.
		initUI();
	}

	// Initiate the UI elements.
	private void initUI() {
		// Initialize the editTexts and set their text values. Their text values
		// are set because they will either be initialized to nothing or passed
		// to the activity when it is actually a password being edited.
		titleText = (EditText) findViewById(R.id.titleEdit);
		titleText.setText(title);
		passwordText = (EditText) findViewById(R.id.passwordEdit);
		passwordText.setText(password);
		confirmText = (EditText) findViewById(R.id.confirmEdit);
		confirmText.setText(password);
		notesText = (EditText) findViewById(R.id.notesEdit);
		notesText.setText(notes);
		createButton = (Button) findViewById(R.id.createButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		generateButton = (Button) findViewById(R.id.generateButton);
		// Set button onClickListeners
		createButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		generateButton.setOnClickListener(this);
	}

	// Gets the data that's bundled with the incoming intent. Information is
	// only passed with the intent if the password is being edited instead of
	// created.
	private void getData() {
		Bundle bun = this.getIntent().getExtras();
		if (bun != null) {
			title = bun.getString("title");
			password = bun.getString("password");
			notes = bun.getString("notes");
		}
	}

	public void onClick(View arg0) {
		// Take the correct actions based on which button is clicked.
		switch (arg0.getId()) {
		case R.id.createButton:
			createNewPassword();
			break;
		case R.id.cancelButton:
			this.finish();
			break;
		case R.id.generateButton:
			Intent i = new Intent(NewPasswordScreen.this,
					GenerateOptionsScreen.class);
			startActivity(i);
			break;
		}

	}

	private void createNewPassword() {
		// Get the information that the user input.
		String title = titleText.getText().toString();
		String password = passwordText.getText().toString();
		String confirm = confirmText.getText().toString();
		String notes = notesText.getText().toString();
		// Check that the user has input info.
		if (title.length() > 0) {
			if (password.length() > 0) {
				// See if the password and confirm password match.
				if (password.equals(confirm)) {
					// Return the info to the previous activity.
					Intent result = new Intent();
					result.putExtra("title", title);
					result.putExtra("password", password);
					result.putExtra("notes", notes);
					setResult(Activity.RESULT_OK, result);
					finish();
					// Elses notifying the users of input errors.
				} else {
					ToastUtils.displayToast("Passwords do not match.", this);
				}
			} else {
				ToastUtils.displayToast("Password field is empty.", this);
			}
		} else {
			ToastUtils.displayToast("Title field is empty.", this);
		}
	}
}
