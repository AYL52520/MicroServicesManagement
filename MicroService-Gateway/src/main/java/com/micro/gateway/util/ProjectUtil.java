package com.micro.gateway.util;

import java.util.HashMap;
import java.util.Map;

public class ProjectUtil {
	public static String toString(Exception exception) {
		StringBuilder sb = new StringBuilder();
		if (null != exception) {
			sb.append(exception);
			for (StackTraceElement per : exception.getStackTrace()) {
				sb.append("\n")
					.append("\t")
					.append(per);
			}
		}
		return sb.toString();
	}

	public static <T> void reloadMap(Map<String, T> standard, Map<String, T> newElement) {
		Map<String, T> copy = new HashMap<>(standard);
		newElement.forEach((k, v) -> standard.put(k, v));
		copy.keySet().forEach(per -> {
			if (!newElement.containsKey(per)) {
				standard.remove(per);
			}
		});
	}

	public static Map<String, Object> newMap(Object... args) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < args.length; i += 2) {
			map.put((String) args[i], args[i + 1]);
		}
		return map;
	}

}
