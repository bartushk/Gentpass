package bartushk.gentpass.core;

import java.util.ArrayList;

import bartushk.gentpass.data.PasswordInfo;

/**
 * Name : GentPassApp.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : A class for random static functions that didn't warrant their
 * own class, but also felt out of place where they were used.
 */
public class Utils {

	/**
	 * Sorts an ArrayList of PasswordInfo using insertion sort. Insertion sort
	 * was chosen because the list will be relatively short and nearly sorted in
	 * most cases.
	 * 
	 * @param inList
	 *            - The ArrayList of PasswordInfo to be sorted.
	 * @return void
	 */
	public static void passwordInfoInsertionSort(ArrayList<PasswordInfo> inList) {
		// Do an insertion sort.
		for (int i = 1; i < inList.size(); i++) {
			if (inList.get(i).getTitle()
					.compareTo(inList.get(i - 1).getTitle()) < 0) {
				PasswordInfo tmp = inList.get(i);
				for (int p = i - 1; p >= 0; p--) {
					if (p == 0) {
						inList.add(p, tmp);
						break;
					}
					if (inList.get(i).getTitle()
							.compareTo(inList.get(p - 1).getTitle()) > 0) {
						inList.add(p, tmp);
						break;
					}
				}
				inList.remove(i + 1);
			}
		}
	}
}
