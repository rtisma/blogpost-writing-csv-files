package com.roberttisma.blog1.csv.schema;

import com.roberttisma.blog1.model.Person;

import java.util.function.Function;

import static java.util.Arrays.stream;

public enum PersonSchema implements SchemaField<Person> {
  id("personId",Person::getId),
  firstName("personFirstName", Person::getFirstName),
  lastName("personLastName", Person::getLastName),
  age("personAge", Person::getAge);

  private final String fieldName;
  private final String alternativeFieldName;
  private final Function<Person, Object> getter;

  /**
   * Construct PersonField with associated OutputColumnName and getter
   * @param alternativeFieldName is the name of this PersonField in the csv file
   * @param getter is the argumentless producer of the object Person that is used to write data to the csv file
   */
  PersonSchema(String alternativeFieldName, Function<Person, Object> getter) {
    this.fieldName = name();
    this.getter = getter;
    this.alternativeFieldName = alternativeFieldName;
  }

  public static SchemaField<Person>[] getSchema(){
    return values();
  }

  /**
   * If the alternativeFieldName is not explicitly defined, it is assigned
   * the fieldName by default
   */
  PersonSchema(Function<Person, Object> getter) {
    this.fieldName = name();
    this.getter = getter;
    this.alternativeFieldName = fieldName;
  }

  @Override
  public String getFieldName() {
    return fieldName;
  }

  @Override
  public String getAlternativeFieldName() {
    return alternativeFieldName;
  }

  @Override
  public Function<Person, Object> getGetter() {
    return getter;
  }

  public static String[] getFieldNames() {
    return stream(values())
        .map(SchemaField::getFieldName)
        .toArray(String[]::new);
  }

}
