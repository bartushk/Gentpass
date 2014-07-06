package bartushk.gentpass.ui;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import bartushk.gentpass.R;
import bartushk.gentpass.core.Authorizer;
import bartushk.gentpass.core.GentPassApp;
import bartushk.gentpass.core.Utils;
import bartushk.gentpass.data.User;
import bartushk.gentpass.io.Reader;
import bartushk.gentpass.web.LocalTrustHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Name : LoginScreen.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Activity where a user can either attempt to log in, generate a
 * password, or register as a new user.
 */
public class LoginScreen extends Activity implements OnClickListener {

	// Edit texts where the user can enter the username and password they would
	// like to log in as.
	EditText passwordEdit, usernameEdit;

	// Buttons used to either attempt a login, move to the new user creation
	// activity, and to move to password generation.
	Button loginButton, newUserButton, generateButton;

	// Checkbox where the user can decide if they want their username rememberd.
	CheckBox rememberCheck;

	// User preferences where the username is stored if desired.
	SharedPreferences userPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		// Get the user preferences.
		userPrefs = this.getSharedPreferences("user_pref", MODE_WORLD_READABLE);
		// Initialize the UI.
		initUI();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences.Editor prefsEditor = userPrefs.edit();
		prefsEditor.putBoolean("checked", rememberCheck.isChecked());
		if (!rememberCheck.isChecked()) {
			prefsEditor.putString("username", "");
		}

		prefsEditor.commit();
	}

	// Initiate the UI elements.
	private void initUI() {
		// Bind variables to buttons from the layout XML.
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
		usernameEdit = (EditText) findViewById(R.id.usernameEdit);
		loginButton = (Button) findViewById(R.id.loginButton);
		newUserButton = (Button) findViewById(R.id.newUserButton);
		generateButton = (Button) findViewById(R.id.generatePasswordButton);
		rememberCheck = (CheckBox) findViewById(R.id.rememberCheck);
		// Set button onClickListeners
		loginButton.setOnClickListener(this);
		newUserButton.setOnClickListener(this);
		generateButton.setOnClickListener(this);
		// Set the rememberCheck based off the userPreferences.
		rememberCheck.setChecked(userPrefs.getBoolean("checked", false));
		// If it's checked, set the usernameEdit text to the username in
		// savedPreferences.
		if (rememberCheck.isChecked()) {
			usernameEdit.setText(userPrefs.getString("username", ""));
		}

	}

	public void onClick(View arg0) {

		// Call the correct function based on the ID of the clicked button.
		switch (arg0.getId()) {
		case R.id.loginButton:
			loginUser();
			break;
		case R.id.newUserButton:
			createNewUser();
			break;
		case R.id.generatePasswordButton:
			launchPasswordGenerate();
			break;
		case R.id.testButton:
			testFunc();
			break;
		}

	}


	private void testFunc(){
        final HttpClient client = new LocalTrustHttpClient(this);
		Thread jsonPostThread = new Thread(){
			
			public void run(){
                try{

                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                    HttpResponse response;
                    JSONObject json = new JSONObject();

					HttpPost post = new HttpPost("https://192.168.1.9:32487/");
					json.put("test", "derp");
					StringEntity se = new StringEntity( json.toString() );
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
					post.setEntity(se);
					response = client.execute(post);
					if(response!=null){
                        HttpEntity entity = response.getEntity();
                        String responseString = EntityUtils.toString(entity, "UTF-8");
                        Log.d("responseString", responseString);
					}
					
				}catch(Exception e){
					Log.d("derpity", "derp error");
				}
			}		
		};
		
		jsonPostThread.start();
			
	}
	
	
	

	private boolean loginUser() {
		// Get the username and password from the corresponding editTexts and
		// create a temporary User with them.
		String username = usernameEdit.getText().toString();
		String password = passwordEdit.getText().toString();
		User tmp = new User(username, password);
		// Make sure the password is at least 8 characters long.
		if (username.length() > 7 && password.length() > 7) {
			// Check if the username and password are valid.
			if (Authorizer.validateUser(tmp)) {
				// Set the user SharedPreferences.
				setPreferences();

                try {
                    Reader read = new Reader();
                    tmp.setPasswordInfos(read.getUserPasswords(tmp));
                } catch (IOException e) {
                    e.printStackTrace();
                }
				// Set the user in the application context.
				((GentPassApp) this.getApplication()).setUser(tmp);
				// Start the password viewing activity.
				Intent i = new Intent(LoginScreen.this, PasswordScreen.class);
				startActivity(i);
			} else {
				// Notify the user that their username and password is invalid.
				ToastUtils.displayToast("Invalid Username or Password.", this);
			}
		} else {
			// Notify the user that their password or username is too short.
			ToastUtils
					.displayToast(
							"Username and Password must be at least 8 characters long.",
							this);
		}
		return false;
	}

	// Starts the activity that registers a new user.
	private void createNewUser() {
		Intent i = new Intent(LoginScreen.this, NewUserScreen.class);
		startActivity(i);
	}

	// Starts the activity that generates a new user.
	private void launchPasswordGenerate() {
		Intent i = new Intent(LoginScreen.this, GenerateOptionsScreen.class);
		startActivity(i);
	}

	// Sets the preferences based on what's in the usernameEdit and whether the
	// rememberCheck is checked.
	private void setPreferences() {
		SharedPreferences.Editor prefsEditor = userPrefs.edit();
		if (rememberCheck.isChecked()) {
			prefsEditor
					.putString("username", usernameEdit.getText().toString());
		} else {
			prefsEditor.putString("username", "");
		}
		prefsEditor.putBoolean("checked", rememberCheck.isChecked());
		prefsEditor.commit();
	}
}
