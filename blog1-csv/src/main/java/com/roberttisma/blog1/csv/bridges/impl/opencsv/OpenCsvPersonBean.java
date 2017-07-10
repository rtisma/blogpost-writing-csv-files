package com.roberttisma.blog1.csv.bridges.impl.opencsv;

import com.opencsv.bean.CsvBindByName;
import com.roberttisma.blog1.model.Person;

/**
 * Bean that implements the Person interface inorder to use
 * OpenCSV's @CsvBindByName annotation without modifying
 * the existing (or production) model classes
 */
public class OpenCsvPersonBean implements Person {

  @CsvBindByName
  private Integer id;

  @CsvBindByName
  private String firstName;

  @CsvBindByName
  private String lastName;

  @CsvBindByName
  private Integer age;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public Person setId(Integer id) {
    this.id = id;
    return this;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public Person setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public Person setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Override
  public Integer getAge() {
    return age;
  }

  @Override
  public Person setAge(Integer age) {
    this.age = age;
    return this;
  }

  @Override
  public String toString() {
    return "OpenCsvPersonBean{" +
        "id=" + id +
        ", age=" + age +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        '}';
  }

  /**
   * Converts a Person to a OpenCsvPersonBean
   */
  public static OpenCsvPersonBean convert(Person person) {
    OpenCsvPersonBean bean = new OpenCsvPersonBean();
    bean.setAge(person.getAge())
        .setFirstName(person.getFirstName())
        .setLastName(person.getLastName())
        .setId(person.getId());
    return bean;
  }

}
