package org.qlrm.to;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;


public class EmployeeTO {

  private Integer id;
  private String name;


  public EmployeeTO (Integer id, String name) {

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
