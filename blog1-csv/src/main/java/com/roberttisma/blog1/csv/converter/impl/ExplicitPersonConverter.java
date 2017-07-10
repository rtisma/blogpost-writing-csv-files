package com.roberttisma.blog1.csv.converter.impl;

import com.roberttisma.blog1.csv.converter.Converter;
import com.roberttisma.blog1.model.Person;

public class ExplicitPersonConverter implements Converter<Person, String[]> {

  @Override
  public String[] convert(Person person) {
    return new String[]{
        person.getId().toString(),
        person.getFirstName(),
        person.getLastName(),
        person.getAge().toString()
    };
  }

}
