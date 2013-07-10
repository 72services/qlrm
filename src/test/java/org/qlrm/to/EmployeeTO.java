package org.qlrm.to;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;


public class EmployeeTO {

  private int id;
  private String name;


  public EmployeeTO (int id, String name) {

    this.id = id;
    this.name = name;

  }

  public int getId() {
    return id;
 }
  public String getName() {
    return name;
 }

}
