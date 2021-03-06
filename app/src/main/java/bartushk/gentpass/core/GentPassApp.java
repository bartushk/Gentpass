package bartushk.gentpass.core;

import bartushk.gentpass.data.User;
import bartushk.gentpass.web.SyncService;

import android.app.Application;

/**
 * Name : GentPassApp.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : An application extension used to pass objects between
 * different activities when necessary.
 */
public class GentPassApp extends Application {
	//User to be preserved across activities.
	private User user;
    private SyncService syncService = new SyncService(this.getBaseContext());
	
	//setters
	public void setUser(User user) {
		this.user = user;
	}

	
	//getters
	public User getUser() {
		return this.user;
	}

}