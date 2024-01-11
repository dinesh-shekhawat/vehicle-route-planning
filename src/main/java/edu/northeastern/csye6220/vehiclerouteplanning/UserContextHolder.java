package edu.northeastern.csye6220.vehiclerouteplanning;

public class UserContextHolder {
	private static final ThreadLocal<String> user = new ThreadLocal<>();

    public static void set(String userName) {
    	user.set(userName);
    }

    public static String get() {
        return user.get();
    }

    public static void clear() {
    	user.remove();
    }
	
}
