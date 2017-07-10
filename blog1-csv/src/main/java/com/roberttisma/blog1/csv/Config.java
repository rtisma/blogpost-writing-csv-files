package com.roberttisma.blog1.csv;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Character.MIN_VALUE;

public class Config {

  public static final Character COMMA = ',';
  public static final Character EMPTY_CHARACTER = MIN_VALUE;

  public static final Character DEFAULT_SEPARATOR = COMMA;
  public static final Character DEFAULT_QUOTE = EMPTY_CHARACTER;
  public static final Path OUTPUT_CSV_DIRPATH = Paths.get("csvOutputDir");

}
