package com.roberttisma.blog1.model;

/**
 * Considered PRODUCTION CODE and cannot be modified
 */
public interface Person {

  Integer getId();

  Person setId(Integer id);

  String getFirstName();

  Person setFirstName(String firstName);

  String getLastName();

  Person setLastName(String lastName);

  Integer getAge();

  Person setAge(Integer age);

  default boolean equalsPerson(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person)) return false;

    Person that = (Person) o;

    if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
    if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
      return false;
    if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null) return false;
    return getAge() != null ? getAge().equals(that.getAge()) : that.getAge() == null;
  }

}
