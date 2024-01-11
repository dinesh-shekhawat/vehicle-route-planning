package edu.northeastern.csye6220.vehiclerouteplanning.util;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.AbstractEntity;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.Otp;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.UserAccess;

public class EntityTableMap {
	
	private EntityTableMap() {
		
	}
	
	// Might need in case of native SQL queries
    private static Map<Class<? extends AbstractEntity>, String> map = new HashMap<>();
	    
	static {
		// Add mappings between entity classes and table names
		map.put(UserAccess.class, "user_access");
		map.put(User.class, "user");
		map.put(Otp.class, "otp");
	}

	public static String getTableName(Class<? extends AbstractEntity> entityClass) {
		return map.get(entityClass);
	}

}
