package com.micro.service.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
	public final static String LONG = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String DATE = "yyyyMMdd";
	public final static String TIME = "HHmmss";
	public final static String TIMESTAMP = "HHmmssSSS";

	public static String format(Instant instant) {
		return format(instant, LONG);
	}

	public static String format(Instant instant, String pattern) {
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern));
	}

	public static boolean isBefore(LocalDate localDate) {
		return localDate.isBefore(LocalDate.now());
	}
}
