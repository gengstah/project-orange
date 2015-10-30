package org.geeksexception.project.talent.util;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
	
	private static final char[] symbols;
	
	private static final Random random = new Random();
	
	private static final char[] buf = new char[32];
	
	static {
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();
	}
	
	public static String generatePassword(String password) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
		
		return passwordEncoder.encode(password);
		
	}
	
	public static String generateRandomPassword() {
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}
	
}