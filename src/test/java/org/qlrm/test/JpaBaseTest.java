package org.qlrm.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.qlrm.model.Employee;

public abstract class JpaBaseTest {

    protected EntityManager em;

    protected int employeeId;
    protected String employeeName;

    @BeforeEach
    void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu");
        em = emf.createEntityManager();
        removeAllEmployees();
        Employee employee = new Employee();
        employee.setName("Peter Muster");
        storeEmployee(employee);
        employeeId = employee.getId();
        employeeName = employee.getName();
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
