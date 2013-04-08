# QLRM - Query Language Result Mapper
In JPA there is no Constructor Expression for native SQL queries. QLRM fills the gap!

And because the implementation was quite easy there is an implentation for JDBC as well.

##Usage
Usage is quit forward but be aware of:
- The Constructor must have the same number of arguments as the result of the SQL query
- The result types must match the constructor arguments types

###JPA Native Query
#### List
    Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE");
    List<EmployeeTO> list = JpaSqlResultMapper.list(q, EmployeeTO.class);
#### Unique Result
    Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
    EmployeeTO to = JpaSqlResultMapper.uniqueResult(q, EmployeeTO.class);
###JPQL
#### List
    Query q = em.createQuery("SELECT e.id, e.name FROM Employee e");
    List<EmployeeTO> list = JpaSqlResultMapper.list(q, EmployeeTO.class);
#### Unique Result
    Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = 1");
    EmployeeTO to = JpaSqlResultMapper.uniqueResult(q, EmployeeTO.class);

###JDBC SQL
#### List
    stmt.execute("SELECT ID, NAME FROM EMPLOYEE");
    List<EmployeeTO> list = JdbcSqlResultMapper.list(stmt.getResultSet(), EmployeeTO.class);
#### Unique Result
    boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
    Assert.assertTrue(ok);

##References
The QL Result Mapper is inspired by EclipseLink and Hibernate:
- http://onpersistence.blogspot.ch/2010/07/eclipselink-jpa-native-constructor.html
- http://docs.jboss.org/hibernate/orm/3.3/reference/en/html/querysql.html#d0e13904

# Class Generator
ClassGenerator is a simple utility to generate transfer objects from database tables.

##Usage
The first parameter is the path where the source file should be generated to. The second is the package name, third a suffix.
With the forth parameter you can define if the fields should be public or if the generator must generate getters. 
Then a database connection must be passed. And the last parameter is a vargargs where you can passe one or multiple table names.

    ClassGenerator.generateFromTables("src/test/java/", "ch.simas.sqlresultmapper.to", "TO", false, con, "EMPLOYEE");

#License
SQL Result Mapper is open source and free software under Apache License, Version 2:

http://www.apache.org/licenses/LICENSE-2.0.html
