package org.drama.domain;

public interface QueryFactory {
    Queriable<Object> getQuerier(String name);
}
