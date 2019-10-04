package net.preibisch.ijannot.util;

import org.apache.log4j.Logger;

public class Log {
	
	private static final Logger logger = Logger.getLogger(Log.class);

	public static void print(String s) {
		if (logger.isInfoEnabled()) {
			logger.info(s);
		}
	}

	public static void debug(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug(s);
		}
	}

	public static void error(String s) {
		logger.error(s);
	}

	public static void main(String[] args) {
		Log.print("Hello");
	}

}
