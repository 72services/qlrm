package org.qlrm.to;

public class EmployeeTO {

    private final Integer id;
    private final String name;

    /**
     * INFO stefanheimberg: konstruktor bewusst für tests erstellt.
     */
    public EmployeeTO(String name, Integer id) {

        this.id = id;
        this.name = name;

    }

    public EmployeeTO(Integer id, String name) {

        this.id = id;
        this.name = name;

    }

    /**
     * INFO stefanheimberg: konstruktor bewusst für tests erstellt.
     */
    public EmployeeTO(Integer id, String name, Integer anyOtherParam) {

        this.id = id;
        this.name = name;

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EmployeeTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
