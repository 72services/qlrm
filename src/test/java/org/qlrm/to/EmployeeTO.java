package org.qlrm.to;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;


public class EmployeeTO {

  private Integer id;
  private String name;

  /**
   * INFO stefanheimberg: konstruktor bewusst für tests erstellt.
   */
  public EmployeeTO (String name, Integer id) {

    this.id = id;
    this.name = name;

  }

  public EmployeeTO (Integer id, String name) {

    this.id = id;
    this.name = name;

  }

  /**
   * INFO stefanheimberg: konstruktor bewusst für tests erstellt.
   */
  public EmployeeTO (Integer id, String name, Integer anyOtherParam) {

    this.id = id;
    this.name = name;

  }

  public Integer getId() {
    return id;
 }
  public String getName() {
    return name;
 }

}
