package bartushk.gentpass.ui;

import java.io.IOException;

import bartushk.gentpass.R;
import bartushk.gentpass.core.Authorizer;
import bartushk.gentpass.data.User;
import bartushk.gentpass.io.Writer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Name : NewUserScreen.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Activity where a user can register a new login.
 */
public class NewUserScreen extends Activity implements OnClickListener {

	// Create or cancel buttons.
	Button createButton, cancelButton;
	// EditTexts for corresponding user input.
	EditText usernameEdit, passwordEdit, confirmEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user_screen);
		// Initialize the UI.
		initUI();
	}

	// Initiate the UI elements.
	private void initUI() {
		// Bind variables to buttons from the layout XML.
		usernameEdit = (EditText) findViewById(R.id.newUsernameEdit);
		passwordEdit = (EditText) findViewById(R.id.newPasswordEdit);
		confirmEdit = (EditText) findViewById(R.id.newConfirmPassEdit);
		createButton = (Button) findViewById(R.id.createNewUserButton);
		cancelButton = (Button) findViewById(R.id.cancelNewUserButton);
		// Set button onClickListeners
		createButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	public void onClick(View v) {

		// Take the correct actions based on which button is clicked.
		switch (v.getId()) {
		case R.id.createNewUserButton:
			createNewUser();
			break;
		case R.id.cancelNewUserButton:
			this.finish();
			break;

		}

	}

	// Takes input from the user bia edit texts and if the desired username and
	// password are valid and unused, creates a new valid user.
	private void createNewUser() {
		// Gets user input.
		String username = usernameEdit.getText().toString();
		String password = passwordEdit.getText().toString();
		String confirm = confirmEdit.getText().toString();

		// Checks username and password criteria.
		if (username.length() > 7 && password.length() > 7) {
			if (password.equals(confirm)) {
				User tmp = new User(username, password);
				// Check if the user already exists.
				if (!Authorizer.userAlreadyExists(tmp)) {
					try {
						// Add a user with the writer.
						Writer write = new Writer();
						write.addUser(tmp);
						// Notify the user of success.
						ToastUtils.displayToast(
								"User Created: " + tmp.getUsername(), this);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Elses notify the user of failures.
				} else {
					ToastUtils.displayToast("Username already exists.", this);
				}
			} else {
				ToastUtils.displayToast(
						"Password and confirmation do not match.", this);
			}
		} else {
			ToastUtils
					.displayToast(
							"Username and Password must be at least 8 characters long.",
							this);
		}

	}

}
