package bartushk.gentpass.ui;

import java.io.IOException;
import java.util.ArrayList;
import bartushk.gentpass.R;
import bartushk.gentpass.core.GentPassApp;
import bartushk.gentpass.core.Utils;
import bartushk.gentpass.data.PasswordInfo;
import bartushk.gentpass.data.User;
import bartushk.gentpass.io.Reader;
import bartushk.gentpass.io.Writer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Name : PasswordScreen.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : This activity displays the user a list of all the titles of
 * their PasswordInfo, and copies their password to the clip board when an item
 * is selected. It also allows the user to see the information about each
 * password, edit, or delete a password on a long click. The user can also
 * create new passwords here.
 */
public class PasswordScreen extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener, TextWatcher {

	// The currently logged in user.
	private User currentUser;
	// Constant for calling the password creation activity for result.
	private final int PASSWORD_SCREEN = 1;

	// List view that will display the password titles.
	ListView passwordList;
	// Button for adding new passwords.
	Button addButton;
	// Edit text for searching for passwords.
	EditText searchEdit;
	// AlertDialog displayed on a list item long click.
	AlertDialog longClickDialog;
	// AlertDialog to confirm a list item deletion.
	AlertDialog confirmDialog;
	// ArrayList holding the displayed PasswordInfo
	ArrayList<PasswordInfo> passwordsDisplayed;
	// The most currently long clicked password info.
	PasswordInfo currentPass;
	// Whether the current call to NewPasswordScreen is an edit call or not.
	boolean edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_screen);
		// Get the current user set at the LoginScreen from the application
		// context.
		currentUser = ((GentPassApp) this.getApplication()).getUser();
		// Initialize a few variables.
		edit = false;
		passwordsDisplayed = new ArrayList<PasswordInfo>();

		// Populate the passwordsAll ArrayList.
        Utils.passwordInfoInsertionSort(currentUser.getPasswordInfos());
        initUI();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Check which activity is returning and take the correct actions.
		switch (requestCode) {
		case (PASSWORD_SCREEN): {
			// The case that a NewPasswordScreen returned okay, so a new
			// password is made with the information bundled with the returned
			// intent.
			if (resultCode == Activity.RESULT_OK) {
				// If it was an edited password, remove the previous password.
				if (edit) {
                    currentUser.getPasswordInfos().remove(currentPass);
					edit = false;

				}
				// Get the returned info.
				String title = data.getStringExtra("title");
				String password = data.getStringExtra("password");
				String notes = data.getStringExtra("notes");
				// Add a new password to the passwords ArrayList.
                currentUser.getPasswordInfos().add(new PasswordInfo(title, password, notes));

				// Resort the list.
				Utils.passwordInfoInsertionSort(currentUser.getPasswordInfos());

				// Rewrite the list.
				saveListToSDCard();

				// Set the list view after adding the new password.
				setListView();
			}
			break;
		}
		}
	}

	// Initiate the UI elements.
	private void initUI() {
		// Bind variables to buttons from the layout XML.
		addButton = (Button) findViewById(R.id.addPasswordButton);
		passwordList = (ListView) findViewById(R.id.passwordList);
		searchEdit = (EditText) findViewById(R.id.searchPasswordsText);
		// Set the listeners to this activity.
		searchEdit.addTextChangedListener(this);
		addButton.setOnClickListener(this);
		passwordList.setOnItemClickListener(this);
		passwordList.setOnItemLongClickListener(this);
		// Set the passwordList ListView.
		setListView();

		// String values that will be displayed in the alertDialog triggered on
		// a ListView long click item.
		final CharSequence[] items = { "Edit", "Delete", "View Info" };

		// Build the longClick alert dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Password Options:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					// When the edit item in the dialog is clicked start the
					// NewPasswordScreen passing the information of the password
					// being edited to the next screen.
					edit = true;
					Intent i = new Intent(PasswordScreen.this,
							NewPasswordScreen.class);
					i.putExtra("title", currentPass.getTitle());
					i.putExtra("password", currentPass.getPassword());
					i.putExtra("notes", currentPass.getNotes());
					startActivityForResult(i, PASSWORD_SCREEN);
					break;
				case 1:
					// When the delete item in the dialog is clicked, delete the
					// corresponding item from the passwords list after showing
					// an alert dialog to confirm.
					confirmDialog.setTitle("Delete " + currentPass.getTitle() + "?");
					confirmDialog.show();
					break;
				case 2:
					// When the note info item is clicked an alert dialog
					// displaying the corresponding passwordInfo is shown.
					AlertDialog alert = new AlertDialog.Builder(
							PasswordScreen.this).create();
					alert.setTitle("Password Info:");
					alert.setMessage("Title:\n" + currentPass.getTitle()
							+ "\n\n" + "Password:\n"
							+ currentPass.getPassword() + "\n\n" + "Notes:\n"
							+ currentPass.getNotes());
					alert.show();
					break;

				}
			}
		});
		// Set longClickDialog with the builder.
		longClickDialog = builder.create();

		// Create the dialog for confirming a password deletion.
		confirmDialog = new AlertDialog.Builder(PasswordScreen.this).create();

		// Create the first button.
		confirmDialog.setButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// When this button, titled Delete, is clicked, delete
						// the corresponding item from the passwords list.
                        currentUser.getPasswordInfos().remove(currentPass);

						// Save changes to the SD card and reset the
						// PasswordList to show changes.
						saveListToSDCard();
						setListView();
					}
				});
		// Create the second button.
		confirmDialog.setButton2("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// When this button, titled Cancel, is clicked, dismiss
						// the dialog.
						confirmDialog.dismiss();
					}
				});
	}

	public void onClick(View arg0) {
		// When the new password button is clicked, create a new password.
		switch (arg0.getId()) {
		case R.id.addPasswordButton:
			Intent i = new Intent(PasswordScreen.this, NewPasswordScreen.class);
			startActivityForResult(i, PASSWORD_SCREEN);
			break;
		}
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// If the passwords ArrayList is not empty, set the passLocation and
		// currentPass and allow the Dialog to handle the rest.
		if (!passwordsDisplayed.isEmpty()) {
			currentPass = passwordsDisplayed.get(arg2);
			longClickDialog.show();
		} else {
			// IF the password list is empty, start the NewPasswordScreen
			// activity.
			Intent i = new Intent(PasswordScreen.this, NewPasswordScreen.class);
			startActivityForResult(i, PASSWORD_SCREEN);
		}
		return false;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// If the passwords ArrayList is not empty, copy the current password to
		// the clipboard.
		if (!passwordsDisplayed.isEmpty()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(passwordsDisplayed.get(arg2).getPassword());
			ToastUtils.displayToast("Password Copied", this);
		} else {
			// IF the password list is empty, start the NewPasswordScreen
			// activity.
			Intent i = new Intent(PasswordScreen.this, NewPasswordScreen.class);
			startActivityForResult(i, PASSWORD_SCREEN);
		}
	}

	public void afterTextChanged(Editable s) {
		// Calls setListView whenever the search text is changed.
		setListView();
	}

	// Unused TextWatcher function.
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	// Unused TextWatcher function.
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	// Resets the passwordList ListView.
	private void setListView() {
		// Get the string to filter the list with.
		passwordsDisplayed.clear();
		String search = searchEdit.getText().toString().toLowerCase();
		ArrayList<String> toDisplay = new ArrayList<String>();
		if (!currentUser.getPasswordInfos().isEmpty()) {
			// If the passwordsAll list is not empty, iterate through it adding
			// the title to the toDisplay string array list and the PasswordInfo
			// to the passwordsDisplayed ArrayList if the password search text
			// is empty or the title contains the search text.
			for (int i = 0; i < currentUser.getPasswordInfos().size(); i++) {
				if (search.equals("")
						|| currentUser.getPasswordInfos().get(i).getTitle().toLowerCase().contains(search)) {
					toDisplay.add(currentUser.getPasswordInfos().get(i).getTitle());
					passwordsDisplayed.add(currentUser.getPasswordInfos().get(i));
				}
			}
		} else {
			toDisplay.add("No Passwords Saved");
		}
		// Set the passwordList arrayAdapter.
		passwordList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.simple_list, R.id.list_content, toDisplay));
	}

	// Saves the current passwords list to the Android SD Card.
	private void saveListToSDCard() {
		try {
			Writer write = new Writer();
			write.rewriteUserPasswords(currentUser);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
