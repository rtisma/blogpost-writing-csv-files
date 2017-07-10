package com.roberttisma.blog1.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class Utils {

  public static void checkState(boolean passCondition, String formattedMessage, Object... args) {
    if (!passCondition) {
      throw new IllegalStateException(format(formattedMessage, args));
    }
  }

  public static <T> boolean setsHaveDifference(Set<T> leftSet, Set<T> rightSet) {
    return !leftSet.containsAll(rightSet) || !rightSet.containsAll(leftSet);
  }

  public static <T, R> List<R> transform(Function<T, R> function, List<? extends T> data) {
    return data.stream().map(function).collect(toList());
  }

  /**
   * Reads a file and stores all its contents into a String
   */
  public static String readFileToString(Path path) throws FileNotFoundException {
    checkState(path.toFile().exists(), "File [%s] does not exist", path.toString());
    checkState(path.toFile().isFile(), "File [%s] is not a file", path.toString());
    BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
    return br.lines().collect(Collectors.joining("\n"));
  }

  public static void closeObject(Closeable o) throws IOException {
    if (o != null){
      o.close();
    }
  }

  public static String resolveChar(Character c){
    if (c.equals('\0')){
      return "";
    }
    return c.toString();
  }

}
