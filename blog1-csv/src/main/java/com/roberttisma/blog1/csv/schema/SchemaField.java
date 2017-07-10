package com.roberttisma.blog1.csv.schema;

import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public interface SchemaField<T> {

  String getFieldName();

  String getAlternativeFieldName();

  Function<T, Object> getGetter();

  static <A> String join(SchemaField<A>[]  schemaFields){
    return stream(schemaFields)
        .map(SchemaField::getFieldName)
        .collect(joining(" , "));
  }

}
