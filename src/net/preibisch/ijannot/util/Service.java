package net.preibisch.ijannot.util;

import org.scijava.Context;
import org.scijava.log.LogService;

import net.imagej.ops.OpService;

public class Service {
	private static OpService opService;
	
	public static OpService getOps() {
		if(opService==null) {
			init();
		}
		return opService;
	}

	public static void init() {
		final Context context = new Context(OpService.class, LogService.class);
		opService = context.getService(OpService.class);
	}
}
