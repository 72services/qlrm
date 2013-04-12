package org.qlrm.to;

import java.sql.*;
import java.util.*;
import java.math.*;


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
