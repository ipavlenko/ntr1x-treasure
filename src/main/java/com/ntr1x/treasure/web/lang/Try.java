package com.ntr1x.treasure.web.lang;

import java.util.Optional;
import java.util.concurrent.Callable;


public class Try {
	
	public static <T> Optional<T> get(Callable<T> callable) {
		
		try {
			return Optional.of(callable.call());
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	public static void run(Runnable runnable) {
		
		try {
			runnable.run();
		} catch (Exception e) {
			// ignore
		}
	}
}
