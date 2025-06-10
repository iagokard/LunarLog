package com.lunarlog.util;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class CodeGenerator {

	private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

	public static String generateTrackingCode() {
		OffsetDateTime now = OffsetDateTime.now();
		return "TRK" + now.format(DATE_FORMATTER) + now.format(TIME_FORMATTER);
	}

	public static String generateRandomKeyword(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
		}
		return sb.toString();
	}
}
