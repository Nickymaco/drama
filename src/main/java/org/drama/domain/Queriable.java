package org.drama.domain;

import java.util.List;

/**
 * 针对数据进行 insert, delte, update 操作指令
 * @param <T>
 */
public interface Queriable<T> {
    int create(T parameter);
    int delete(T parameter);
    int update(T parameter);
    T retrieve(T parameter);
    List<T> retrieveList(T parameter);
}
