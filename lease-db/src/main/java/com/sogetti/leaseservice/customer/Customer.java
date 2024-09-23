package com.sogetti.leaseservice.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sogetti.leaseservice.car.Car;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore //Id is not in interest for end user
  private Long id;

  @Column(unique = true)
  private String name;

  private String street;
  private String houseNumber;
  private String zipCode;
  private String place;
  private String emailAddress;
  private String phoneNumber;

  @ManyToOne private Car car;

  protected Customer() {}

  public Customer(
      String name,
      String street,
      String houseNumber,
      String zipCode,
      String place,
      String emailAddress,
      String phoneNumber,
      Car car) {
    super();
    this.name = name;
    this.street = street;
    this.houseNumber = houseNumber;
    this.zipCode = zipCode;
    this.place = place;
    this.emailAddress = emailAddress;
    this.phoneNumber = phoneNumber;
    this.car = car;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("Customer [id=")
        .append(id)
        .append(", name=")
        .append(name)
        .append(", street=")
        .append(street)
        .append(", houseNumber=")
        .append(houseNumber)
        .append(", zipCode=")
        .append(zipCode)
        .append(", place=")
        .append(place)
        .append(", emailAddress=")
        .append(emailAddress)
        .append(", phoneNumber=")
        .append(phoneNumber)
        .append(", car=")
        .append(car)
        .append("]");
    return builder.toString();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Car getCar() {
    return car;
  }

  public void setCar(Car car) {
    this.car = car;
  }
}
