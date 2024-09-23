package com.sogetti.leaseservice.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"make", "model", "version"}))
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore // Id is not in interest for end user
  private Long id;

  private String make;
  private String model;
  private String version;
  private int doors;
  private double co2Emission;
  private double grossPrice;

  /** Price of car in euro */
  private double netPrice;

  protected Car() {}

  public Car(
      String make,
      String model,
      String version,
      int doors,
      double co2Emission,
      double grossPrice,
      double netPrice) {
    super();
    this.make = make;
    this.model = model;
    this.version = version;
    this.doors = doors;
    this.co2Emission = co2Emission;
    this.grossPrice = grossPrice;
    this.netPrice = netPrice;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("Car [id=")
        .append(id)
        .append(", make=")
        .append(make)
        .append(", model=")
        .append(model)
        .append(", version=")
        .append(version)
        .append(", doors=")
        .append(doors)
        .append(", co2Emission=")
        .append(co2Emission)
        .append(", grossPrice=")
        .append(grossPrice)
        .append(", netPrice=")
        .append(netPrice)
        .append("]");
    return builder.toString();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getDoors() {
    return doors;
  }

  public void setDoors(int doors) {
    this.doors = doors;
  }

  public double getCo2Emission() {
    return co2Emission;
  }

  public void setCo2Emission(double co2Emission) {
    this.co2Emission = co2Emission;
  }

  public double getGrossPrice() {
    return grossPrice;
  }

  public void setGrossPrice(double grossPrice) {
    this.grossPrice = grossPrice;
  }

  public double getNetPrice() {
    return netPrice;
  }

  public void setNetPrice(double netPrice) {
    this.netPrice = netPrice;
  }
}
