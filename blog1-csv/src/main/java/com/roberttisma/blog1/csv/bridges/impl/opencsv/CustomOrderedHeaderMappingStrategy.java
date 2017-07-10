package com.roberttisma.blog1.csv.bridges.impl.opencsv;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.util.Set;

import static com.roberttisma.blog1.csv.Utils.checkState;
import static com.roberttisma.blog1.csv.Utils.setsHaveDifference;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

/**
 * Mapping stategy that allows for a custom header order
 */
public class CustomOrderedHeaderMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

  public CustomOrderedHeaderMappingStrategy(String[] customHeader, Class<? extends T> clazz) {
    setType(clazz);
    checkHeaders(clazz,customHeader, super.generateHeader());
    this.header = customHeader; //Reassign the header variable to the custom
  }

  private void checkHeaders(Class<? extends T> clazz, String[] customHeader, String[] actualHeader) {
    Set<String> customHeaderSet = stream(customHeader).map(String::toUpperCase)
        .collect(toSet()); //Need to convert to upperCase since OpenCSV does the same conversion under the hood
    Set<String> actualHeaderSet = stream(actualHeader).collect(toSet());
    checkState(!setsHaveDifference(actualHeaderSet, customHeaderSet),
        "The class [%s] has capitalized field names %s and the custom capitalized headers %s are NOT EQUAL",
        clazz.getSimpleName(),
        actualHeaderSet,
        customHeaderSet);
  }

}
