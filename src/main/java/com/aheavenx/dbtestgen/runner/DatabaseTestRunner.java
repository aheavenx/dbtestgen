package com.aheavenx.dbtestgen.runner;

import org.junit.runner.Runner;
import org.junit.runners.Parameterized;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Alex Storgermann
 *         Creation date: 19.04.18.
 */
public class DatabaseTestRunner extends Suite {

    private static final ParametersRunnerFactory DEFAULT_FACTORY = new BlockJUnit4ClassRunnerWithParametersFactory();
    private final List<Runner> runners;


    protected DatabaseTestRunner(Class<?> klass, List<Runner> runners) throws Exception {
        super(klass, Collections.<Runner>emptyList());
        ParametersRunnerFactory runnerFactory = this.getParametersRunnerFactory(klass);
        Iterable<Object> allParameters = null;
        this.runners = Collections.unmodifiableList(this.createRunnersForParameters(allParameters, "{index} {0}, {2}", runnerFactory));
    }

    private ParametersRunnerFactory getParametersRunnerFactory(Class<?> klass)
            throws InstantiationException, IllegalAccessException {
        Parameterized.UseParametersRunnerFactory annotation = klass
                .getAnnotation(Parameterized.UseParametersRunnerFactory.class);
        if (annotation == null) {
            return DEFAULT_FACTORY;
        } else {
            Class<? extends ParametersRunnerFactory> factoryClass = annotation
                    .value();
            return factoryClass.newInstance();
        }
    }

    protected List<Runner> getChildren() {
        return runners;
    }

    private TestWithParameters createTestWithNotNormalizedParameters(
            String pattern, int index, Object parametersOrSingleParameter) {
        Object[] parameters= (parametersOrSingleParameter instanceof Object[]) ? (Object[]) parametersOrSingleParameter
                : new Object[] { parametersOrSingleParameter };
        return createTestWithParameters(getTestClass(), pattern, index,
                parameters);
    }

    private List<Runner> createRunnersForParameters(
            Iterable<Object> allParameters, String namePattern,
            ParametersRunnerFactory runnerFactory)
            throws InitializationError,
            Exception {
        try {
            List<TestWithParameters> tests = createTestsForParameters(
                    allParameters, namePattern);
            List<Runner> runners = new ArrayList<Runner>();
            for (TestWithParameters test : tests) {
                runners.add(runnerFactory
                        .createRunnerForTestWithParameters(test));
            }
            return runners;
        } catch (ClassCastException e) {
            throw parametersMethodReturnedWrongType();
        }
    }

    private List<TestWithParameters> createTestsForParameters(
            Iterable<Object> allParameters, String namePattern)
            throws Exception {
        int i = 0;
        List<TestWithParameters> children = new ArrayList<TestWithParameters>();
        for (Object parametersOfSingleTest : allParameters) {
            children.add(createTestWithNotNormalizedParameters(namePattern,
                    i++, parametersOfSingleTest));
        }
        return children;
    }

    private Exception parametersMethodReturnedWrongType() throws Exception {
        String className = this.getTestClass().getName();
        //todo
        String methodName = "Excel parser";
        String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", new Object[]{className, methodName});
        return new Exception(message);
    }

    private static TestWithParameters createTestWithParameters(
            TestClass testClass, String pattern, int index, Object[] parameters) {
        String finalPattern = pattern.replaceAll("\\{index\\}",
                Integer.toString(index));
        String name = MessageFormat.format(finalPattern, parameters);
        return new TestWithParameters("[" + name + "]", testClass,
                Arrays.asList(parameters));
    }



}
