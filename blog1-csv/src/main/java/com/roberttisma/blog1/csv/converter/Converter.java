package com.roberttisma.blog1.csv.converter;

public interface Converter<T, R> {

  R convert(T object);

}
