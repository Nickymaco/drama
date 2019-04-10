package org.drama.vo;

import java.io.Serializable;

public class TriParameterValueObject<P1, P2, P3> implements Serializable, Comparable<TriParameterValueObject<P1, P2, P3>> {
	private static final long serialVersionUID = -7486375651358826417L;
	private P1 param1;
	private P2 param2;
	private P3 param3;
	private Comparable<TriParameterValueObject<P1, P2, P3>> compareDelegate;
	
	public TriParameterValueObject() {
	}
	
	public TriParameterValueObject(P1 p1, P2 p2, P3 p3) {
		setParam1(p1);
		setParam2(p2);
		setParam3(p3);
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

	public P3 getParam3() {
		return param3;
	}

	public void setParam3(P3 param3) {
		this.param3 = param3;
	}

	@Override
	public int compareTo(TriParameterValueObject<P1, P2, P3> o) {
		if(this.compareDelegate != null) {
			return compareDelegate.compareTo(o);
		}
		return 0;
	}

	public Comparable<TriParameterValueObject<P1, P2, P3>> getCompareDelegate() {
		return compareDelegate;
	}

	public void setCompareDelegate(Comparable<TriParameterValueObject<P1, P2, P3>> delegate) {
		this.compareDelegate = delegate;
	}
}
