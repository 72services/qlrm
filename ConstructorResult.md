# What about JPA 2.1 Constructor Result?
With JPA 2.1 there will be the ConstructorResult annotation.

Example from the specification:
```java
Query q = em.createNativeQuery(
    "SELECT c.id, c.name, COUNT(o) as orderCount, AVG(o.price) AS avgOrder " +
    "FROM Customer c, Orders o " +
    "WHERE o.cid = c.id " +
    "GROUP BY c.id, c.name",
    "CustomerDetailsResult");

@SqlResultSetMapping(name="CustomerDetailsResult",
    classes={
        @ConstructorResult(targetClass=com.acme.CustomerDetails.class,
            columns={
                @ColumnResult(name="id"),
                @ColumnResult(name="name"),
                @ColumnResult(name="orderCount"),
                @ColumnResult(name="avgOrder", type=Double.class)})
    })
```
## Why QLRM is better?
The problem with that approach is, that you must declare the mapping of the fields in the constructor.
With QLRM this is not necessary. CustomerDetails does not have to be mapped!
```java
Query q = em.createNativeQuery(
    "SELECT c.id, c.name, COUNT(o) as orderCount, AVG(o.price) AS avgOrder " +
    "FROM Customer c, Orders o " +
    "WHERE o.cid = c.id " +
    "GROUP BY c.id, c.name",
    "CustomerDetailsResult");
      
List<EmployeeTO> list = JpaResultMapper.list(q, com.acme.CustomerDetails.class);
```
