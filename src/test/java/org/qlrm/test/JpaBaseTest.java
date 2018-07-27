package org.qlrm.test;

import org.junit.Before;
import org.qlrm.model.Employee;

import javax.persistence.*;

public abstract class JpaBaseTest {

    protected EntityManager em;

    protected int employeeId;
    protected String emplyoeeName;

    @Before
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu");
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

    private void removeAllEmployees() {
        EntityTransaction trx = em.getTransaction();
        trx.begin();
        Query q = em.createQuery("DELETE FROM Employee");
        q.executeUpdate();
        trx.commit();
    }
}
