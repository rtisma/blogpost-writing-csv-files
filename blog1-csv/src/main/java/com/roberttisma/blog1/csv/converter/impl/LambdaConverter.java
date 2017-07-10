package com.roberttisma.blog1.csv.converter.impl;

import com.roberttisma.blog1.csv.converter.Converter;
import com.roberttisma.blog1.csv.schema.SchemaField;

import static java.util.Arrays.stream;

public class LambdaConverter<T> implements Converter<T, String[]> {

  private final SchemaField<T>[] schema;

  private LambdaConverter(SchemaField<T>[] schema) {
    this.schema = schema;
  }

  @Override
  public String[] convert(T object) {
    return stream(schema)
        .map(SchemaField::getGetter)
        .map(x -> x.apply(object))
        .map(Object::toString)
        .toArray(String[]::new);
  }

  public static <T> LambdaConverter<T> createLambdaConverter(SchemaField<T>[] schema) {
    return new LambdaConverter<>(schema);
  }

}
