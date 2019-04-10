package org.drama.vo;

import java.io.Serializable;

public class BiParameterValueObject<P1, P2> implements Serializable,Comparable<BiParameterValueObject<P1, P2>> {
	private static final long serialVersionUID = 4835032246561552286L;
	private P1 param1;
	private P2 param2;
	private Comparable<BiParameterValueObject<P1, P2>> comparableDelegate;
	
	public BiParameterValueObject() {
	}
	
	public BiParameterValueObject(P1 p1, P2 p2) {
		setParam1(p1);
		setParam2(p2);
	}
	
	public BiParameterValueObject(P1 p1, P2 p2, Comparable<BiParameterValueObject<P1, P2>> delegate) {
		this(p1,p2);
		this.setComparableDelegate(delegate);
	}
	
	public P1 getParam1() {
		return param1;
	}
	
	public void setParam1(P1 param1) {
		this.param1 = param1;
	}
	
	public P2 getParam2() {
		return param2;
	}
	
	public void setParam2(P2 param2) {
		this.param2 = param2;
	}

	@Override
	public int compareTo(BiParameterValueObject<P1, P2> o) {
		if(getComparableDelegate() != null) {
			return getComparableDelegate().compareTo(o);
		}
		return 0;
	}

	public Comparable<BiParameterValueObject<P1, P2>> getComparableDelegate() {
		return comparableDelegate;
	}

	public void setComparableDelegate(Comparable<BiParameterValueObject<P1, P2>> comparableDelegate) {
		this.comparableDelegate = comparableDelegate;
	}
}
