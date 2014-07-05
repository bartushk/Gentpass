package bartushk.gentpass.ui;

import android.content.Context;
import android.widget.Toast;

/**
 * Name : ToastUtils.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Static functions for more easily displaying toast messages
 * across activities.
 */
public class ToastUtils {

	// Quick easy way to display a long toast by passing the Activity context
	// and message.
	public static void displayToast(String message, Context context) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}

}
