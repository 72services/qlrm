#Why?
In JPA there is no Constructor Expression for native SQL queries. QLRM fills the gap!

And because the implementation was quite easy there is an implementation for JDBC resultsets as well.

### But what about JPA 2.1 and the ConstructorResult?
Read more: https://github.com/simasch/qlrm/blob/master/ConstructorResult.md

### QLRM 1.6.8
- Switched Java version back to 1.7. Sorry for any inconvenience! 

### QLRM 1.6.6
- Fixes from Nicola Mazarese pull request https://github.com/simasch/qlrm/pull/14

### QLRM 1.6.4
- Contains fixes from Jan Mosigs pull request https://github.com/simasch/qlrm/pull/12

### QLRM 1.6.3 Bugfix
- https://github.com/simasch/qlrm/issues/9 thanks to Jan Mosig for the fix

### QLRM 1.6.1 New Features
- Support for multiple constructors
- Definition of schema for code generation

Thanks to Stefan Heimberg https://github.com/StefanHeimberg, Nicola Mazarese https://github.com/nicolaMaza and Jan Mosig https://github.com/JanMosigItemis for their contribution!

##Maven Dependency
QLRM is available in Maven Central

```xml
<dependency>
    <groupId>ch.simas.qlrm</groupId>
    <artifactId>qlrm</artifactId>
    <version>1.6.8</version>
</dependency>
```

##Usage
Usage is quite forward but be aware of:
- The Constructor must have the same number of arguments as the result of the SQL query
- The result types must match the constructor arguments types

###JPA Native Query
#### List
```java
Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE");
List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);
```
#### Unique Result
```java
Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);
```
###JPQL
#### List
```java
Query q = em.createQuery("SELECT e.id, e.name FROM Employee e");
List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);
```
#### Unique Result
```java
Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = 1");
EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);
```

###JDBC SQL
#### List
```java
stmt.execute("SELECT ID, NAME FROM EMPLOYEE");
List<EmployeeTO> list = jdbcResultMapper.list(stmt.getResultSet(), EmployeeTO.class);
```
#### Unique Result
```java
boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
EmployeeTO to = jdbcResultMapper.uniqueResult(stmt.getResultSet(), EmployeeTO.class);
```

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

```java
classGenerator.generateFromTables("src/test/java/", "ch.simas.sqlresultmapper.to", "TO", false, con, "EMPLOYEE");
```

#License
SQL Result Mapper is open source and free software under Apache License, Version 2:

http://www.apache.org/licenses/LICENSE-2.0.html
