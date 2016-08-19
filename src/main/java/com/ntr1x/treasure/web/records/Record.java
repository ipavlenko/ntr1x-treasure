package com.ntr1x.treasure.web.records;

import java.util.function.Function;
import java.util.function.Predicate;

import com.ntr1x.treasure.web.model.Managed;

public interface Record<T extends Managed> {
	
	T create(T managed, Function<T, String> alias, Predicate<T> validate);
	T update(T managed, Predicate<T> validate);
	T remove(T managed, Predicate<T> validate);
}
