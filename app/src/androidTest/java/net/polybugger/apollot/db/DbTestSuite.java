package net.polybugger.apollot.db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApolloDbAdapterTest.class,
        AcademicTermContractTest.class,
        ClassItemTypeContractTest.class,
        ClassContractTest.class,
        StudentContractTest.class
})
public class DbTestSuite { }
