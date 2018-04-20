package com.aheavenx.dbtestgen.rule;

import com.aheavenx.dbtestgen.generator.TestGenerator;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Alex Storgermann
 *         Creation date: 20.04.18.
 */
public abstract class GenerationSupplier implements TestRule {

    private TestGenerator testGenerator;

    public abstract GenerationSupplier(TestGenerator testGenerator);

    public Statement apply(Statement base, Description description) {
        return null;
    }
}
