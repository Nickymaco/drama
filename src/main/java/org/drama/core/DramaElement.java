package org.drama.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class DramaElement implements InvocationHandler {
	private Element invocator;

	private DramaElement(Element element) {
		invocator = element;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(invocator, args);
	}

	public static <R extends Element> Element proxy(R subject) {
		Class<?> clzz = subject.getClass();
		return (Element) Proxy.newProxyInstance(clzz.getClassLoader(), clzz.getInterfaces(), new DramaElement(subject));
	}
}
