package org.drama.vo;

import java.io.Serializable;

public class BiParameterValueObject<P1, P2> implements Serializable {
	private static final long serialVersionUID = 4835032246561552286L;
	private P1 param1;
	private P2 param2;

	public BiParameterValueObject() {
	}
	
	public BiParameterValueObject(P1 p1, P2 p2) {
		setParam1(p1);
		setParam2(p2);
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
}
