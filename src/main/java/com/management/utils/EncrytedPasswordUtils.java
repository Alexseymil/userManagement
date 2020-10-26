package com.management.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrytedPasswordUtils {
	
	 // Encryte Password with BCryptPasswordEncoder
    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    public static void main(String [] args) {
    	String password = "10";
    	System.out.println(password);
    	password = encrytePassword(password);
    	System.out.println(password);
    	System.out.println(password.length());
    	
    	
    }
}
