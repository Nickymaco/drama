package org.drama.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class ImmutableSet<E> implements Set<E> {
	private Set<E> proxySet;
	
	private ImmutableSet(Set<E> proxySet) {
		this.proxySet = proxySet;
	}

	@Override
	public int size() {
		return proxySet.size();
	}

	@Override
	public boolean isEmpty() {
		return proxySet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return proxySet.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return proxySet.iterator();
	}

	@Override
	public Object[] toArray() {
		return proxySet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return proxySet.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return proxySet.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
	}

	public static <T> ImmutableSet<T> newInstance(Set<T> s) {
		return new ImmutableSet<T>(Collections.unmodifiableSet(s));
	}
}
