package ro.dragos.security;


import java.util.List;

public class UserHelper {

	public static boolean userExists(String username, List<User> users) {
		for (User u : users) {
			if (u.getUsername().equals(username))
				return true;
		}
		return false;
	}

}
