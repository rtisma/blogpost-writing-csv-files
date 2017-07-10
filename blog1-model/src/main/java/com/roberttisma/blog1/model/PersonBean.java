package com.roberttisma.blog1.model;

import java.io.Serializable;

/**
 * Considered PRODUCTION CODE and cannot be modified
 */
public class PersonBean implements Person, Serializable {

  private static final long serialVersionUID = 1499699879L;

  private Integer id;
  private String firstName;
  private String lastName;
  private Integer age;

  public Integer getId() {
    return id;
  }

  public Person setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Person setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Person setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public Integer getAge() {
    return age;
  }

  public Person setAge(Integer age) {
    this.age = age;
    return this;
  }

  @Override
  public String toString() {
    return "PersonBean{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", age=" + age +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PersonBean)) return false;

    PersonBean that = (PersonBean) o;

    if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
    if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
      return false;
    if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null) return false;
    return getAge() != null ? getAge().equals(that.getAge()) : that.getAge() == null;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
    result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
    result = 31 * result + (getAge() != null ? getAge().hashCode() : 0);
    return result;
  }

  public static PersonBean createPersonBean(int id, String firstName, String lastName, int age) {
    PersonBean personBean = new PersonBean();
    personBean
        .setAge(age)
        .setId(id)
        .setLastName(lastName)
        .setFirstName(firstName);
    return personBean;
  }

}
