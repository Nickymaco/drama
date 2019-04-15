package org.drama.domain;

public interface QueryFactory {
    Queriable<Object> getQuerier(Class<?> clazz);
}
