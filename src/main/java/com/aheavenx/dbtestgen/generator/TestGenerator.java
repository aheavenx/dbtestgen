package com.aheavenx.dbtestgen.generator;

/**
 * @author Alex Storgermann
 *         Creation date: 19.04.18.
 */
public interface TestGenerator {
    Iterable<Object> getTests(Class<?> clazz);
}
