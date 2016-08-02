package org.qlrm.test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.Before;
import org.qlrm.model.Employee;

public abstract class JpaBaseTest {

    protected EntityManager em;

    protected int employeeId;
    protected String emplyoeeName;

    @Before
    public void init() throws ClassNotFoundException, SQLException, FileNotFoundException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("srm");
        em = emf.createEntityManager();
        removeAllEmployees();
        Employee employee = new Employee();
        employee.setName("Peter Muster");
        storeEmployee(employee);
        employeeId = employee.getId();
        emplyoeeName = employee.getName();
    }

    protected void storeEmployee(Employee employee) {
        EntityTransaction trx = em.getTransaction();
        trx.begin();
        em.persist(employee);
        trx.commit();
    }

    protected void removeAllEmployees() {
        EntityTransaction trx = em.getTransaction();
        trx.begin();
        Query q = em.createQuery("DELETE FROM Employee");
        q.executeUpdate();
        trx.commit();
    }
}
