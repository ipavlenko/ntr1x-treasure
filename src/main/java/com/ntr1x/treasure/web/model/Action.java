package com.ntr1x.treasure.web.model;

import java.util.stream.Stream;

public enum Action {
	
	CREATE,
	REMOVE,
	UPDATE,
	IGNORE,
	
	;
	
	public static <T extends Managed> Stream<T> filter(Stream<T> stream, Action action, Action assume) {
		return stream
			.filter((t) -> t.getAction() == action || t.getAction() == null && assume == action)
		;
	}
	
	public static <T extends Managed> Stream<T> create(Stream<T> list, Action assume) {
		return filter(list, CREATE, assume);
	}
	
	public static <T extends Managed> Stream<T> create(Stream<T> list) {
		return filter(list, CREATE, IGNORE);
	}
	
	public static <T extends Managed> Stream<T> remove(Stream<T> list, Action assume) {
		return filter(list, REMOVE, assume);
	}
	
	public static <T extends Managed> Stream<T> remove(Stream<T> list) {
		return filter(list, REMOVE, IGNORE);
	}
	
	public static <T extends Managed> Stream<T> update(Stream<T> list, Action assume) {
		return filter(list, UPDATE, assume);
	}
	
	public static <T extends Managed> Stream<T> update(Stream<T> list) {
		return filter(list, UPDATE, IGNORE);
	}
	
	public static <T extends Managed> Stream<T> ignore(Stream<T> list, Action assume) {
		return filter(list, IGNORE, assume);
	}
	
	public static <T extends Managed> Stream<T> ignore(Stream<T> list) {
		return filter(list, IGNORE, IGNORE);
	}
}
